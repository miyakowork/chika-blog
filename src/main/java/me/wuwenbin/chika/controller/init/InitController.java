package me.wuwenbin.chika.controller.init;

import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2019/3/16 at 11:10
 */
@Controller
public class InitController {

    private final HttpServletRequest request;
    private final InitService initService;

    @Autowired
    public InitController(HttpServletRequest request, InitService initService) {
        this.request = request;
        this.initService = initService;
    }

    @GetMapping("/init")
    public String init() {
        return "init";
    }

    @PostMapping("/init/submit")
    public Result initSubmit() {
        return initService.initSystem(request.getParameterMap());
    }
}
