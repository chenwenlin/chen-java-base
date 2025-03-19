package com.chen.bizcenter.common.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author: chenille
 * @Date: 2024-11-18 23:40
 */
public interface MvcObjectMapperCustomizer {
    /**
     * 提供的扩展自定义序列化方式
     *
     * @param objectMapper objectMapper
     */
    void customize(ObjectMapper objectMapper);
}
