package com.chen.bizcenter.common.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: chenille
 * @Date: 2024-11-19 23:08
 */
@Configuration
public class WebMainConfig {

    /**
     * controller打印日志切面
     *
     * @return {@link ApiLogAop}
     */
    @Bean
    @ConditionalOnProperty(name = "chen.base.api.log.enable", havingValue = "true")
    public ApiLogAop apiLogAop() {
        return new ApiLogAop();
    }
}
