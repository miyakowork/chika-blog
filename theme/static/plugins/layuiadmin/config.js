/**

 @Name：layuiAdmin iframe版全局配置
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License：LPPL（layui付费产品协议）

 */

layui.define(['laytpl', 'layer', 'element', 'util'], function (exports) {
    exports('setter', {
        container: 'LAY_app' //容器ID
        , base: layui.cache.base //记录静态资源所在路径
        , views: layui.cache.base + 'tpl/' //动态模板所在目录
        , entry: 'index' //默认视图文件名
        , engine: '.html' //视图文件后缀名
        , pageTabs: true //是否开启页面选项卡功能。iframe版推荐开启

        , name: 'layuiAdmin'
        , tableName: 'layuiAdmin' //本地存储表名
        , MOD_NAME: 'admin' //模块事件名

        , debug: true //是否开启调试模式。如开启，接口异常时会抛出异常 URL 等信息

        //自定义请求字段
        , request: {
            tokenName: false //自动携带 token 的字段名（如：access_token）。可设置 false 不携带。
        }

        //自定义响应字段
        , response: {
            statusName: 'code' //数据状态的字段名称
            , statusCode: {
                ok: 200 //数据状态一切正常的状态码
                , logout: -1 //登录状态失效的状态码
            }
            , msgName: 'msg' //状态信息的字段名称
            , dataName: 'data' //数据详情的字段名称
        }

        //扩展的第三方模块
        , extend: [
            'echarts', //echarts 核心包
            'echartsTheme' //echarts 主题
        ]

    });
});
