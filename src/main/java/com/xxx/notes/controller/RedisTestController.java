package com.xxx.notes.controller;

import com.xxx.notes.base.annotation.Key;
import com.xxx.notes.base.annotation.SaveRedis;
import com.xxx.notes.entity.MemberEntity;
import com.xxx.notes.dto.SysResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RedisController
 * @Description TODO
 * @Author l17561
 * @Date 2018/7/19 17:20
 * @Version V1.0
 */
@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    private RedisTemplate<String, Object> template;


    @SaveRedis(prefix = "USER")
    @ApiOperation(value = "Redis设值", notes = "Redis设值")
    @RequestMapping(value = "/set/{key}/{value}", method = RequestMethod.GET)
    public SysResult setKey(@PathVariable(name="key") @Key String key,
                            @PathVariable(name="value") String value){

        return SysResult.ok();
    }


    /**
     *  这里先不纠结了，问题很大
     * @param key
     * @param value
     * @return
     */
    @ApiOperation(value = "RedisTemplate使用", notes = "RedisTemplate使用")
    @RequestMapping(value = "/put/{key}/{value}", method = RequestMethod.GET)
    public SysResult templateOpera(@PathVariable(name="key") String key,
                                   @PathVariable(name="value") String value){

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMid("abc");
        memberEntity.setName("abcdefg");
        template.opsForValue().set(key, memberEntity);

        return null;
    }
}
