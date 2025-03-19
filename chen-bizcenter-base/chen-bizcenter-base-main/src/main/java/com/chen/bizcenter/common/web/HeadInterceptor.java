package com.chen.bizcenter.common.web;

import com.chen.bizcenter.common.HeadParamUtil;
import com.chen.bizcenter.common.head.HeadParam;
import com.chen.bizcenter.common.head.HeadParamConstant;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求头拦截器
 *
 * @Author: chenille
 * @Date: 2024-10-25 23:01
 **/
@Slf4j
public class HeadInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {
        HeadParam headParam = HeadParamUtil.getHeadParam();
        String userId = request.getHeader(HeadParamConstant.USER_ID);
        if (StringUtils.isNoneBlank(userId)) {
            headParam.setUserId(Long.parseLong(userId));
        }

        String userName = request.getHeader(HeadParamConstant.USER_NAME);
        if (StringUtils.isNoneBlank(userName)) {
            headParam.setUserName(userName);
        }

        String appId = request.getHeader(HeadParamConstant.APP_ID);
        if (StringUtils.isNoneBlank(appId)) {
            headParam.setAppId(appId);
        }

        String channel = request.getHeader(HeadParamConstant.CHANNEL);
        if (StringUtils.isNoneBlank(channel)) {
            headParam.setChannel(channel);
        }
        HeadParamUtil.setHeadParam(headParam);
        return true;
    }
}
