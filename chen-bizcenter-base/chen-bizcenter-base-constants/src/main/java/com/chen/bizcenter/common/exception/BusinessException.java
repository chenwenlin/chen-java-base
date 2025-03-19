package com.chen.bizcenter.common.exception;

import com.chen.bizcenter.common.enums.interfaces.BaseErrorCodeIEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: chenille
 * @Date: 2024-11-19 23:11
 */
@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException implements BaseErrorCodeIEnum {

    private static final long serialVersionUID = -2907380034202458263L;

    private final String code;
    private final String msg;

    public BusinessException(BaseErrorCodeIEnum codeEnum) {
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
    }
}
