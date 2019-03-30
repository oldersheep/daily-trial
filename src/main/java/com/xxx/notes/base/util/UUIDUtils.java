package com.xxx.notes.base.util;

import java.util.UUID;

/**
 * @Description 生成唯一标识
 * @Author l17561
 * @Date 2019/3/26 10:28
 * @Version V1.0
 */
public class UUIDUtils {

    /**
     * 获取随机UUID（使用JDK生成）
     * @return UUID
     */
    public static String getUUID() {
        while (true) {
            long uuid = UUID.randomUUID().getMostSignificantBits();
            if (uuid > 0) {
                return uuid + "";
            }
        }
    }

    /**
     * 生成一个纯数字型的UUID。
     */
    public static String absNumUUID() {
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits()));
    }

    /**
     * 生成一个带-的UUID,长度为36位.
     * @return
     */
    public static String get() {
        return UUID.randomUUID().toString();
    }
}
