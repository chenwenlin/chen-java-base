package com.chen.bizcenter.common.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: chenille
 * @Date: 2024-11-18 23:37
 */
@Configurable
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 加入框架的拦截器，用于处理header中的公共参数
        registry.addInterceptor(new HeadInterceptor()).addPathPatterns("/**");
    }
}
