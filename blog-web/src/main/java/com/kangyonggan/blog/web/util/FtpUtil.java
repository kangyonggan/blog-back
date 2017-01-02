package com.kangyonggan.blog.web.util;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author kangyonggan
 * @since 2016/12/25
 */
@Log4j2
@Data
public class FtpUtil {

    private String path;
    private String ip;
    private int port;
    private String username;
    private String password;

    /**
     * 登录ftp服务器
     *
     * @return
     * @throws Exception
     */
    private FTPClient connect() throws Exception {
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
        ftp.changeWorkingDirectory(path);
        log.info("连接文件服务器成功, 上传路径page:" + path);
        return ftp;
    }

    /**
     * 上传的文件
     *
     * @param file
     * @return 返回在文件服务器的相对路径
     */
    public String upload(MultipartFile file) {
        FTPClient ftp = null;
        FileInputStream in = null;
        try {
            ftp = connect();
            String filePath = FileUpload.upload(file);

            File file2 = FileUpload.getAbsolutePath(filePath);
            log.info("将要上传的文件:" + file2);
            in = new FileInputStream(file2);
            ftp.storeFile(file2.getName(), in);

            log.info("文件上传成功,path=" + path);
            return filePath;
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
