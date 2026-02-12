package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 发送kafka消息字段
 */
@AllArgsConstructor
@Data
public class KeywordKafka {

        /**
         * 关键字主键id
         */
        private Integer uniKey;

        /**
         * 关键字名称
         */
        private String keyword;

        /**  采集类型
         （关键词采集：
         人才招商 news_talent   资本招商 news_project   产品舆情 news_product   企业舆情 news_enterprise
         公众号采集：
         企业舆情 news_enterprise   亲缘舆情 news_qinshang   产业舆情  news_industry)
         */
        private String taskCode;

        /**
         * 企业名称
         */
        private String searchWord;

        /**
         *关键词采平台
         */
        private String platform;

        /**
         * 关键词采集模式 (all 全量采集  incre 增量采集 )
         */
        private String collectType;

        /**
         * 公众号唯一CODE
         */
        private String publicAccountUrl;

        /**
         * 企业公众号名称
         */
        private String newsSource;

        /**
         * 产业链名称
         */
        private String searchedIndustryName;

        /**
         * 企业统一社会信用代码
         */
        private String uniCode;

        /**
         * 采集时间
         */
        private String currentTime;

        /**
         * 采集周期天数
         */
        private Integer cycle;


        public KeywordKafka() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                this.currentTime = LocalDateTime.now().format(formatter);
        }

}
