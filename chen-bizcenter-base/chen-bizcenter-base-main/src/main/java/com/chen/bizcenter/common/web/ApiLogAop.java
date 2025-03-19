package com.chen.bizcenter.common.web;

import com.alibaba.fastjson.JSONObject;
import com.chen.bizcenter.common.HeadParamUtil;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


/**
 * controller请求日志AOP
 *
 * @Author: chenille
 * @Date: 2024-10-25 23:00
 */
@Slf4j
@Aspect
@ConditionalOnProperty(value = "chen.base.api.log.enable", havingValue = "true")
public class ApiLogAop {

    /**
     * 是否开启打印返回日志
     */
    @Value("${chen.base.api.log.res.enable:false}")
    private boolean enableResLog;

    /**
     * 打印返回日志的长度
     */
    @Value("${chen.base.api.log.res.size:-1}")
    private Integer responseLogSize;

    /**
     * 是否开启打印请求日志
     */
    @Value("${chen.base.api.log.req.enable:false}")
    private boolean enableReqLog;

    /**
     * 打印请求日志的长度
     */
    @Value("${chen.base.api.log.req.size:-1}")
    private Integer requestParamSize;


    @Pointcut("")
    public void restController() {
    }

    @Pointcut("execution(public * com.chen..*.*(..))")
    public void yzController() {
    }

    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void controller() {
    }


    @Around("yzController() && controller()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 请求参数打印
        this.quietExecute(() -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return;
            }
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (enableReqLog) {
                String result = RequestMethod.GET.name().equals(request.getMethod()) ? JSONObject.toJSONString(request.getParameterMap()) : JSONObject.toJSONString(dealSomeArgs(joinPoint));
                String reqParam = subtractReqParamIfExceed(result, requestParamSize);
                log.info("==请求开始== [请求IP]:{} ,[请求方式]:{}， [请求URL]:{}, [请求类名]:{}, [请求方法名]:{}, [请求头参数]:{}, [请求参数]:{}",
                        request.getRemoteAddr(), request.getMethod(), request.getRequestURL(), joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), JSONObject.toJSONString(HeadParamUtil.getHeadParam()), reqParam);
            } else {
                log.info("==请求开始== [请求IP]:{} ,[请求方式]:{}， [请求URL]:{}, [请求类名]:{}, [请求方法名]:{}, [请求头参数]:{}",
                        request.getRemoteAddr(), request.getMethod(), request.getRequestURL(), joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), JSONObject.toJSONString(HeadParamUtil.getHeadParam()));
            }
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
                if (enableReqLog && finalRetVal instanceof ResponseVO) {
                    String resValue = subtractReqParamIfExceed(JSONObject.toJSONString(finalRetVal), responseLogSize);
                    log.info("==请求结束== [请求耗时]:{}毫秒, [返回数据]:{}", endTime - startTime, resValue);
                } else {
                    log.info("==请求结束== [请求耗时]:{}毫秒", endTime - startTime);
                }
            });
        }
    }

    private void quietExecute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("log object serialize may throw Exception", e);
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

    /**
     * 处理body中的参数
     *
     * @param joinPoint joinPoint
     * @return body参数
     */
    private Object dealSomeArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<Object> argList = new ArrayList<>();
        for (Object arg : args) {
            if (arg != null && filterArg(arg)) {
                argList.add(arg);
            }
        }
        return argList;
    }

    /**
     * 需要过滤掉的参数类型
     *
     * @param arg arg
     * @return true/false
     */
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
