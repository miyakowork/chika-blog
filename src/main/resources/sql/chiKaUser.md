findLoginUser
===
```mysql
select * from chika_user where enable = 1 
                           and (email = #username# or username = #username#)
                           and password = #password#
```
findByQqOpenId
===
```mysql
select * from chika_user where enable =#enable# 
                           and qq_open_id = #qqOpenId#;
```
countNickname
===
```mysql
select count(1) from chika_user where nickname=#nickname#
```