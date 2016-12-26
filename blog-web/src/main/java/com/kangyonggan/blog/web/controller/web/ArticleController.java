package com.kangyonggan.blog.web.controller.web;

import com.kangyonggan.api.model.dto.reponse.AttachmentResponse;
import com.kangyonggan.api.model.vo.Article;
import com.kangyonggan.api.model.vo.Attachment;
import com.kangyonggan.api.service.ApiArticleService;
import com.kangyonggan.blog.common.util.MarkdownUtil;
import com.kangyonggan.blog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2016/12/18
 */
@Controller
@RequestMapping("article")
public class ArticleController extends BaseController {

    @Autowired
    private ApiArticleService apiArticleService;

    /**
     * 文章详情
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[\\d]+}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") Long id, Model model) {
        AttachmentResponse<Article> response = apiArticleService.findArticleById(id);

        Article article = response.getData();
        article.setContent(MarkdownUtil.markdownToHtml(article.getContent()));
        List<Attachment> attachments = response.getAttachments();

        model.addAttribute("article", article);
        model.addAttribute("attachments", attachments);
        return getPathDetail();
    }

}
