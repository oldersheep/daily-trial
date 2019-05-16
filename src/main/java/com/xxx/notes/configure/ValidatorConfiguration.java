package com.xxx.notes.configure;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

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

    @Bean
    public Validator validator() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("i18n/validation/message.properties");
        /*ResourceBundleLocator resourceBundleLocator = new MessageSourceResourceBundleLocator(messageSource);
        MessageInterpolator messageInterpolator = new ResourceBundleMessageInterpolator(resourceBundleLocator);
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
                .messageInterpolator(messageInterpolator).ignoreXmlConfiguration()
                .failFast(true).buildValidatorFactory();
        return new SpringValidatorAdapter(validatorFactory.getValidator());*/
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 将fail_fast设置为true即可，如果想验证全部，则设置为false或者取消配置即可
                // .failFast(true) // 等同于下面的效果
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();

        return new SpringValidatorAdapter(factory.getValidator());
        // Validator validator = factory.getValidator();
        // return validator;
    }
}
