package com.chen.bizcenter.common.vo.res;

import com.chen.bizcenter.common.enums.interfaces.BaseErrorCodeIEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回对象VO
 *
 * @Author: chenille
 * @Date: 2024-10-25 22:24
 **/
@Data
public class ResponseVO<T> implements Serializable {
    private static final long serialVersionUID = -9200122821274720605L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码message
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回错误信息
     *
     * @param codeEnum 错误信息枚举
     * @return {@link ResponseVO<Void>}
     */
    public static ResponseVO<Void> error(BaseErrorCodeIEnum codeEnum) {
        ResponseVO<Void> result = new ResponseVO<>();
        result.setCode(codeEnum.getCode());
        result.setMessage(codeEnum.getMsg());
        return result;
    }

    /**
     * 返回错误信息
     *
     * @param code code
     * @param msg msg
     * @return {@link ResponseVO<Void>}
     */
    public static ResponseVO<Void> error(String code, String msg) {
        ResponseVO<Void> result = new ResponseVO<>();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
