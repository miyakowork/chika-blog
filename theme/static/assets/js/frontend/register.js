/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */

var reEmail = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;
$("form").submit(function () {
    if (isOpenRegister) {
        var email = $("input[name=_email]").val();
        var pass = $("input[name=_password]").val();
        var nickname = $("input[name=nickname]").val();
        var code = $("input[name=mailCode]").val();
        if (reEmail.test(email) && pass.length >= 6 && nickname.length > 0) {
            $.post("/registration", {
                chiKaUser: email,
                chiKaPass: md5(pass),
                mailCode: code,
                nickname: nickname
            }, function (resp) {
                layer.msg(resp.message);
                if (resp.code === ChiKa.status.ok) {
                    setTimeout(function () {
                        location.href = resp.data;
                    }, 1000)
                }
            })
        }
    }
    return false;
});

$("#sendMailCode").click(function () {
    var email = $("input[name=_email]").val();
    if (reEmail.test(email)) {
        var $sendCodeBtn = $("#sendCode");
        $sendCodeBtn.text("已发送");
        $("#sendSeconds").text("(60)");
        var $this = $("#sendMailCode");
        $this.addClass("send-code");
        $this.attr("disabled", true);
        var num = 60;
        var i = setInterval(function () {
            num--;
            var $second = $("#sendSeconds");
            $second.text("(" + num + ")");
            if (num === 0) {
                clearInterval(i);
                $sendCodeBtn.text("重新发送");
                $second.text("");
                $this.removeClass("send-code");
                $this.attr("disabled", false);
            }
        }, 1000);
        $.post("/sendMailCode", {
            email: email
        }, function (resp) {
            layer.msg(resp.message);
        })
    } else {
        layer.msg("请正确填写邮箱！");
    }

});