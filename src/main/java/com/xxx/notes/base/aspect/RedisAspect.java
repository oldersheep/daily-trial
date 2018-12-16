package com.xxx.notes.base.aspect;

import com.alibaba.fastjson.JSON;
import com.xxx.notes.base.annotation.Key;
import com.xxx.notes.base.annotation.SaveRedis;
import com.xxx.notes.base.service.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @ClassName RedisAspect
 * @Description Redis的切面通知
 * @Author l17561
 * @Date 2018/7/20 8:59
 * @Version V1.0
 */
@Aspect
@Component
public class RedisAspect {

    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.xxx.notes.base.annotation.SaveRedis)")
    public void saveRedis(){}

    @Around(value = "saveRedis()")
    private Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint){
        try {
            // 前置通知，获取key
            Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
            SaveRedis saveRedis = method.getAnnotation(SaveRedis.class);
            String prefix = saveRedis.prefix();
            String key = "";
            Parameter[] parameters = method.getParameters();
            Object[] args = proceedingJoinPoint.getArgs();
            for (int i = 0; i< parameters.length; i++){
                if (parameters[i].getAnnotation(Key.class) != null) {
                    key = key + "_" + args[i].toString();
                }
            }

            key = prefix + key;

            // 获取返回值
            Object result = proceedingJoinPoint.proceed();

            // 后置通知，进行存储
            if (result instanceof String) {
                redisService.set(key, result.toString());
            } else {
                redisService.set(key, JSON.toJSONString(result));
            }
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }


    //    /**
//     *  注解的值无法进行动态赋值，所以这个办法不太对
//     */
//    @AfterReturning(pointcut = "saveRedis()")
//    private void afterReturning(JoinPoint joinPoint){
//        /*获取注解的name和value*/
//        String prefix="GG_";
//        SaveRedis saveRedis =((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(SaveRedis.class);
//        String key = prefix + saveRedis.name();
//        String value = saveRedis.value();
//        redisService.set(key, value);
//    }
}
