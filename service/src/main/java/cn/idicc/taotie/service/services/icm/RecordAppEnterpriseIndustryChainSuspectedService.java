package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppEnterpriseIndustryChainSuspectedDO;
import cn.idicc.taotie.infrastructment.request.icm.RecordAppEnterpriseIndustryChainSuspectedReq;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface RecordAppEnterpriseIndustryChainSuspectedService {

    /**
     * 根据企业名称查询疑似名录
     * @param recordAppEnterpriseIndustryChainSuspectedReq--enterpriseName
     * @return
     */
    PageInfo<RecordAppEnterpriseIndustryChainSuspectedDO> selectAll(RecordAppEnterpriseIndustryChainSuspectedReq recordAppEnterpriseIndustryChainSuspectedReq);

    /**
     * 批量添加疑似名录
     * @param recordAppEnterpriseIndustryChainSuspectedDOList
     * @return
     */
    Integer addBatchRecordAppEnterpriseIndustryChainSuspected(List<RecordAppEnterpriseIndustryChainSuspectedDO> recordAppEnterpriseIndustryChainSuspectedDOList);

    /**
     * 单次删除疑似名录
     * @param recordAppEnterpriseIndustryChainSuspectedDO-- bizId
     * @return
     */
    Integer deleteRecordAppEnterpriseIndustryChainSuspected(RecordAppEnterpriseIndustryChainSuspectedDO recordAppEnterpriseIndustryChainSuspectedDO);


    /**
     * 导入信息-疑似企业名录
     * @param file
     * @return
     */
    String suspectedEnterprise(MultipartFile file,RecordAppEnterpriseIndustryChainSuspectedReq recordAppEnterpriseIndustryChainSuspectedReq);

    int setStatusInitByEnterpriseIds(Set<String> enterpriseIds);

}
