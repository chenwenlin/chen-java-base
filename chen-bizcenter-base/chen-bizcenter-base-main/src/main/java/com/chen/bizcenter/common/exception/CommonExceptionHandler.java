package com.chen.bizcenter.common.exception;

import com.chen.bizcenter.common.vo.res.ResponseVO;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

/**
 * 全局异常处理器
 *
 * @Author: chenille
 * @Date: 2024-10-25 22:59
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    private String appId;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseVO<Void> handleBusinessException(BusinessException e) {
        logWarnException(e);
        return ResponseVO.error(e.getCode(), e.getMsg());
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseVO<Void> handlerNoFoundException(NoHandlerFoundException e) {
        logException(e);
        return ResponseVO.error(getCode(Error.NO_HANDLER_FOUND), Error.NO_HANDLER_FOUND.getMsg());
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseVO<Void> handleDuplicateKeyException(DuplicateKeyException e) {
        logException(e);
        return ResponseVO.error(getCode(Error.DUPLICATE_KEY), Error.DUPLICATE_KEY.getMsg());
    }


    @ExceptionHandler(Exception.class)
    public ResponseVO<Void> handleException(Exception e) {
        logException(e);
        return ResponseVO.error(getCode(Error.UNKNOWN), Error.UNKNOWN.getMsg());
    }

    /**
     * 处理请求对象属性不满足校验规则的异常信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseVO<Void> exception(MethodArgumentNotValidException exception) {
        logWarnException(exception);
        return ResponseVO.error(getCode(Error.METHOD_ARGUMENT_NOT_VALID), Error.METHOD_ARGUMENT_NOT_VALID.getMsg());
    }

    /**
     * 参数类型数据异常
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseVO<Void> exception(MethodArgumentTypeMismatchException exception) {
        logWarnException(exception);
        return ResponseVO.error(getCode(Error.METHOD_ARGUMENT_TYPE_MISMATCH), Error.METHOD_ARGUMENT_TYPE_MISMATCH.getMsg());
    }


    /**
     * 丢失参数异常
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseVO<Void> exception(MissingServletRequestParameterException exception) {
        logWarnException(exception);
        return ResponseVO.error(getCode(Error.MISSING_SERVLET_REQUEST_PARAMETER), Error.MISSING_SERVLET_REQUEST_PARAMETER.getMsg());
    }


    /**
     * 处理请求单个参数不满足校验规则的异常信息
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseVO<Void> constraintViolationExceptionHandler(ConstraintViolationException exception) {
        logWarnException(exception);
        return ResponseVO.error(getCode(Error.CONSTRAINT_VIOLATION), Error.CONSTRAINT_VIOLATION.getMsg());
    }

    /**
     * 参数无效的异常信息处理。
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseVO<Void> handleIllegalArgumentException(IllegalArgumentException exception) {
        logWarnException(exception);
        return ResponseVO.error(getCode(Error.ILLEGAL_ARGUMENT), Error.ILLEGAL_ARGUMENT.getMsg());
    }

    /**
     * 消息不可读异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseVO<Void> handleIllegalArgumentException(HttpMessageNotReadableException exception) {
        logException(exception);
        return ResponseVO.error(getCode(Error.HTTP_MESSAGE_NOT_READABLE), Error.HTTP_MESSAGE_NOT_READABLE.getMsg());
    }


    @Getter
    @AllArgsConstructor
    public enum Error {
        UNKNOWN("000", "未知错误，请联系系统管理员", Type.SYS),
        NO_HANDLER_FOUND("001", "路径不存在，请检查路径是否正确", Type.SYS),

        DUPLICATE_KEY("000", "数据库中已存在该记录", Type.BUS),
        METHOD_ARGUMENT_NOT_VALID("001", "请求参数校验失败.", Type.BUS),
        METHOD_ARGUMENT_TYPE_MISMATCH("002", "请求参数类型不匹配.", Type.BUS),
        MISSING_SERVLET_REQUEST_PARAMETER("003", "请求参数缺失.", Type.BUS),
        CONSTRAINT_VIOLATION("005", "请求参数不满足约束条件.", Type.BUS),
        ILLEGAL_ARGUMENT("006", "请求参数不合法.", Type.BUS),
        HTTP_MESSAGE_NOT_READABLE("007", "Http 消息不可读.", Type.BUS),
        ;
        private final String code;
        private final String msg;
        private final Type type;
    }

    /**
     * 获取错误编码
     *
     * @param error error
     * @return code
     */
    public String getCode(Error error) {
        return error.getType().getCode() + "." + appId + error.getCode();
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        SYS("sys"),
        BUS("bus"),
        ;
        private final String code;
    }

    private void logException(Exception e) {
        log.error(StringUtils.hasText(e.getMessage()) ? e.getMessage() : "异常：" + e.getClass().getName(), e);
    }

    private void logWarnException(Exception e) {
        log.warn(StringUtils.hasText(e.getMessage()) ? e.getMessage() : "异常：" + e.getClass().getName(), e);
    }
}
