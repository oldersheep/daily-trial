package com.xxx.notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ehcache")
@CacheConfig(cacheNames = {"demo", "demo2"})
public class EhCacheController {

    @Autowired
    @Qualifier("demo")
    private Cache cache;

    @Cacheable(key = "#str", value = "demo")
    @RequestMapping("/set")
    public String set(@RequestParam String str) {

        System.out.println(str);
        return str;
    }

    @RequestMapping("/get")
    public String put() {

        return cache.get("123", String.class);
    }
}
