package com.kangyonggan.blog.biz.service.impl;

import com.kangyonggan.blog.biz.util.BookParse;
import com.kangyonggan.blog.biz.util.FtpUtil;
import com.kangyonggan.blog.biz.util.PropertiesUtil;
import com.kangyonggan.blog.biz.util.RSSFeedWriter;
import com.kangyonggan.blog.model.constants.AppConstants;
import com.kangyonggan.blog.model.rss.Feed;
import com.kangyonggan.blog.model.rss.FeedMessage;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/24
 */
@Service
@Log4j2
public class TYCQService {

    private static final String BASE_URL = "http://www.biquge.com";

    public String refresh() {
        Elements list = BookParse.getChapterList(BASE_URL + "/3_3835/", "#list dl dd a");

        if (list == null) {
            return "failure";
        }

        Feed feed = new Feed();
        Date date = new Date();
        date.setTime(date.getTime() - 8 * 60 * 60 * 1000);
        feed.setPubDate(BookParse.format.format(date));
        feed.setLink(BASE_URL);
        feed.setTitle("天域苍穹");
        feed.setDescription("笑尽天下英雄，宇内我为君主！万水千山，以我为尊；八荒六合，唯我称雄！我欲舞风云，凌天下，踏天域，登苍穹！谁可争锋？！诸君可愿陪我，并肩凌天下，琼霄风云舞，征战这天域苍穹？");

        List<FeedMessage> feedMessages = feed.getFeedMessages();

        for (int i = 10; i < list.size(); i++) {
            Element chapter = list.get(i);
            log.info("正在解析:{}", chapter);

            FeedMessage feedMessage = BookParse.getChapter(BASE_URL + chapter.attr("href"), ".bookname h1", "#content");
            feedMessages.add(feedMessage);
        }

        try {
            File file = new File(PropertiesUtil.getProperties(AppConstants.FILE_PATH_ROOT) + "tycq.xml");

            if (!file.exists()) {
                file.createNewFile();
            }

            new RSSFeedWriter(feed, file.getPath()).write();

            FtpUtil.upload("tycq.xml", "rss/");

            log.info("刷新天域苍穹成功");
        } catch (Exception e) {
            log.error("刷新天域苍穹出错啦", e);
        }
        return "success";
    }
}
