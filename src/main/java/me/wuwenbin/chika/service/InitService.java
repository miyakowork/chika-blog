package me.wuwenbin.chika.service;

import me.wuwenbin.chika.model.bean.Result;

import java.util.Map;

/**
 * created by Wuwenbin on 2019/3/16 at 12:34
 */
public interface InitService {

    /**
     * 初始化系统设置
     *
     * @param param
     * @return
     */
    Result initSystem(Map<String, String[]> param);
}
