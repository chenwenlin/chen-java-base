package com.chen.bizcenter.common.feign;

import com.alibaba.fastjson.JSONObject;
import com.chen.bizcenter.common.vo.res.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * feign 请求日志AOP
 *
 * @Author: chenille
 * @Date: 2024-10-25 23:00
 */
@Aspect
@Slf4j
@ConditionalOnProperty(value = "chen.base.feign.log.enable", havingValue = "true")
public class FeignLogAop {

    @Value("${chen.base.feign.log.enable:false}")
    private boolean logEnabled;

    /**
     * 打印请求日志的长度
     */
    @Value("${chen.base.feign.log.req.size:-1}")
    private Integer requestParamSize;

    /**
     * 打印返回日志的长度
     */
    @Value("${chen.base.feign.log.res.size:-1}")
    private Integer responseLogSize;


    @Pointcut("execution(public * com.chen..*.*(..))")
    public void yzFeignClient() {
    }

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
    public void feignClient() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMethod() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void request() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMethod() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMethod() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMethod() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchMethod() {
    }

    @Pointcut("request() || getMethod() || postMethod() || putMethod() || deleteMethod() || patchMethod()")
    public void allRequest() {
    }


    @Around("yzFeignClient() && feignClient() && allRequest()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!logEnabled) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();
        final String typeName = joinPoint.getSignature().getDeclaringTypeName();
        final String methodName = joinPoint.getSignature().getName();
        this.quietExecute(() -> {
            String reqParam = subtractReqParamIfExceed(JSONObject.toJSONString(deleteSomeArgs(joinPoint)), requestParamSize);
            log.info("==feign client请求开始== [请求类名]:{},[请求方法名]:{}, [请求参数]:{}", typeName, methodName, reqParam);
        });
        Object retVal = null;
        try {
            Object[] args = joinPoint.getArgs();
            retVal = joinPoint.proceed(args);
            return retVal;
        } finally {
            Object finalRetVal = retVal;
            this.quietExecute(() -> {
                long endTime = System.currentTimeMillis();
                if (finalRetVal instanceof ResponseVO) {
                    String logStr = subtractReqParamIfExceed(JSONObject.toJSONString(finalRetVal), responseLogSize);
                    log.info("==feign client请求结束== [请求类名]:{},[请求方法名]:{}, [请求耗时]:{}毫秒, [返回数据]:{}", typeName, methodName, endTime - startTime, logStr);
                } else {
                    log.info("==feign client请求结束== [请求类名]:{},[请求方法名]:{}, [请求耗时]:{}毫秒", typeName, methodName, endTime - startTime);
                }
            });
        }
    }

    /**
     * 字符串分割
     *
     * @param value 被分割的字符串
     * @param size  需要分割的size
     * @return 分割后的字符串
     */
    private String subtractReqParamIfExceed(String value, Integer size) {
        if (size > 0 && value != null && value.length() > size) {
            return value.substring(0, size);
        }
        return value;
    }

    private void quietExecute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("log object serialize may throw Exception", e);
        }
    }

    private Object deleteSomeArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<Object> argList = new ArrayList<>();
        for (Object arg : args) {
            if (arg != null && filterArg(arg)) {
                argList.add(arg);
            }
        }
        return argList;
    }

    private boolean filterArg(Object arg) {
        if (arg instanceof HttpServletRequest) {
            return false;
        } else if (arg instanceof HttpServletResponse) {
            return false;
        } else if (arg instanceof Throwable) {
            return false;
        } else if (arg instanceof BindingResult) {
            return false;
        } else if (arg instanceof MultipartFile) {
            return false;
        } else {
            return !(arg instanceof MultipartFile[]);
        }
    }
}
