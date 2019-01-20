package com.xxx.notes.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName RedisTemplateConfig
 * @Description 使用原生jedis进行配置
 * @Author l17561
 * @Date 2018/7/12 13:59
 * @Version V1.0
 */
// https://blog.csdn.net/plei_yue/article/details/79362372
@Configuration
@EnableCaching
@PropertySource(value = "classpath:redis.yml", factory = YmlPropertyFactory.class)
public class JedisConfiguration { //extends CachingConfigurerSupport { // 这里将其注释掉，另有考虑

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password; // 暂时不用

    @Bean
    public Jedis jedis(JedisPool jedisPool){

        return jedisPool.getResource();
    }

    @Bean
    public JedisPool redisPoolFactory() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        return new JedisPool(jedisPoolConfig, host, port, timeout);
    }
}
