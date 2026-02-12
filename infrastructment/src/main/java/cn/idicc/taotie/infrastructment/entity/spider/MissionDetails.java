package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
/**
 * 爬取任务详情表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissionDetails {

    //主键
    private Long missionDetailsId;
    //序列号（外键）
    private Integer missionDetailsUninkey;
    //所属类型(
    //    关键词采集：
    //    人才招商 news_talent
    //    资本招商 news_capital
    //    产品舆情 news_product
    //    企业舆情 news_enterprise
    //    公众号采集：
    //    企业舆情 news_enterprise_off
    //    亲缘舆情 news_qinshang
    //    产业舆情  news_industry)
    //(   官网采集 and_product
    //     核对  news_publisher
    //)
    private String missionDetailsType;
    //所属模块 (采集平台)
    private String missionDetailsModule;
    //爬取开始时间
    private String missionDetailsStateTime;
    //爬取结束时间
    private String missionDetailsEndTime;
    //调取接口次数
    private Integer missionDetailsInterfaceNum;
    //应该采集总数
    private Integer missionDetailsEssayNum;
    //采集成功数量
    private Integer missionDetailsSucceedNum;
    //采集失败数量
    private Integer missionDetailsFailNum;
    //消息发送时间
    private String missionDetailsSendTime;
    //删除 0:否 1是
    private Integer deleted;

}
