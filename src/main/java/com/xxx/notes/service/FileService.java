package com.xxx.notes.service;

import com.xxx.notes.dto.SysResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName FileService
 * @Description 文件服务
 * @Author l17561
 * @Date 2018/8/31 14:49
 * @Version V1.0
 */
public interface FileService {

    /**
     *  上传单个文件
     * @param file 上传文件
     * @return 是否成功
     */
    SysResult uploadSingleFile(MultipartFile file) throws Exception;

}
