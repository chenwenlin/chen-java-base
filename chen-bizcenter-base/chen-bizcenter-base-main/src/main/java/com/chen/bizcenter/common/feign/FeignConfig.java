package com.chen.bizcenter.common.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: chenille
 * @Date: 2024-11-19 23:08
 */
@Configuration
public class FeignConfig {

    /**
     * feign打印日志切面
     *
     * @return {@link FeignLogAop}
     */
    @Bean
    @ConditionalOnProperty(name = "chen.base.feign.log.enable", havingValue = "true")
    public FeignLogAop feignLogAspect() {
        return new FeignLogAop();
    }
}
