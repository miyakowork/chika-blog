###登录/注册
+ 属性变量  

|变量名|变量含义|
|:---:|:---:|
|isOpenRegister|是否开放注册|
|isOpenForgot|是否开放忘记密码功能|
+ API  
    + QQ登录接口  
        + 请求URL：/api/qq  
        + 请求方法：GET  
        + 请求参数：无  
        + 返回结果：回调url（字符串）  
    + 普通登录接口（适合AJAX）  
        + 请求URL：/login  
        + 请求方法：POST  
        + 请求参数  
            + chiKaUser：用户账号/邮箱
            + chiKaPass：密码（md5加密后）
        + 返回格式：json  
        + 返回示例：200成功码，其余的失败；data为跳转url，根据用户角色判断跳转目标
            ```json
              {
                "code": 200,
                "data": "/management/index",
                "message": "登录成功！"
              }
            ```
        
