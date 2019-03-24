###登录/注册
+ 包含的页面  
    + 登录：/login
    + 注册：/register
+ 包含的属性变量  

|变量名|变量含义|
|:---:|:---:|
|isOpenRegister|是否开放注册|
|isOpenForgot|是否开放忘记密码功能|
|isOpenQqLogin|是否开启QQ登录|
+ 包含的API接口
    + QQ登录接口  
        + 请求URL：/api/qq  
        + 请求方法：GET  
        + 请求参数：无  
        + 返回结果：跳转至指定URL
    + GITHUB登录接口  
        + 请求URL：/api/github  
        + 请求方法：GET  
        + 请求参数：无  
        + 返回结果：跳转至指定URL
    + 普通登录接口（AJAX）  
        + 请求URL：/login  
        + 请求方法：POST  
        + 请求参数  
            + chiKaUser：用户账号/邮箱（必填）
            + chiKaPass：密码（md5加密后，必填）
        + 返回格式：JSON  
        + 返回示例：200成功码，其余的失败；data为跳转url，根据用户角色判断跳转目标
            ```json
              {
                "code": 200,
                "data": "/management/index",
                "message": "登录成功！"
              }
            ```
    + 普通注册接口（AJAX）
        + 请求URL：/registration
        + 请求方法：POST
        + 请求参数：  
            + chiKaUser：邮箱账号（必填）
            + chiKaPass：密码（md5加密后，必填）
            + mailCode：邮箱收到的验证码（必填）
            + nickname：昵称（必填）
        + 返回格式：JSON
        + 返回示例同上
    + 发送验证码接口（AJAX）
        + 请求URL：/sendMailCode
        + 请求方法：POST
        + 请求参数：  
            + email：邮箱账号（必填）
        + 返回格式：JSON
        + 返回示例同上
    + 重置密码（AJAX）
        + 请求URL：/reset
        + 请求方法：POST
        + 请求参数：  
            + email：邮箱账号（必填）
        + 返回格式：JSON
        + 返回示例同上
    + 注解登录URL（url访问）
        + 请求URL：/token/logout
        + 请求方法：GET
        + 请求参数：  无
        + 返回结果：跳转至指定页面