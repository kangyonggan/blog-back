package com.kangyonggan.blog.web.controller.web;

import com.kangyonggan.api.model.constants.ResponseState;
import com.kangyonggan.api.model.dto.reponse.CommonResponse;
import com.kangyonggan.api.model.vo.Article;
import com.kangyonggan.api.service.ApiArticleService;
import com.kangyonggan.blog.common.util.MarkdownUtil;
import com.rsslibj.elements.Channel;
import com.rsslibj.elements.Item;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/24
 */
@RestController
@RequestMapping("/")
@Log4j2
public class RssController {

    private static final String BASE_URL = "http://kangyonggan.com/#article/";

    private static final String RSS_PATH = "/kyg/rss/blog.xml";
//    private static final String RSS_PATH = "/Users/kyg/rss.xml";

    @Resource
    private ApiArticleService apiArticleService;

    /**
     * 刷新rss
     *
     * @return
     */
    @RequestMapping(value = "rss/refresh", method = RequestMethod.GET)
    public String refresh() {

        Channel channel = new Channel();
        channel.setTitle("朕的博客");
        channel.setLink("http://kangyonggan.com");

        try {
            CommonResponse<Article> response = apiArticleService.findAllArticles();
            if (response.getState().equals(ResponseState.Y)) {
                List<Article> list = response.getList();
                log.info("一共{}篇文章", list.size());

                for (int i = 0; i < list.size(); i++) {
                    Article article = list.get(i);
                    Item item = new Item();
                    item.setTitle(article.getTitle());
                    item.setLink(BASE_URL + article.getId());
                    item.setDcDate(article.getUpdatedTime());
                    item.setDescription(MarkdownUtil.markdownToHtml(article.getContent()));

                    channel.addItem(i, item);
                }

                File file = new File(RSS_PATH);

                if (!file.exists()) {
                    file.createNewFile();
                }

                PrintWriter writer = new PrintWriter(new FileWriter(file));
                writer.write(channel.getFeed("rss"));
                writer.flush();
                writer.close();

                log.info("rss刷新成功");
                return "success";
            }
        } catch (Exception e) {
            log.error("查询所有文章失败", e);
        }

        return "failure";
    }

}
