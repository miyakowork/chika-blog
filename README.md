## 整体变动及优化
+ 名称由「笔记博客」v4 正式更名为「千夏博客」v1.0.0
+ 简化博客系统前后台权限控制，删除冗余权限验证和管理
+ 弃用VUE的组件功能，全部改用HTML模板+layui+Thymeleaf模板引擎
+ 增加主题商店功能和后台统计功能（包含访问、文章等）
+ 增加博客首页模块（非直接进入博客列表）
+ 扩大搜索内容范围（包含文章、笔记、云文件、项目等），增加独立的搜索结果页面
+ IP地理位置查询接口改用太平洋网络的，淘宝的API会因为调用频率过高而导致500错误
+ 数据访问层由「[Spring Data JPA](https://spring.io/projects/spring-data-jpa)」改用「[BeetlSQL](http://ibeetl.com/guide/#beetlsql)」
+ 页面模板引擎由「[Thymeleaf](https://www.thymeleaf.org)」改用「[Beetl](http://ibeetl.com/guide/#beetl)」