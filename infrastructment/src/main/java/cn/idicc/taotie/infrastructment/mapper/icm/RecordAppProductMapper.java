package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppProductDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * <p>
 * 产品表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-09
 */
public interface RecordAppProductMapper extends BaseMapper<RecordAppProductDO> {

    default RecordAppProductDO selectByBizId(String bizId) {
        return this.selectOne(Wrappers.lambdaQuery(RecordAppProductDO.class)
                .eq(RecordAppProductDO::getDeleted, false)
                .eq(RecordAppProductDO::getBizId, bizId));
    }
}
