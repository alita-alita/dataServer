package cn.idicc.taotie.infrastructment.mapper.dw;

import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryLabelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产业标签表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-15
 */
public interface DwdIndustryLabelMapper extends BaseMapper<DwdIndustryLabelDO> {


    Integer insertOnDuplicate(@Param("dos") List<DwdIndustryLabelDO> chainDOS);
}
