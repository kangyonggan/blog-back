package com.kangyonggan.blog.web.controller.dashboard;

import com.kangyonggan.blog.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author kangyonggan
 * @since 2017/1/8
 */
@Controller
@RequestMapping("dashboard/system/user")
public class DashboardSystemUserController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("SYSTEM")
    public String list() {
        return getPathList();
    }

}
