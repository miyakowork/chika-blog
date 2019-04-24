/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.define(['form'], function (exports) {
    var $ = layui.$,
        form = layui.form;

    var $titleInput = $("input[name=title]");
    var $urlSeqInput = $("input[name=urlSeq]");
    $titleInput.bind("input propertychange", function () {
        var title = $titleInput.val();
        if (title.length > 0) {
            $urlSeqInput.val("/" + title);
        } else {
            $urlSeqInput.val("");
        }
    });

    form.on('checkbox(customUrl)', function (data) {
        if (data.elem.checked) {
            $urlSeqInput.removeAttr("disabled");
            $urlSeqInput.val("/" + $titleInput.val());
            $titleInput.bind("input propertychange", function () {
                var title = $titleInput.val();
                if (title.length > 0) {
                    $urlSeqInput.val("/" + title);
                } else {
                    $urlSeqInput.val("");
                }
            });
        } else {
            $urlSeqInput.val("/article/#{文章ID}#");
            $urlSeqInput.attr("disabled", true);
            $titleInput.unbind("input propertychange");
        }
    });


    exports('article', {});
});