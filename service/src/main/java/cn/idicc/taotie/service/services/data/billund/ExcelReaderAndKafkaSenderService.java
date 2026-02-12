package cn.idicc.taotie.service.services.data.billund;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelReaderAndKafkaSenderService {


    /**
     * 人才招商-关键词 信息采集
     */
    String talentInformationCollection(MultipartFile file);

    /**
     * 资本招商-关键词 信息采集
     */
    String capitalInformationCollection(MultipartFile file);

    /**
     * 产品舆情-关键词 信息采集
     */
    String ProductInformationCollection(MultipartFile file);

    /**
     * 企业舆情-关键词 信息采集
     */
    String enterpriseInformationCollection(MultipartFile file);

    /**
     * 企业舆情-公众号 信息采集
     */
    String enterpriseOFInformationCollection(MultipartFile file);

    /**
     * 亲缘舆情-公众号 信息采集
     */
    String qinshangOFInformationCollection(MultipartFile file);

    /**
     * 产业舆情-公众号 信息采集
     */
    String industryOFInformationCollection(MultipartFile file);

    /**
     * 官网 信息采集
     */
    String OfficialWebsiteInformationCollection(MultipartFile file);

    /**
     * 【核对】
     * @param file
     * @return
     */
    String HoldInformationCollection(MultipartFile file);

    //----------------------------------------------------------------------------------

    /**
     * 人才招商信息采集  手动采集任务
     */
    String manualGatherTalent(List<Integer> keywordIds);

    /**
     * 资本招商信息采集  手动采集任务
     */
    String manualGatherCapital(List<Integer> keywordIds);

    /**
     * 产品舆情信息采集  手动采集任务
     */
    String manualGatherProducts(List<Integer> keywordIds);

    /**
     * 企业舆情信息采集  手动采集任务
     */
    String manualGatherEnterprise(List<Integer> keywordIds);

    /**
     * 企业舆情-公众号-信息采集  手动采集任务
     */
    String manualGatherEnterpriseOF(List<Integer> keywordIds);

    /**
     * 亲缘舆情-公众号-信息采集  手动采集任务
     */
    String manualGatherQinshangOF(List<Integer> keywordIds);

    /**
     * 产业舆情-公众号-信息采集  手动采集任务
     */
    String manualGatherIndustryOF(List<Integer> keywordIds);


    String manualGatherOfficialWebsiteIds(List<Long> officialWebsiteIds);


    String manualGatherHoldIds(List<Long> holdIds);
}
