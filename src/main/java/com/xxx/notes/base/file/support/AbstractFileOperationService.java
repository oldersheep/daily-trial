package com.xxx.notes.base.file.support;

import com.xxx.notes.base.constant.HttpHeaders;
import com.xxx.notes.base.exception.GlobalException;
import com.xxx.notes.base.file.entity.BasicFileInfo;
import com.xxx.notes.base.file.exception.*;
import com.xxx.notes.base.file.support.component.FileOperationService;
import com.xxx.notes.base.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description 针对文件操作的抽象类，封装通用操作
 * @Author l17561
 * @Date 2019/3/25 18:02
 * @Version V1.0
 */
public abstract class AbstractFileOperationService<T extends BasicFileInfo> implements FileOperationService {

    private final static Logger log = LoggerFactory.getLogger(AbstractFileOperationService.class);

    // 单位：MB ( 1 << 20) = (1024 * 1024)
    private static final int MB_SHIFT = 20;

    // 上传的文件大小限制 (0-不做限制) ，单位：字节
    public abstract int getMaxSize();

    // 允许上传的文件的后缀(包含.)，若有多个，则用逗号分隔
    public abstract String getSuffix();

    // 根路径，由用户自定义指定
    public abstract String getRootPath();

    public String getCustomPath() {
        // 自定义路径，格式为yyyy/MM/dd/HH
        String pattern = "yyyy" + File.separator + "MM" + File.separator + "dd" + File.separator + "HH";
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    /**
     * 针对file进行检查
     *
     * @param file
     */
    public void checkFile(MultipartFile file) {
        if (file == null) {
            throw new FileIsEmptyException();
        }
        int maxSize = getMaxSize();
        if (maxSize > 0 && file.getSize() > (maxSize << MB_SHIFT)) {
            throw new FileOutOfSizeException("请上传小于" + maxSize + "MB的文件");
        }
        List<String> typeList = Arrays.asList(getSuffix().toLowerCase().split(","));
        String fileName = file.getOriginalFilename();
        int lastPointIndex = fileName.lastIndexOf(".");
        if (lastPointIndex == -1) {
            throw new FileNotSupportException("未检测到文件的格式，有潜在威胁！");
        }
        String fileType = fileName.substring(lastPointIndex + 1).toLowerCase();
        if (!typeList.contains(fileType)) {
            throw new FileNotSupportException("暂不支持上传" + fileType + "类型的文件");
        }
    }

    /**
     * 上传文件 -- 步骤为：1、检验文件是否合法 2、上传文件 3、持久化到数据库
     *
     * @param file     文件
     * @param fileInfo 文件封装信息
     * @throws Exception
     */
    @Override
    public void uploadSingleFile(MultipartFile file, BasicFileInfo fileInfo) throws Exception {
        checkFile(file);

        String fileId = UUIDUtils.absNumUUID();
        String fileName = file.getOriginalFilename();
        int lastPointIndex = fileName.lastIndexOf(".");
        String fileType = fileName.substring(lastPointIndex);
        fileName = fileName.substring(0, lastPointIndex);


        //创建路径文件，执行上传
        String fileUrl = makePath(getRootPath(), getCustomPath()) + fileId + fileType;

        file.transferTo(new File(fileUrl));

        fileInfo.setFileId(fileId);
        fileInfo.setFileName(fileName);
        fileInfo.setFileType(fileType);
        fileInfo.setFileUrl(fileUrl);
        // 持久化到数据库
        insertFileInfo((T)fileInfo);
    }


    /**
     * 文件下载
     *
     * @param request  根据请求判断浏览器的格式，来设置fileName
     * @param response 将内容给前端响应
     * @param fileInfo 文件的基本信息封装 主要用到 fileName fileType fileUrl
     */
    @Override
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response,
                             BasicFileInfo fileInfo) {

        File file = new File(fileInfo.getFileUrl());
        String fileName = fileInfo.getFileName();
        String fileType = fileInfo.getFileType();
        try (InputStream inStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {

            //根据不同浏览器判断不同的方式
            String header = request.getHeader(HttpHeaders.USER_AGENT).toUpperCase();

            if (HttpHeaders.UserAgent.isFirefox(header)) {
                //火狐也包含RV
                fileName = new String((fileName + fileType).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            } else if (HttpHeaders.UserAgent.isIE(header)) {
                // IE浏览器
                fileName = java.net.URLEncoder.encode((fileName + fileType), StandardCharsets.UTF_8.name());
            } else {
                // 谷歌、火狐等
                fileName = new String((fileName + fileType).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }

            //设置文件下载头
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
            response.addHeader(HttpHeaders.CONTENT_LENGTH, "" + file.length());
            //设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");

            int len;
            byte[] buff = new byte[4096];

            while ((len = inStream.read(buff)) != -1) {
                out.write(buff, 0, len);
            }

            while ((len = inStream.read()) != -1) {
                out.write(len);
            }
        } catch (Exception e) {
            log.error("下载时发生异常", e);
            throw new FileDownloadException(e.getMessage());
        }
    }

    /**
     * 文件预览， 目前仅支持 pdf格式的预览
     *
     * @param request
     * @param response
     * @param fileInfo 文件的基本信息封装 主要用到 fileName fileType fileUrl
     */
    @Override
    public void previewFile(HttpServletRequest request,
                            HttpServletResponse response,
                            BasicFileInfo fileInfo) throws FileNotSupportException {
        String fileType = fileInfo.getFileType();
        if (".pdf".equalsIgnoreCase(fileType)) {
            downloadFile(request, response, fileInfo);
        } else {
            throw new FileNotSupportException("目前仅支持PDF格式的预览，暂不支持" + fileType + "的预览！");
        }
    }

    /**
     * 将文件信息持久化到数据库
     *
     * @param fileInfo 文件信息，对应到数据库中表
     * @return 影响行数
     * @throws FileInfoPersistentException
     */
    protected abstract int insertFileInfo(T fileInfo) throws FileInfoPersistentException;

    private String makePath(String rootPath, String customPath) throws IOException {
        StringBuilder sb = new StringBuilder(rootPath);
        if (!rootPath.endsWith(File.separator) && !customPath.startsWith(File.separator)) {
            sb.append(File.separator);
        }
        sb.append(customPath);
        if (!customPath.endsWith(File.separator)) {
            sb.append(File.separator);
        }
        String dir = sb.toString();
        File descDir = new File(dir);
        if (!descDir.exists()) {
            if (!descDir.mkdirs()) {
                throw new IOException(String.format("文件夹[%s]创建失败", dir));
            }
        }

        return dir;
    }
}
