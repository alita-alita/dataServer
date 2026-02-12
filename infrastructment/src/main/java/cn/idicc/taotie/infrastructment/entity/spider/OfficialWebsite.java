package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 官网采集 实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class OfficialWebsite {
    /**
     * 主键
     */
    private Long officialWebsiteId;
    /**
     * 企业ID
     */
    private String officialWebsiteEnterpriseId;
    /**
     * 企业名称
     */
    private String officialWebsiteName;
    /**
     * 企业统一社会信用代码
     */
    private String officialWebsiteUniCode;
    /**
     * 企业官网地址
     */
    private String officialWebsiteUrl;
    /**
     * 采集类型
     */
    private String officialWebsiteTaskCode;
    /**
     * 采集平台（官网）
     */
    private String officialWebsitePlatform;
    /**
     * 采集状态（0=未操作  1=发送成功  2=发送失败）
     */
    private Integer officialWebsiteState;

    /**
     * 采集时间
     */
    private String officialWebsiteTime;

    /**
     * 采集周期
     */
    private Integer officialWebsiteCycle;

    /**
     * 下一次采集时间
     */
    private String officialWebsiteNextTime;

    /**
     * 删除 0:否 1是
     */
    private Boolean deleted;













}
