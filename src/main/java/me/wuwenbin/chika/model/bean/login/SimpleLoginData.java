package me.wuwenbin.chika.model.bean.login;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/3/19 at 15:32
 */
@Data
public class SimpleLoginData implements Serializable {

    private String chiKaUser;
    private String chiKaPass;
    private HttpServletRequest request;

}
