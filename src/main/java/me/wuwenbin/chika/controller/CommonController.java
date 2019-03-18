package me.wuwenbin.chika.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * created by Wuwenbin on 2019/3/16 at 18:25
 */
@Controller
public class CommonController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
