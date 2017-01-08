package com.kangyonggan.blog.web.controller.web;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.api.model.constants.DictionaryType;
import com.kangyonggan.api.model.dto.request.FindArticlesByTagRequest;
import com.kangyonggan.api.model.vo.Article;
import com.kangyonggan.api.model.vo.Dictionary;
import com.kangyonggan.api.service.ApiArticleService;
import com.kangyonggan.api.service.ApiDictionaryService;
import com.kangyonggan.blog.model.constants.AppConstants;
import com.kangyonggan.blog.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2016/12/22
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {


    @Resource
    private ApiArticleService apiArticleService;

    @Resource
    private ApiDictionaryService apiDictionaryService;

    /**
     * 网站模板
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String web() {
        return "web/web";
    }

    /**
     * 网站首页
     *
     * @param tag
     * @param pageNum
     * @param model
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(@RequestParam(value = "p", required = false, defaultValue = "1") int pageNum,
                        @RequestParam(value = "tag", required = false, defaultValue = "") String tag,
                        Model model) {
        List<Dictionary> tags = apiDictionaryService.findDictionariesByType(DictionaryType.ARTICLE_TAG.getType()).getList();

        FindArticlesByTagRequest request = new FindArticlesByTagRequest();
        request.setPageNum(pageNum);
        request.setPageSize(AppConstants.PAGE_SIZE);
        request.setTag(tag);

        PageInfo<Article> page = apiArticleService.findArticlesByTag(request).getPage();

        model.addAttribute("page", page);
        model.addAttribute("tags", tags);
        return getPathIndex();
    }

    @RequestMapping(value = "error/404", method = RequestMethod.GET)
    public String error404() {
        return "redirect:/#404";
    }

    @RequestMapping(value = "404", method = RequestMethod.GET)
    public String page404() {
        return "404";
    }

}
