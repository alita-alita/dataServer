package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseIndustryLabelRelationDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author wd
 * @description 企业产业链标签关系
 * @date 12/16/22 9:14 AM
 */
public interface EnterpriseIndustryLabelRelationService extends IService<EnterpriseIndustryLabelRelationDO> {

    /**
     * 批量查询指定企业id关联的产业链标签绑定记录，并根据企业id分组
     *
     * @param enterpriseIds
     * @return
     */
    Map<Long, List<EnterpriseIndustryLabelRelationDO>> mapByEnterpriseIds(List<Long> enterpriseIds);
}
