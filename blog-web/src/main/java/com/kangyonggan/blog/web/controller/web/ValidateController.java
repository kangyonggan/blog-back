package com.kangyonggan.blog.web.controller.web;

import com.kangyonggan.api.model.dto.reponse.CommonResponse;
import com.kangyonggan.api.model.vo.Dictionary;
import com.kangyonggan.api.service.ApiDictionaryService;
import com.kangyonggan.blog.biz.service.MenuService;
import com.kangyonggan.blog.biz.service.RoleService;
import com.kangyonggan.blog.biz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author kangyonggan
 * @since 2016/12/3
 */
@RestController
@RequestMapping("validate")
public class ValidateController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Resource
    private ApiDictionaryService apiDictionaryService;

    /**
     * 校验用户名是否可用
     *
     * @param username
     * @param oldUsername
     * @return
     */
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public boolean validateUsername(@RequestParam("username") String username,
                                    @RequestParam("oldUsername") String oldUsername) {
        if (username.equals(oldUsername)) {
            return true;
        }

        return !userService.existsUsername(username);
    }

    /**
     * 校验角色代码是否可用
     *
     * @param code
     * @param oldCode
     * @return
     */
    @RequestMapping(value = "role", method = RequestMethod.POST)
    public boolean validateRoleCode(@RequestParam("code") String code,
                                    @RequestParam(value = "oldCode", required = false, defaultValue = "") String oldCode) {
        if (code.equals(oldCode)) {
            return true;
        }

        return !roleService.existsRoleCode(code);
    }

    /**
     * 校验菜单代码是否可用
     *
     * @param code
     * @param oldCode
     * @return
     */
    @RequestMapping(value = "menu", method = RequestMethod.POST)
    public boolean validateMenuCode(@RequestParam("code") String code,
                                    @RequestParam(value = "oldCode", required = false, defaultValue = "") String oldCode) {
        if (code.equals(oldCode)) {
            return true;
        }

        return !menuService.existsMenuCode(code);
    }

    /**
     * 校验字典代码是否可用
     *
     * @param code
     * @param oldCode
     * @return
     */
    @RequestMapping(value = "dictionary", method = RequestMethod.POST)
    public boolean validateDictionaryCode(@RequestParam("code") String code,
                                          @RequestParam(value = "oldCode", required = false, defaultValue = "") String oldCode) {
        if (code.equals(oldCode)) {
            return true;
        }

        CommonResponse<Dictionary> response = apiDictionaryService.findDictionaryByCode(code);

        return response.getData() == null;
    }

}
