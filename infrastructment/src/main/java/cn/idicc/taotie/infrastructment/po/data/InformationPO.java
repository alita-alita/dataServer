package cn.idicc.taotie.infrastructment.po.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author: wd
 * @Date: 2022/12/24
 * @Description:资讯
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "information", createIndex = false)
public class InformationPO extends BaseSearchPO{

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 地址
     */
    private String publicAccountUrl;
    /**
     * 主题
     */
    private List<String> themes;
    /**
     * 产业id
     */
    private List<Long> industryIds;
    /**
     * 发布日期
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd")
    private LocalDate publishingDate;
    /**
     * 资讯类型，招商资讯、产业资讯、企业资讯、潜在机会
     */
    private List<Integer> infoType;
    /**
     * 统一社会信用代码
     */
    private List<String> unifiedSocialCreditCodes;

    /**
     * 来源
     */
    private String source;
    /**
     * 关注人id集合
     */
    private List<UserCollectInfo> userCollectInfo;

}
