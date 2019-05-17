package com.xxx.notes.base.util;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author l17561
 * @Date 2019/5/17 17:49
 * @Version V1.0
 */
public class RegexUtils {

    /**
     * 固定电话
     */
    private static final Pattern TELEPHONE_REGEX = Pattern.compile("^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$");

    //中国手机号正则表达式
    private static final Pattern CHINA_PHONE_REGEX = Pattern.compile("^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[5|6])|(17[0135678])|(18[0-9])|(19[7|8|9]))\\d{8}$");

    //香港手机号
    private static final Pattern HKPHONE_REGEX = Pattern.compile("^([5689])\\d{7}$");


    /**
     * 大陆号码或香港号码或者是固定电话均可
     */
    public static boolean isPhoneLegal(@NotNull CharSequence str) {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str) || isTelephone(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * <p>
     * 移动号段：139 138 137 136 135 134 147 150 151 152 157 158 159 178 182 183 184 187 188 198
     * <p>
     * 联通号段：130 131 132 155 156 166 165 185 186 145 176
     * <p>
     * 电信号段：133 153 177 173 180 181 189 199
     * <p>
     * 虚拟运营商号段：170 171
     */
    public static boolean isChinaPhoneLegal(@NotNull CharSequence str) {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        return CHINA_PHONE_REGEX.matcher(str).matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(@NotNull CharSequence str) {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        return HKPHONE_REGEX.matcher(str).matches();
    }

    /**
     * 座机号
     */
    public static boolean isTelephone(@NotNull CharSequence str) {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        return TELEPHONE_REGEX.matcher(str).matches();
    }
}
