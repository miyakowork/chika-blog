findLoginUser
===
```mysql
select * from chika_user where enable = 1 
                           and username = #username# 
                           and password = #password#
```
findByQqOpenId
===
```mysql
select * from chika_user where enable =#enable# 
                           and qq_open_id = #qqOpenId#;
```