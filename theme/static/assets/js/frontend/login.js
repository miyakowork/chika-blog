/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
Auth.init({
    login_url: '/#login',
    forgot_url: '/#forgot'
});


layui.use(['jquery'], function () {
    var $ = layui.$;

    $("form").submit(function () {
        var action = $(this).attr("action");
        if (action === '/#login') {
            var $ckUser = $("input[name=chiKaUser]");
            var $ckPass = $("input[name=chiKaPass]");
            var account = $ckUser.val();
            if (account.length < 4 || account > 20) {
                layer.msg('输入正确的邮箱/账号')
            } else if (!/^[\S]{6,12}$/.test($ckPass.val())) {
                layer.msg('密码必须6到12位，且不能出现空格');
            } else {
                ChiKa.ajax("/login",
                    {
                        chiKaUser: $ckUser.val(),
                        chiKaPass: md5($ckPass.val())
                    }, function (resp) {
                        layer.msg(resp.message);
                        if (resp.code === ChiKa.status.ok) {
                            setTimeout(function () {
                                location.href = resp.data;
                            }, 1000);
                        }
                    }
                );
            }
        }
        if (action === '/#forgot') {
            var ac = $("input[name=chiKaUser]").val();
            var reEmail = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;
            if (reEmail.test(ac)) {
                $.post("/sendMailCode", {
                    email: ac
                }, function (resp) {
                    layer.msg(resp.message, function () {
                        if (resp.code === ChiKa.status.ok) {
                            layer.prompt({
                                    title: '输入您邮箱收到的验证码',
                                    formType: 3,
                                    btn: ['重置密码', '取消']
                                },
                                function (value, index, elem) {
                                    $.post('/reset', {email: ac}, function (resp) {
                                        layer.close(index);
                                        layer.msg(resp.message);
                                    })
                                });
                        }
                    });
                });
            } else {
                layer.msg("邮箱填写不正确！");
            }
        }
        return false;
    });


    $("#fromQqLogin").hover(function () {
        $("#fromQqLoginTxt").css({
            "font-weight": "bolder",
            "color": "#ff6464"
        });
    }, function () {
        $("#fromQqLoginTxt").css({
            "font-weight": "normal",
            "color": "#ff64648c"
        });
    });


    $("#fromGithubLogin").hover(function () {
        $("#fromGithubLoginTxt").css({
            "font-weight": "bolder",
            "color": "#ff6464"
        });
    }, function () {
        $("#fromGithubLoginTxt").css({
            "font-weight": "normal",
            "color": "#ff64648c"
        });
    });
});
