package com.kangyonggan.blog.web.controller.dashboard;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.api.model.constants.AttachmentType;
import com.kangyonggan.api.model.constants.DictionaryType;
import com.kangyonggan.api.model.dto.reponse.AttachmentResponse;
import com.kangyonggan.api.model.dto.reponse.CommonResponse;
import com.kangyonggan.api.model.dto.request.SaveArticleRequest;
import com.kangyonggan.api.model.dto.request.SearchArticlesRequest;
import com.kangyonggan.api.model.dto.request.UpdateArticleRequest;
import com.kangyonggan.api.model.dto.request.UpdateArticleWithAttachmentsRequest;
import com.kangyonggan.api.model.vo.Article;
import com.kangyonggan.api.model.vo.Attachment;
import com.kangyonggan.api.model.vo.Dictionary;
import com.kangyonggan.api.service.ApiArticleService;
import com.kangyonggan.api.service.ApiDictionaryService;
import com.kangyonggan.blog.biz.service.UserService;
import com.kangyonggan.blog.common.util.Collections3;
import com.kangyonggan.blog.common.util.MarkdownUtil;
import com.kangyonggan.blog.model.constants.AppConstants;
import com.kangyonggan.blog.model.vo.ShiroUser;
import com.kangyonggan.blog.model.vo.User;
import com.kangyonggan.blog.web.controller.BaseController;
import com.kangyonggan.blog.web.util.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2016/12/17
 */
@Controller
@RequestMapping("dashboard/user/article")
public class DashboardUserArticleController extends BaseController {

    @Resource
    private ApiArticleService apiArticleService;

    @Resource
    private ApiDictionaryService apiDictionaryService;

    @Autowired
    private UserService userService;

