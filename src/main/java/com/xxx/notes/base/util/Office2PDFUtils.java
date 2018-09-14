package com.xxx.notes.base.util;

import com.aspose.cells.Workbook;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @ClassName Office2PDFUtil
 * @Description 转换为PDF的工具类
 * @Author l17561
 * @Date 2018/9/13 9:03
 * @Version V1.0
 */
public class Office2PDFUtils {

    private static Logger logger = LoggerFactory.getLogger(Office2PDFUtils.class);
    private static String[] TYPES = new String[]{"WORD", "EXCEL"};

    public static void main(String[] args) {
        doc2pdf("d:/DSG_web接口_修订版.docx","d:/www.pdf");
        //excel2pdf("d:/项目信息0620.xls","d:/eee.pdf");
    }

    public static void doc2pdf(String docPath, String pdfPath) {
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense(TYPES[0])) {
            return;
        }
        try {
            long old = System.currentTimeMillis();
            File file = new File(pdfPath); // 新建一个空白pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(docPath); // Address是将要被转化的word文档
            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,EPUB, XPS, SWF 相互转换
            doc.save(os, SaveFormat.PDF);

            logger.info("共耗时：" + ((System.currentTimeMillis() - old) / 1000.0) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void excel2pdf(String excelPath, String pdfPath) {
        if (!getLicense(TYPES[1])) {
            return;
        }
        try {
            long old = System.currentTimeMillis();
            Workbook wb = new Workbook(excelPath);// 原始excel路径
            FileOutputStream fileOS = new FileOutputStream(new File(pdfPath));
            wb.save(fileOS, com.aspose.cells.SaveFormat.PDF);
            fileOS.close();

            logger.info("共耗时：" + ((System.currentTimeMillis() - old) / 1000.0) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取license.xml文件，其中license.xml放在resources路径下
     *
     * @return license是否有误
     */
    private static boolean getLicense(String type) {
        boolean result = true;
        try {
            // license.xml放在resources路径下
            InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:license.xml"));
            switch (type) {
                case "WORD" :
                    com.aspose.words.License word = new com.aspose.words.License();
                    word.setLicense(is);
                    break;
                case "EXCEL" :
                    com.aspose.cells.License excel = new com.aspose.cells.License();
                    excel.setLicense(is);
                    break;
                default:
                    result = false;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

}
