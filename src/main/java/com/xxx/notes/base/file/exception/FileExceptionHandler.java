package com.xxx.notes.base.file.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FileExceptionHandler
 * @Description 文件异常处理器
 * @Author Lilg
 * @Date 2019/4/12 23:49
 * @Version 1.0
 */
@Slf4j
@Order(1) // 将其优先级调整其在全局异常处理器之前执行
@RestControllerAdvice
public class FileExceptionHandler {

    /**
     * 处理文件上传失败的异常，此异常发生在上传过程中，由于
     *
     * @param e 文件上传失败异常
     */
    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleFileUploadException(FileUploadException e) {
        log.error("upload file failed, because {}", e.getCause());
        return fail(e.getMessage());
    }

    @ExceptionHandler(FileNotSupportException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleFileNotSupportException(FileNotSupportException e) {
        log.error("file not support, because {}", e.getCause());
        return fail(e.getMessage());
    }

    @ExceptionHandler(FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleFileDownloadException(FileDownloadException e) {
        log.error("download file failed, because {}", e.getCause());
        return fail(e.getMessage());
    }

    @ExceptionHandler(FileOutOfSizeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleFileOutOfSizeException(FileOutOfSizeException e) {
        log.error("file size out, because {}", e.getCause());
        return fail(e.getMessage());
    }

    private Map<String, Object> fail() {

        return fail(500, "系统异常！");
    }

    private Map<String, Object> fail(String message) {

        return fail(500, message);
    }

    private Map<String, Object> fail(Integer code, String message) {

        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        return result;
    }

}
