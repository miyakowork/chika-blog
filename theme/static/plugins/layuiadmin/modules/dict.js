/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.define(['jquery', 'form', 'table'], function (exports) {
    var $ = layui.$,
        form = layui.form,
        table = layui.table;

    var cateTable = table.render({
        elem: '#cate-table'
        , url: '/management/cate/list'
        , request: {
            pageName: 'pageNo'
            , limitName: 'pageSize'
        }
        , page: true
        , limit: 10
        , height: 'full'
        , cols: [[
            {field: 'id', title: 'ID', width: 50, sort: true}
            , {field: 'name', title: '标识', edit: 'text', sort: true}
            , {
                field: 'fontIcon', title: '图标', edit: 'text', align: 'center',
                templet: function (d) {
                    return '<i class="' + d.fontIcon + '"></i>';
                }
            }
            , {
                field: 'cn_name', title: '名称', edit: 'text', sort: true, templet: function (d) {
                    return d.cnName;
                }
            }
            , {
                field: 'order_index', title: '排序', edit: 'text', sort: true, templet: function (d) {
                    return d.orderIndex;
                }
            }
            , {field: 'type', title: '种类', sort: true}
            , {
                fixed: 'right',
                title: '操作',
                align: 'center',
                toolbar: '<div><a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="del">删除</a></div>'
            }
        ]]
    });


//监听排序事件
    table.on('sort(cate)', function (obj) {
        table.reload('cate-table', {
            initSort: obj
            , where: {
                orderField: obj.field //排序字段
                , orderDirection: obj.type //排序方式
            }
        });
    });


    //监听提交
    form.on('submit(cate-submit)', function (data) {
        $.post(
            "/management/cate/create",
            data.field,
            function (resp) {
                layer.msg(resp.message)
                if (resp.code === 200) {
                    cateTable.reload({
                        page: {curr: 1}
                    })
                }
            }
        );
        return false;
    });

    exports('dict', {});
});