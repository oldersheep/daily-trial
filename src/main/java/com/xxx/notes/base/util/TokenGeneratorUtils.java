package com.xxx.notes.base.util;

import org.springframework.util.DigestUtils;

/**
 * @ClassName TokenGeneratorUtils
 * @Description
 * @Author l17561
 * @Date 2018/8/13 9:12
 * @Version V1.0
 */
public class TokenGeneratorUtils {

    /**
     * MD5形式的token生成方式
     * @param strings
     * @return
     */
    public static String md5Generate(String... strings) {
        long timestamp = System.currentTimeMillis();

        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }
}
