package cn.idicc.taotie.infrastructment.mapper.dw;

import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryChainIndustryLabelRelationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产业链产业标签关系表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-15
 */
public interface DwdIndustryChainIndustryLabelRelationMapper extends BaseMapper<DwdIndustryChainIndustryLabelRelationDO> {


    Integer insertOnDuplicate(@Param("dos") List<DwdIndustryChainIndustryLabelRelationDO> chainDOS);
}
