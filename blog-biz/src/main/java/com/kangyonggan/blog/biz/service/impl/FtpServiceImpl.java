package com.kangyonggan.blog.biz.service.impl;

import com.kangyonggan.blog.biz.service.FtpService;
import com.kangyonggan.blog.biz.util.PropertiesUtil;
import com.kangyonggan.blog.model.constants.AppConstants;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;

/**
 * @author kangyonggan
 * @since 2017/1/4
 */
@Data
@Log4j2
public class FtpServiceImpl implements FtpService {

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
        log.info("连接文件服务器成功, 上传路径path:" + path);
        return ftp;
    }

    /**
     * 上传的文件
     *
     * @param name
     * @return 返回在文件服务器的相对路径
     */
    public String upload(String name) {
        FTPClient ftp = null;
        FileInputStream in = null;
        try {
            ftp = connect();
            in = new FileInputStream(PropertiesUtil.getProperties(AppConstants.FILE_PATH_ROOT) + name);
            ftp.storeFile(name, in);

            log.info("文件上传成功,name=" + name);
            return name;
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
