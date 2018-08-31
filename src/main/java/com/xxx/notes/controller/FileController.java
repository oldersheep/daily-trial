package com.xxx.notes.controller;

import com.xxx.notes.dto.SysResult;
import com.xxx.notes.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @ClassName FileController
 * @Description 文件操作的接口
 * @Author l17561
 * @Date 2018/8/31 14:18
 * @Version V1.0
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileService fileService;

    @PostMapping
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public SysResult upload(@RequestParam MultipartFile file) {
        try{
            logger.info("上传文件开始====");
            return fileService.uploadSingleFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(300, "上传失败");
        }
    }

}
