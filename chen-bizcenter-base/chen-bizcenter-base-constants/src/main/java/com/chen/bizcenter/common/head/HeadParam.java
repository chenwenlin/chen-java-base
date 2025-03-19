package com.chen.bizcenter.common.head;

import lombok.Data;

/**
 * 请求头参数
 *
 * @Author: chenille
 * @Date: 2024-11-18 23:16
 **/
@Data
public class HeadParam {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 来源appID
     */
    private String appId;

    /**
     * 渠道编码
     */
    private String channel;
}
