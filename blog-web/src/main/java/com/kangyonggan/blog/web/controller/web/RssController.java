package com.kangyonggan.blog.web.controller.web;

import com.kangyonggan.api.common.util.DateUtils;
import com.kangyonggan.api.model.constants.ResponseState;
import com.kangyonggan.api.model.dto.reponse.CommonResponse;
import com.kangyonggan.api.model.vo.Article;
import com.kangyonggan.api.service.ApiArticleService;
import com.kangyonggan.blog.biz.service.impl.TYCQService;
import com.kangyonggan.blog.biz.util.FtpUtil;
import com.kangyonggan.blog.biz.util.PropertiesUtil;
import com.kangyonggan.blog.biz.util.RSSFeedWriter;
import com.kangyonggan.blog.common.util.MarkdownUtil;
import com.kangyonggan.blog.model.constants.AppConstants;
import com.kangyonggan.blog.model.rss.Feed;
import com.kangyonggan.blog.model.rss.FeedMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/24
 */
@RestController
@RequestMapping("/rss")
@Log4j2
public class RssController {

    private static final String BASE_URL = "http://kangyonggan.com/#article/";
    private static final String RSS_NAME = "blog.xml";

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private ApiArticleService apiArticleService;

    @Autowired
    private TYCQService tycqService;

    /**
     * 刷新blog
     *
     * @return
     */
    @RequestMapping(value = "blog", method = RequestMethod.GET)
    public String refreshBlog() {

        Feed feed = new Feed();
        feed.setDescription("记录生活、工作和学习时的笔记心得等");
        feed.setLink("http://kangyonggan.com");
        feed.setTitle("朕的博客");
        List<FeedMessage> feedMessages = feed.getFeedMessages();

        try {
            CommonResponse<Article> response = apiArticleService.findAllArticles();
            if (response.getState().equals(ResponseState.Y)) {
                List<Article> list = response.getList();
                log.info("一共{}篇文章", list.size());

                for (int i = 0; i < list.size(); i++) {
                    Article article = list.get(i);
                    FeedMessage feedMessage = new FeedMessage();

                    feedMessage.setTitle(article.getTitle());
                    feedMessage.setLink(BASE_URL + article.getId());
                    feedMessage.setDescription(MarkdownUtil.markdownToHtml(article.getContent()));
                    Date date = article.getUpdatedTime();
                    date.setTime(date.getTime() - 8 * 60 * 60 * 1000);
                    feedMessage.setPubDate(format.format(date));
                    feedMessages.add(feedMessage);
                }

                File file = new File(PropertiesUtil.getProperties(AppConstants.FILE_PATH_ROOT) + RSS_NAME);

                if (!file.exists()) {
                    file.createNewFile();
                }

                new RSSFeedWriter(feed, file.getPath()).write();

                FtpUtil.upload(RSS_NAME, "rss/");

                log.info("rss刷新成功");
                return "success";
            }
        } catch (Exception e) {
            log.error("查询所有文章失败", e);
        }

        return "failure";
    }

    /**
     *
     * 刷新天域苍穹
     *
     * @return
     */
    @RequestMapping(value = "tycq", method = RequestMethod.GET)
    public String refreshTYCQ() {
        return tycqService.refresh();
    }

}
