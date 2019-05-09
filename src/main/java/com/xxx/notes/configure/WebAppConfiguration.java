package com.xxx.notes.configure;

import com.xxx.notes.base.interceptor.AuthorizationInterceptor;
import com.xxx.notes.base.web.databind.StringToParameterEnumConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebAppConfiguration
 * @Description web的过滤器设置
 * @Author l17561
 * @Date 2018/8/13 9:41
 * @Version V1.0
 */
@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {

    // 让spring进行管理，会将依赖的类进行注入
    @Bean
    public AuthorizationInterceptor authorization() {
        return new AuthorizationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 这样会报错，因为这个类里面进行了其他的注入，如果自己new，并不能放入到sprig的容器
        // 且相关的类也不会进行注入
        // registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**");

        registry.addInterceptor(authorization()).addPathPatterns("/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToParameterEnumConverterFactory());
    }
}
