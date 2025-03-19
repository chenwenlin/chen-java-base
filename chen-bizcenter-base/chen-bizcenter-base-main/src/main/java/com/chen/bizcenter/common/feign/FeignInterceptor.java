package com.chen.bizcenter.common.feign;

import com.chen.bizcenter.common.HeadParamUtil;
import com.chen.bizcenter.common.head.HeadParam;
import com.chen.bizcenter.common.head.HeadParamConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * feign请求头配置类
 *
 * @Author: chenille
 * @Date: 2024-11-18 23:12
 **/
@Slf4j
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HeadParam headParam;
        try {
            headParam = HeadParamUtil.getHeadParam();
        } catch (Exception e) {
            return;
        }
        if (headParam == null) {
            return;
        }

        if (headParam.getAppId() != null) {
            requestTemplate.header(HeadParamConstant.APP_ID, headParam.getAppId());
        }

        if (headParam.getUserId() != null) {
            requestTemplate.header(HeadParamConstant.USER_ID, String.valueOf(headParam.getUserId()));
        }

        if (headParam.getUserName() != null) {
            requestTemplate.header(HeadParamConstant.USER_NAME, headParam.getUserName());
        }
        if (headParam.getChannel() != null) {
            requestTemplate.header(HeadParamConstant.CHANNEL, headParam.getChannel());
        }
    }
}
