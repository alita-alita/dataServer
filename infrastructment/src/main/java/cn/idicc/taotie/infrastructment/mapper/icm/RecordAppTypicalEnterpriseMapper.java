package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppTypicalEnterpriseDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 典型企业表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-02-10
 */
public interface RecordAppTypicalEnterpriseMapper extends BaseMapper<RecordAppTypicalEnterpriseDO> {

    default Boolean checkExists(Long labelId,String uniCode){
        return this.selectCount(Wrappers.lambdaQuery(RecordAppTypicalEnterpriseDO.class)
                .eq(RecordAppTypicalEnterpriseDO::getIndustryLabelId,labelId)
                .eq(RecordAppTypicalEnterpriseDO::getEnterpriseUniCode,uniCode)
        )>0;
    }

    Integer physicDelete(@Param("id")Long id);

    /**
     * 根据产业链ID查询典型企业
     *
     * @param chainId
     * @return
     */
    List<RecordAppTypicalEnterpriseDO> queryByChainId(@Param("chainId") Long chainId);
}
