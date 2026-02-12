package cn.idicc.taotie.infrastructment.entity.spider;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 关键字操作表
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class Keyword {

    /**
     * 主键id
     */
    private Long  keywordId;

    /**
     * 关键词
     */
    private String keywordName;

    /**  采集类型
     （关键词采集：
     人才招商 news_talent   资本招商 news_project   产品舆情 news_product   企业舆情 news_enterprise
     公众号采集：
     企业舆情 news_enterprise   亲缘舆情 news_qinshang   产业舆情  news_industryOF)
     */
    private String keywordTaskCode;

    /**
     * 企业名称
     */
    private String keywordSearchWord;

    /**
     * 采集平台
     */
    private String keywordPlatform;

    /**
     * 采集模式 (all  全量采集  incre 增量采集 )
     */
    private String keywordMode;

    /**
     * 公众号唯一CODE
     */
    private String keywordPublicAccountUrl;

    /**
     * 企业公众号名称
     */
    private String keywordNewsSource;

    /**
     * 产业链名称
     */
    private String keywordIndustryName;

    /**
     * 企业统一社会信用代码
     */
    private String keywordUniCode;

    /**
     * 采集状态（0=未操作  1=发送成功  2=发送失败）
     */
    private Integer keywordState;

    /**
     * 采集时间
     */
    private String keywordTime;

    /**
     * 采集周期
     */
    private Integer keywordCycle;

    /**
     * 下一次采集时间
     */
    private String keywordNextTime;

    /**
     * 采集区分-- 1.关键词采集   2.公众号采集（极致了）
     */
    private Integer distinguish;

    /**
     * 逻辑删除字段
     */
    private Boolean deleted;


}
