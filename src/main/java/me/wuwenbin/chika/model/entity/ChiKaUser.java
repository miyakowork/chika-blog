package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2019/3/13 at 12:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chika_user")
public class ChiKaUser implements Serializable {

    private Long id;
    private Integer enable;
    /*
    1表示管理员，2表示访客用户
    */
    private Integer role;
    private String avatar;
    private String email;
    private String nickname;
    private String password;
    private String qqNum;
    private String qqOpenId;
    private String username;
    private String wechatOpenId;
    private LocalDateTime create;
}
