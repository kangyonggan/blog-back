package com.kangyonggan.blog.web.controller.dashboard;

import com.kangyonggan.api.model.constants.DictionaryType;
import com.kangyonggan.api.model.constants.ResponseState;
import com.kangyonggan.api.model.dto.reponse.CommonResponse;
import com.kangyonggan.api.model.dto.request.SaveDictionaryRequest;
import com.kangyonggan.api.model.dto.request.SearchDictionariesRequest;
import com.kangyonggan.api.model.dto.request.UpdateDictionaryByCodeRequest;
import com.kangyonggan.api.model.vo.Dictionary;
import com.kangyonggan.api.service.ApiDictionaryService;
import com.kangyonggan.blog.model.constants.AppConstants;
import com.kangyonggan.blog.web.controller.BaseController;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2017/1/9
 */
@Controller
@RequestMapping("dashboard/data/dictionary")
public class DashboardDataDictionaryController extends BaseController {

    @Resource
    private ApiDictionaryService apiDictionaryService;

    /**
     * 字典列表
     *
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("DATA_DICTIONARY")
    public String list(@RequestParam(value = "p", required = false, defaultValue = "1") int pageNum,
                       @RequestParam(value = "type", required = false, defaultValue = "") String type,
                       @RequestParam(value = "value", required = false, defaultValue = "") String value,
                       Model model) {
        SearchDictionariesRequest request = new SearchDictionariesRequest();
        request.setType(type);
        request.setValue(value);
        request.setPageNum(pageNum);
        request.setPageSize(AppConstants.PAGE_SIZE);
        CommonResponse<Dictionary> response = apiDictionaryService.searchDictionsries(request);

        model.addAttribute("page", response.getPage());
        model.addAttribute("types", DictionaryType.values());
        return getPathList();
    }

    /**
     * 添加
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    @RequiresPermissions("DATA_DICTIONARY")
    public String create(Model model) {
        model.addAttribute("types", DictionaryType.values());
        model.addAttribute("dictionary", new Dictionary());
        return getPathFormModal();
    }

    /**
     * 删除/恢复
     *
     * @param code
     * @param isDeleted
     * @param model
     * @return
     */
    @RequestMapping(value = "{code:[\\w]+}/{isDeleted:\\bundelete\\b|\\bdelete\\b}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @RequiresPermissions("DATA_DICTIONARY")
    public String delete(@PathVariable("code") String code, @PathVariable("isDeleted") String isDeleted, Model model) {
        UpdateDictionaryByCodeRequest request = new UpdateDictionaryByCodeRequest();
        request.setCode(code);
        request.setIsDeleted((byte) (isDeleted.equals("delete") ? 1 : 0));
        CommonResponse<Dictionary> response = apiDictionaryService.updateDictionaryByCode(request);

        model.addAttribute("dictionary", response.getData());
        return getPathTableTr();
    }

    /**
     * 保存
     *
     * @param dictionary
     * @param result
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @RequiresPermissions("DATA_DICTIONARY")
    @ResponseBody
    public Map<String, Object> save(@ModelAttribute("dictionary") @Valid Dictionary dictionary, BindingResult result) {
        Map<String, Object> resultMap = getResultMap();

        if (!result.hasErrors()) {
            SaveDictionaryRequest request = new SaveDictionaryRequest();
            request.setCode(dictionary.getCode());
            request.setType(DictionaryType.ARTICLE_TAG.getType());
            request.setValue(dictionary.getValue());
            request.setSort(dictionary.getSort());

            CommonResponse<Dictionary> response = apiDictionaryService.saveDictionart(request);
            if (!response.getState().equals(ResponseState.Y)) {
                setResultMapFailure(resultMap, response.getErrMsg());
            }
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }

    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[\\d]+}/edit", method = RequestMethod.GET)
    @RequiresPermissions("DATA_DICTIONARY")
    public String edit(@PathVariable("id") Long id, Model model) {
        CommonResponse<Dictionary> response = apiDictionaryService.getDictionary(id);

        model.addAttribute("dictionary", response.getData());
        model.addAttribute("types", DictionaryType.values());
        return getPathFormModal();
    }

    /**
     * 更新
     *
     * @param dictionary
     * @param result
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @RequiresPermissions("DATA_DICTIONARY")
    @ResponseBody
    public Map<String, Object> update(@ModelAttribute("dictionary") @Valid Dictionary dictionary, BindingResult result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> resultMap = getResultMap();

        if (!result.hasErrors()) {
            UpdateDictionaryByCodeRequest request = new UpdateDictionaryByCodeRequest();
            PropertyUtils.copyProperties(request, dictionary);
            CommonResponse<Dictionary> response = apiDictionaryService.updateDictionaryByCode(request);
            if (!response.getState().equals(ResponseState.Y)) {
                setResultMapFailure(resultMap, response.getErrMsg());
            }
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }
}
