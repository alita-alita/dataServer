package cn.idicc.taotie.infrastructment.request.data;

import lombok.Data;

@Data
public class KeywordReq {

    /**
     * 关键字名称
     */
    public String keywordName;
    /**
     * 企业公众号名称
     */
    public String keywordNewsSource;
    /**
     * 关键词采集模式 (all 全量采集  incre 增量采集 )
     */
    public String keywordMode;
    /**
     * 关键词采集类型（talent_wechat=人才 capital_wechat=资产 enterprise_wechat=公众号）
     */
    public String keywordTaskCode;
    /**
     * 关键词采集状态（0=未操作  1=发送成功  2=发送失败）
     */
    public Integer keywordState;
    /**
     * 采集区分-- 1.关键词采集   2.公众号采集（极致了）
     */
    public Integer distinguish;
    //分页
    private Integer pageNum=1;
    private Integer pageSize=10;
    private Integer total;
}
