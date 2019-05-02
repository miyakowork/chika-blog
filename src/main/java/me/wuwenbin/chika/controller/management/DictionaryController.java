package me.wuwenbin.chika.controller.management;

import me.wuwenbin.chika.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2019/4/25 at 14:53
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management/dictionary")
public class DictionaryController extends BaseController {

    @GetMapping
    public String dictionary() {
        return "management/dictionary/dict";
    }
}
