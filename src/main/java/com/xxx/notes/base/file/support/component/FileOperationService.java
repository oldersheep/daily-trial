package com.xxx.notes.base.file.support.component;

import com.xxx.notes.base.file.entity.BasicFileInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 文件操作的基本操作
 * @Author l17561
 * @Date 2019/3/25 18:01
 * @Version V1.0
 */
public interface FileOperationService {

    /**
     * 上传单个文件
     * @param file 文件
     * @param fileInfo 文件封装信息
     * @throws Exception 异常
     */
    void uploadSingleFile(MultipartFile file, BasicFileInfo fileInfo) throws Exception;

    /**
     * 文件下载
     * @param request 根据请求判断浏览器的格式，来设置fileName
     * @param response 将内容给前端响应
     * @param fileInfo 文件的基本信息封装 主要用到 fileName fileType fileUrl
     */
    void downloadFile(HttpServletRequest request, HttpServletResponse response, BasicFileInfo fileInfo);

    /**
     * 文件预览， 目前仅支持 pdf格式的预览
     * @param fileInfo 文件的基本信息封装 主要用到 fileName fileType fileUrl
     */
    void previewFile(HttpServletRequest request, HttpServletResponse response, BasicFileInfo fileInfo);
}
