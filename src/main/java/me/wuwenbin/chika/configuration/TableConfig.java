package me.wuwenbin.chika.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * created by Wuwenbin on 2019/3/10 at 14:42
 */
@Component
@ConfigurationProperties(prefix = "table.schema")
@PropertySource(value = "classpath:table-schema.yml", encoding = "UTF-8")
@Data
public class TableConfig {

    @Value("${chika_user}")
    private String chikaUser;

    @Value("${chika_logger}")
    private String chikaLogger;

    @Value("${chika_upload}")
    private String chikaUpload;

    @Value("${chika_param}")
    private String chikaParam;

    @Value("${chika_dict}")
    private String chikaDict;

    @Value("${chika_cate}")
    private String chikaCate;

    @Value("${chika_cate_refer}")
    private String chikaCateRefer;

    @Value("${chika_tag}")
    private String chikaTag;

    @Value("${chika_tag_refer}")
    private String chikaTagRefer;

    @Value("${chika_article}")
    private String chikaArticle;

    @Value("${chika_note}")
    private String chikaNote;

    @Value("${chika_file}")
    private String chikaFile;

    @Value("${chika_project}")
    private String chikaProject;

    @Value("${chika_message}")
    private String chikaMessage;

    @Value("${chika_comment}")
    private String chikaComment;

    @Value("${chika_profile}")
    private String chikaProfile;
}
