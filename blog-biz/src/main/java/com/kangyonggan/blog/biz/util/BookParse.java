package com.kangyonggan.blog.biz.util;

import com.kangyonggan.blog.model.rss.FeedMessage;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kangyonggan
 * @since 2017/1/24
 */
@Log4j2
public class BookParse {

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取章节列表
     *
     * @param url
     * @param listSelect
     * @return
     */
    public static Elements getChapterList(String url, String listSelect) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.select(listSelect);
        } catch (Exception e) {
            log.error("解析章节列表出错啦，url:" + url, e);
        }
        return null;
    }


    /**
     * 获取章节详情
     *
     * @param url
     * @param titleSelect
     * @param contentSelect
     * @return
     */
    public static FeedMessage getChapter(String url, String titleSelect, String contentSelect) {
        FeedMessage feedMessage = new FeedMessage();
        Date date = new Date();
        date.setTime(date.getTime() - 8 * 60 * 60 * 1000);
        feedMessage.setPubDate(format.format(date));
        feedMessage.setLink(url);
        feedMessage.setGuid(url);
        feedMessage.setAuthor(PropertiesUtil.getProperties("app.name"));

        try {
            Document doc = Jsoup.connect(url).get();
            Element title = doc.select(titleSelect).get(0);
            feedMessage.setTitle(title.html().trim());

            Element content = doc.select(contentSelect).get(0);
            feedMessage.setDescription(content.html());
        } catch (Exception e) {
            log.error("解析章节详情出错啦，url:" + url, e);
        }
        return feedMessage;
    }
}
