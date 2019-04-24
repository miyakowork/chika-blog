package me.wuwenbin.chika.controller.management;

import me.wuwenbin.chika.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2019/4/23 at 22:39
 */
@Controller
@RequestMapping("/management/article")
public class ArticleController extends BaseController {

    private final HttpServletRequest request;

    @Autowired
    public ArticleController(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping("/add")
    public String publishArticle() {
        return "management/article/add";
    }
}
