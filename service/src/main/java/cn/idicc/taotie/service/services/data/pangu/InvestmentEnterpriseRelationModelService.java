package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseRelationModelDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/6/8
 * @Description:
 * @version: 1.0
 */
public interface InvestmentEnterpriseRelationModelService extends IService<InvestmentEnterpriseRelationModelDO> {

    /**
     * 获取指定招商企业关联的招商模型推荐记录的类型集合
     *
     * @param investmentEnterpriseId
     * @return
     */
    List<Integer> listTypesByInvestmentEnterpriseId(Long investmentEnterpriseId);

    /**
     * 获取指定招商企业关联的招商模型推荐记录集合
     *
     * @param investmentEnterpriseId
     * @return
     */
    List<InvestmentEnterpriseRelationModelDO> listByInvestmentEnterpriseId(Long investmentEnterpriseId);
}
