package com.chen.bizcenter.common;

import com.chen.bizcenter.common.head.HeadParam;

/**
 * @Author: chenille
 * @Date: 2024-11-18 23:19
 **/
public class HeadParamUtil {

    /**
     * ThreadLocal 存放请求头信息
     */
    private final static ThreadLocal<HeadParam> THREAD_LOCAL = ThreadLocal.withInitial(HeadParam::new);

    /**
     * 获取请求头HeadParam
     */
    public static HeadParam getHeadParam() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置HeadParam
     */
    public static void setHeadParam(HeadParam extraParam) {
        THREAD_LOCAL.set(extraParam);
    }

    /**
     * 清理ThreadLocal（注意！！每个线程使用完ThreadLocal之后一定要清理ThreadLocal）
     */
    public static void destroy() {
        THREAD_LOCAL.remove();
    }
}
