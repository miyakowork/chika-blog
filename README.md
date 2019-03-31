>（2019年3月24日）目前正在升级开发中...
## 整体变动及优化
+ 名称由「笔记博客」v4 正式更名为「千夏博客」v1.0.0
+ 简化博客系统前后台权限控制，删除冗余权限验证和管理
+ 弃用VUE的组件功能，全部改用HTML模板+layui+Thymeleaf模板引擎
+ 增加主题切换功能和后台统计功能（包含访问、文章等）
+ 增加博客首页模块（非直接进入博客列表，为静态页面）
+ 扩大搜索内容范围（包含文章、笔记、云文件、项目等），增加独立的搜索结果页面
+ IP地理位置查询接口改用太平洋网络的，淘宝的API会因为调用频率过高而导致500错误
+ 优化初始化流程和提示信息
+ 美化了登录/注册界面，增加了忘记密码的功能（过时的浏览器[如：IE]可能支持不完美）
+ 第三方登录增加Github账号登录（之前已有QQ，后续加入更多再更新）
+ 数据访问层由「[Spring Data JPA](https://spring.io/projects/spring-data-jpa)」改用「[BeetlSQL](http://ibeetl.com/guide/#beetlsql)」
## 运行及部署
+ 本地IDE调试运行详细方法：请加群 [697053454](https://shang.qq.com/wpa/qunwpa?idkey=eb500fdb7cfadd0460d87e610b924e6a64c1456a61c4dfbbe0197f37cf27ca80)
+ 如遇到部署问题，请[issue](https://github.com/miyakowork/chika-blog/issues)，或者加入上方的QQ群咨询