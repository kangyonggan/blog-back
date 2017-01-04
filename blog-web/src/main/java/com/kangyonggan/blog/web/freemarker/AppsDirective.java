package com.kangyonggan.blog.web.freemarker;

import com.kangyonggan.blog.biz.util.PropertiesUtil;
import com.kangyonggan.blog.web.shiro.SuperTag;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2016/12/2
 */
@Component
public class AppsDirective extends SuperTag {

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        env.setVariable("ftpUrl", ObjectWrapper.DEFAULT_WRAPPER.wrap(PropertiesUtil.getProperties("ftp.url")));
        renderBody(env, body);
    }
}
