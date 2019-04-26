package me.wuwenbin.chika.controller.management;

import me.wuwenbin.chika.controller.BaseController;
import me.wuwenbin.chika.model.bean.LayuiTable;
import me.wuwenbin.chika.model.bean.Result;
import me.wuwenbin.chika.model.entity.CKCate;
import me.wuwenbin.chika.service.CateService;
import me.wuwenbin.data.jdbc.support.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * created by Wuwenbin on 2019/4/25 at 13:10
 */
@Controller
@RequestMapping("/management/cate")
public class AdminCateController extends BaseController {

    private final CateService cateService;

    public AdminCateController(CateService cateService) {
        this.cateService = cateService;
    }

    @ResponseBody
    @GetMapping("/list")
    public LayuiTable<CKCate> cateList(Page<CKCate> catePage) {
        catePage = cateService.findPageInfo(catePage, CKCate.class);
        return layuiTable(catePage);
    }

    @ResponseBody
    @PostMapping("/create")
    public Result createCategory(@Valid CKCate ckCate, BindingResult result) {
        if (result.hasErrors()) {
            return Result.error(formatErrorMessage(result.getFieldErrors()));
        } else {
            try {
                cateService.insertCate(ckCate);
                return Result.ok("添加新分类成功！");
            } catch (Exception e) {
                return Result.error("添加性分类失败，错误信息：" + e.getMessage());
            }
        }
    }
}