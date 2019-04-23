package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * created by Wuwenbin on 2019/3/13 at 12:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CKLogger implements Serializable {

    private String id;
    private String contentType;
    private String ipAddr;
    private String ipInfo;
    private String requestMethod;
    private String sessionId;
    private String url;
    private String userAgent;
    private String username;
    private Date time;
}
