package com.xxx.notes.configure;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @ClassName ValidatorConfiguration
 * @Description 校验器配置器
 * @Author Lilg
 * @Date 2019/4/13 0:20
 * @Version 1.0
 */
@Configuration
public class ValidatorConfiguration {

    @Autowired
    MessageSource messageSource;

    @Bean
    public Validator validator() {

        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(interpolator(messageSource))
                .ignoreXmlConfiguration()
                // 将fail_fast设置为true即可，如果想验证全部，则设置为false或者取消配置即可
                .failFast(true) // 等同于下面的效果
                //.addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();

         return new SpringValidatorAdapter(factory.getValidator());
    }

    @Bean
    public MessageInterpolator interpolator(MessageSource messageSource) {

        return new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
    }
}
