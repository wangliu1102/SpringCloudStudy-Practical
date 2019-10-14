package com.huishu.oa.core.util;

import cn.stylefeng.roses.core.util.SpringContextHolder;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.huishu.oa.config.properties.OaProperties;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author sf
 * @Date 2019/6/13 11:22
 * @Description 上传文件工具
 **/
@Slf4j
public class FileUploadUtil {

    /**
     * @param file     MultipartFile
     * @param fileName 文件名
     * @param dir      文件上传地址
     * @return 上传成功返回true，失败false
     */
    public static boolean fileUpload(MultipartFile file, String fileName, String dir) {
        try {
            if (file.isEmpty()) {
                throw new ServiceException(BizExceptionEnum.FILE_NOT_FOUND);
            }
            //文件目录
            String dirPath =SpringContextHolder.getBean(OaProperties.class).getFileUploadPath() + dir;
            //上传文件的完整目录
            String filePath = dirPath + fileName;
            //创建目录
            File filePathDir = new File(dirPath);
            if (!(filePathDir.exists() && filePathDir.isDirectory())) {
                filePathDir.mkdirs();
            }
            //上传文件
            file.transferTo(new File(filePath));
            log.info("上传成功" + filePath);
        } catch (IOException e) {
            log.error("上传失败" + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 删除文件
     *
     * @param path 文件所在的绝对路径oaProperties.getFileUploadPath() + Const.EXPENSE_FILE +fileName
     * @return 成功true，失败false
     */
    public static boolean deleteFile(String path) {
        try {
            if (ToolUtil.isNotEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            log.error("删除失败" + e.getMessage());
            return false;
        }
        return true;
    }

}