    /**
     * 我的文章
     *
     * @param pageNum
     * @param title
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("USER_ARTICLE")
    public String list(@RequestParam(value = "p", required = false, defaultValue = "1") int pageNum,
                       @RequestParam(value = "title", required = false, defaultValue = "") String title,
                       Model model) {
        ShiroUser user = userService.getShiroUser();
        SearchArticlesRequest request = new SearchArticlesRequest();
        request.setPageSize(AppConstants.PAGE_SIZE);
        request.setPageNum(pageNum);
        request.setCreateUsername(user.getUsername());
        request.setTitle(title);
        CommonResponse response = apiArticleService.searchArticles(request);
        PageInfo<Article> page = response.getPage();

        List<Dictionary> dictionaries = apiDictionaryService.findDictionariesByType(DictionaryType.ARTICLE_TAG.getType()).getList();

        model.addAttribute("page", page);
        model.addAttribute("dictionaries", dictionaries);
        return getPathList();
    }

    /**
     * 发布
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    @RequiresPermissions("USER_ARTICLE")
    public String create(Model model) {
        List<Dictionary> dictionaries = apiDictionaryService.findDictionariesByType(DictionaryType.ARTICLE_TAG.getType()).getList();

        model.addAttribute("article", new Article());
        model.addAttribute("dictionaries", dictionaries);
        return getPathForm();
    }

    /**
     * 保存文章
     *
     * @param attachments
     * @param article
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @RequiresPermissions("USER_ARTICLE")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(value = "attachment[]", required = false) List<MultipartFile> attachments,
                       @ModelAttribute("article") @Valid Article article, BindingResult result) throws Exception {
        Map<String, Object> resultMap = getResultMap();

        ShiroUser shiroUser = userService.getShiroUser();
        User user = userService.findUserById(shiroUser.getId());

        if (!result.hasErrors()) {
            SaveArticleRequest request = new SaveArticleRequest();
            request.setTitle(article.getTitle());
            request.setCreateUsername(user.getUsername());
            request.setCreateFullname(user.getFullname());
            request.setContent(article.getContent());
            request.setTags(article.getTags());

            if (attachments != null && !attachments.isEmpty()) {
                List<Attachment> files = uploadAttachments(attachments);
                request.setAttachments(files);
            }

            apiArticleService.saveArticleWithAttachments(request);
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }

    /**
     * 编辑文章
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[\\d]+}/edit", method = RequestMethod.GET)
    @RequiresPermissions("USER_ARTICLE")
    public String edit(@PathVariable("id") Long id, Model model) {
        CommonResponse<Dictionary> response = apiDictionaryService.findDictionariesByType(DictionaryType.ARTICLE_TAG.getType());
        List<Dictionary> dictionaries = response.getList();
        List<Dictionary> tags = apiDictionaryService.findDictionariesByArticleId(id).getList();
        AttachmentResponse<Article> attachmentResponse = apiArticleService.getArticle(id);

        model.addAttribute("article", attachmentResponse.getData());
        model.addAttribute("dictionaries", dictionaries);
        model.addAttribute("tags", Collections3.extractToList(tags, "code"));
        model.addAttribute("attachments", attachmentResponse.getAttachments());
        return getPathForm();
    }

    /**
     * 更新文章
     *
     * @param attachments
     * @param article
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @RequiresPermissions("USER_ARTICLE")
    @ResponseBody
    public Map<String, Object> update(@RequestParam(value = "attachment[]", required = false) List<MultipartFile> attachments,
                      @ModelAttribute("article") @Valid Article article, BindingResult result) throws Exception {
        Map<String, Object> resultMap = getResultMap();

        if (!result.hasErrors()) {
            UpdateArticleWithAttachmentsRequest request = new UpdateArticleWithAttachmentsRequest();
            request.setTags(article.getTags());
            request.setContent(article.getContent());
            request.setCreateFullname(article.getCreateFullname());
            request.setCreateUsername(article.getCreateUsername());
            request.setId(article.getId());
            request.setTitle(article.getTitle());

            if (!attachments.isEmpty()) {
                List<Attachment> files = uploadAttachments(attachments);
                request.setAttachments(files);
            }

            apiArticleService.updateArticleWithAttachments(request);
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }

    /**
     * 删除/恢复
     *
     * @param id
     * @param isDeleted
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[\\d]+}/{isDeleted:\\bundelete\\b|\\bdelete\\b}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @RequiresPermissions("USER_ARTICLE")
    public String delete(@PathVariable("id") Long id, @PathVariable("isDeleted") String isDeleted, Model model) {
        UpdateArticleRequest request = new UpdateArticleRequest();
        request.setId(id);
        request.setIsDeleted((byte) (isDeleted.equals("delete") ? 1 : 0));

        CommonResponse<Article> response = apiArticleService.updateArticle(request);

        model.addAttribute("article", response.getData());
        return getPathTableTr();
    }

    /**
     * 文章详情
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[\\d]+}", method = RequestMethod.GET)
    @RequiresPermissions("USER_ARTICLE")
    public String detail(@PathVariable("id") Long id, Model model) {
        AttachmentResponse<Article> response = apiArticleService.getArticle(id);

        Article article = response.getData();
        article.setContent(MarkdownUtil.markdownToHtml(article.getContent()));
        List<Attachment> attachments = response.getAttachments();

        model.addAttribute("article", article);
        model.addAttribute("attachments", attachments);
        return "web/article/detail";
    }

    /**
     * 删除附件
     *
     * @param sourceId
     * @param attachmentId
     * @return
     */
    @RequestMapping(value = "{sourceId:[\\d]+}/attachment/{attachmentId:[\\d]+}/delete", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("USER_ARTICLE")
    public Map<String, Object> deleteAttachment(@PathVariable("sourceId") Long sourceId, @PathVariable("attachmentId") Long attachmentId) {
        apiArticleService.deleteArticleAttachment(sourceId, attachmentId);
        return getResultMap();
    }

    /**
     * 上传附件
     *
     * @param attachments
     * @return
     * @throws FileUploadException
     */
    private List<Attachment> uploadAttachments(List<MultipartFile> attachments) throws FileUploadException {
        List<Attachment> files = new ArrayList();

        ShiroUser user = userService.getShiroUser();

        for (MultipartFile file : attachments) {
            if (file.isEmpty()) {
                continue;
            }

            String path = FileUpload.upload(file);

            Attachment attachment = new Attachment();
            attachment.setPath(path);
            attachment.setCreateUsername(user.getUsername());
            attachment.setName(file.getOriginalFilename());
            attachment.setType(AttachmentType.ARTICLE.getType());

            files.add(attachment);
        }
        return files;
    }
}
