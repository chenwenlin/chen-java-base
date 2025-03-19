package com.chen.bizcenter.common.serialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

/**
 * jackson序列化配置
 *
 * @Author: chenille
 * @Date: 2024-11-18 23:40
 */
@Configuration
public class JacksonSerializeConfig {

    /**
     * 自定义的序列化
     */
    private final List<MvcObjectMapperCustomizer> customizers;

    /**
     * 构造方法注入bean
     *
     * @param customizers customizers
     */
    public JacksonSerializeConfig(List<MvcObjectMapperCustomizer> customizers) {
        this.customizers = customizers;
    }

    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 忽略value为null 时 key的输出
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);

        // 设置自定义的序列化属性
        if (customizers != null && !customizers.isEmpty()) {
            for (MvcObjectMapperCustomizer customizer : customizers) {
                // 添加自定义的配置
                customizer.customize(objectMapper);
            }
        }
        return objectMapper;
    }
}
