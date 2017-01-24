package com.kangyonggan.blog.web.util;

import com.kangyonggan.blog.biz.util.PropertiesUtil;
import com.kangyonggan.blog.model.constants.AppConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author kangyonggan
 * @since 2017/1/4
 */
@Log4j2
public class FtpUtil {

    private static String path = PropertiesUtil.getProperties("ftp.path");
    private static String ip = PropertiesUtil.getProperties("ftp.ip");
    private static int port = Integer.parseInt(PropertiesUtil.getProperties("ftp.port"));
    private static String username = PropertiesUtil.getProperties("ftp.username");
    private static String password = PropertiesUtil.getProperties("ftp.password");

    /**
     * 登录ftp服务器
     *
     * @param uploadPath
     * @return
     * @throws Exception
     */
    private static FTPClient connect(String uploadPath) throws Exception {
        FTPClient ftp = new FTPClient();
        int reply;
        ftp.connect(ip, port);
        ftp.login(username, password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return null;
        }
        if (StringUtils.isEmpty(uploadPath)) {
            ftp.changeWorkingDirectory(path);
        } else {
            ftp.changeWorkingDirectory(uploadPath);
        }
        log.info("连接文件服务器成功, 上传路径path:" + path);
        return ftp;
    }

    /**
     * 上传的文件
     *
     * @param name
     * @return 返回在文件服务器的相对路径
     */
    public static String upload(String name) {
        return upload(name, null);
    }

    /**
     * 上传的文件
     *
     * @param name
     * @param path
     * @return 返回在文件服务器的相对路径
     */
    public static String upload(String name, String path) {
        FTPClient ftp = null;
        FileInputStream in = null;
        try {
            ftp = connect(path);
            File file = new File(PropertiesUtil.getProperties(AppConstants.FILE_PATH_ROOT) + name);
            in = new FileInputStream(file);
            ftp.storeFile(file.getName(), in);

            log.info("文件上传成功,name=" + file.getName());
            return file.getName();
        } catch (Exception e) {
            log.error("文件上传异常", e);
        } finally {
            try {
                if (ftp != null) {
                    ftp.disconnect();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
        return "";
    }
}
