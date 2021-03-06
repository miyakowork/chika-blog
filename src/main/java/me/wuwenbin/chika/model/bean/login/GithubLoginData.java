package me.wuwenbin.chika.model.bean.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/24 at 11:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubLoginData implements Serializable {

    private String callbackDomain;
    private String code;
}
