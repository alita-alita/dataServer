package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseCorrelationLabelDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author: WangZi
 * @Date: 2022/12/27
 * @Description:
 * @version: 1.0
 */
public interface EnterpriseCorrelationLabelService extends IService<EnterpriseCorrelationLabelDO> {

    /**
     * 批量查询指定企业id关联的企业标签绑定记录，并根据企业id分组
     *
     * @param enterpriseIds
     * @return
     */
    Map<Long, List<EnterpriseCorrelationLabelDO>> mapByEnterpriseIds(List<Long> enterpriseIds);
}
