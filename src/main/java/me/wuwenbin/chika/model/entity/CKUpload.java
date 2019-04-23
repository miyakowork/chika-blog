package me.wuwenbin.chika.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * created by Wuwenbin on 2019/3/13 at 12:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CKUpload implements Serializable {

    private Long id;
    private String diskPath;
    private String type;
    private String virtualPath;
    private Date upload;
}
