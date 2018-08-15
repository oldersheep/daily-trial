package com.xxx.notes.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @ClassName RedisTemplateConfig
 * @Description 使用spring data redis进行配置
 * @Author l17561
 * @Date 2018/7/20 13:59
 * @Version V1.0
 */
@Configuration
@PropertySource(value = "classpath:redis.yml", factory = YmlPropertyFactory.class)
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String, Object> billredisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> templateFor = new RedisTemplate<String, Object>();
        templateFor.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<String> ser = new Jackson2JsonRedisSerializer<String>(String.class);
        templateFor.setDefaultSerializer(ser);
        return templateFor;
    }

}
