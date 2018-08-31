package com.xxx.notes.service.impl;

import com.xxx.notes.base.util.FileUtils;
import com.xxx.notes.dto.SysResult;
import com.xxx.notes.entity.FileEntity;
import com.xxx.notes.mapper.FileMapper;
import com.xxx.notes.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * @ClassName FileServiceImpl
 * @Description 文件操作的服务类
 * @Author l17561
 * @Date 2018/8/31 14:49
 * @Version V1.0
 */
@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${file.path}")
    private String pathPrefix;

    @Autowired
    private FileMapper fileMapper;


    @Override
    public SysResult uploadSingleFile(MultipartFile file) throws Exception {
        // 文件原始名称
        String originalName = file.getOriginalFilename();
        // 文件后缀
        String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
        // 存储的路径
        String storePath = FileUtils.fileDestUrl() + System.currentTimeMillis() + fileSuffix;

        File path = new File(pathPrefix + FileUtils.fileDestUrl());
        if (!path.exists()) {
            path.mkdirs();
        }

        File destFile = new File(pathPrefix, storePath);

        logger.info("文件的存储名称为：{}", destFile.getName());
        file.transferTo(destFile);

        FileEntity entity = new FileEntity(originalName, fileSuffix, storePath, "", new Date());
        fileMapper.insert(entity);
        return SysResult.ok(entity);
    }



}
