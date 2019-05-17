package com.xxx.notes.base.validator;

import com.xxx.notes.base.util.RegexUtils;
import com.xxx.notes.base.validator.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

/**
 * @Description
 * @Author l17561
 * @Date 2019/5/17 17:26
 * @Version V1.0
 */
public class PhoneValidator implements ConstraintValidator<Phone, CharSequence> {

    private java.util.regex.Pattern pattern;

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {

        return (value == null) || (value.toString().trim().isEmpty()) || RegexUtils.isPhoneLegal(value);
    }
}
