package com.xxx.notes.configure;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public class EhCacheConfiguration {

    private static  final String EhCacheConfigPath = "/ehcache.xml";

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource(EhCacheConfigPath));
        cacheManagerFactoryBean.setShared(true);

        return cacheManagerFactoryBean;
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean factoryBean) {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager(factoryBean.getObject());
        ehCacheCacheManager.initializeCaches();//初始化
        return ehCacheCacheManager;
    }

    /**
     * 获取guava 实列的缓存
     *
     * @return guava缓存管理 实列
     */
    private GuavaCacheManager getGuavaCacheManager() {
        GuavaCacheManager guavaCacheManager = new GuavaCacheManager();
        guavaCacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(3600, TimeUnit.SECONDS).maximumSize(1000));
        ArrayList<String> guavaCacheNames = Lists.newArrayList();
        guavaCacheNames.add("GUAVA_CACHE");
        guavaCacheManager.setCacheNames(guavaCacheNames);
        return guavaCacheManager;
    }

    @Bean
    public Cache demo(EhCacheCacheManager ehCacheCacheManager) {
        return ehCacheCacheManager.getCache("demo");
    }

    @Bean
    public Cache demo2(EhCacheCacheManager ehCacheCacheManager) {
        return ehCacheCacheManager.getCache("demo2");
    }
}
