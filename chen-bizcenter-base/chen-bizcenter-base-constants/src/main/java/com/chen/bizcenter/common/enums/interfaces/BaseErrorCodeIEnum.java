package com.chen.bizcenter.common.enums.interfaces;

/**
 * @Author: chenille
 * @Date: 2024-10-25 22:02
 **/
public interface BaseErrorCodeIEnum {

    /**
     * 获取错误码
     *
     * @return 错误码code
     */
    String getCode();

    /**
     * 获取错误码信息
     *
     * @return 错误码msg
     */
    String getMsg();
}
