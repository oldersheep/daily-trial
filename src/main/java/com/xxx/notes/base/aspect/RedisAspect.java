package com.xxx.notes.base.aspect;

import com.alibaba.fastjson.JSON;
import com.xxx.notes.base.annotation.SaveRedis;
import com.xxx.notes.base.service.RedisService;
import com.xxx.notes.base.util.SpELParseUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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

            String prefix = getPrefix(proceedingJoinPoint);
            String key = getKey(proceedingJoinPoint);

            // Key值
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

    private String getPrefix(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        SaveRedis annotation = AnnotationUtils.findAnnotation(method, SaveRedis.class);

        String prefix = annotation.prefix();

        return prefix;
    }

    private String getKey(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();
        SaveRedis annotation = AnnotationUtils.findAnnotation(targetMethod, SaveRedis.class);
        String key = null;
        if (annotation != null) {
            key = annotation.key();
        }
        return SpELParseUtils.parse(target, key, targetMethod, arguments);
    }
}
