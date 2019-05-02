package me.wuwenbin.chika.controller.management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2019/4/1 at 19:27
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class ConsoleController {

    @GetMapping("/console")
    public String index() {
        return "management/console";
    }
}
