package me.wuwenbin.chika.controller.management;

import me.wuwenbin.chika.annotation.AdminMenu;
import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.management.Publish;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2019/4/23 at 22:39
 */
@Controller
@RequestMapping("/management/article")
public class ArticleController extends BaseController {

    @GetMapping("/add")
    @AdminMenu(value = "发布文章", groups = Publish.class, order = 1)
    public String publishArticle() {
        return "management/article/add";
    }
}
