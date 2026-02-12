package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseRelationModelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/6/8
 * @Description:
 * @version: 1.0
 */
@Mapper
public interface InvestmentEnterpriseRelationModelMapper extends BaseMapper<InvestmentEnterpriseRelationModelDO> {

    void physicsDeleteByInvestmentEnterpriseIds(@Param("investmentEnterpriseIds") List<Long> investmentEnterpriseIds);

}
