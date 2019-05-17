package com.xxx.notes.base.exception;

import com.xxx.notes.base.dto.BaseResponse;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理器
 * @Author Lilg
 * @Date 2019/4/13 0:21
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @RequestBody走这里
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> validationErrorHandler(MethodArgumentNotValidException ex) {
        // 同样是获取BindingResult对象，然后获取其中的错误信息
        // 如果前面开启了fail_fast，事实上这里只会有一个信息
        //如果没有，则可能又多个
        List<String> errorInformation = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return BaseResponse.build(402, errorInformation.toString());
    }

    // @Param走这里
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<?> validationErrorHandler(ConstraintViolationException ex) {
        List<String> errorInformation = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return BaseResponse.build(401, errorInformation.toString());
    }
}
