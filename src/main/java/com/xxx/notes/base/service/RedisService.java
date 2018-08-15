package com.xxx.notes.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName RedisService
 * @Description Redis操作的基本服务类
 * @Author l17561
 * @Date 2018/7/20 9:34
 * @Version V1.0
 */
@Component
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    /**
     *  往Redis里面存入内容
     * @param key key
     * @param value value
     */
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设值时发生错误->",e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     *  往Redis里面存入内容，并设置过期时间
     * @param key key
     * @param value value
     * @param exp 过期时间
     */
    public void set(String key, String value, int exp) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, exp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设值时发生错误->",e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     *  为key单独设置过期时间
     * @param key key值
     * @param exp 过期时间
     */
    public void expire(String key, int exp) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, exp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设值时发生错误->",e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     *  根据key从Redis中取值
     * @param key key
     * @return value
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设值时发生错误->",e);
            return  null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

}
