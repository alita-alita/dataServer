package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseDevelopmentIndexDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 企业成长、扩张指数 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-03-10
 */
@Mapper
public interface EnterpriseDevelopmentIndexMapper extends BaseMapper<EnterpriseDevelopmentIndexDO> {

	default List<EnterpriseDevelopmentIndexDO> selectByUniCodes(List<String> uniCodes) {
		return selectList(Wrappers.lambdaQuery(EnterpriseDevelopmentIndexDO.class)
				.in(EnterpriseDevelopmentIndexDO::getUniCode, uniCodes));
	}

	default EnterpriseDevelopmentIndexDO selectByUniCode(String uniCode) {
		return selectOne(Wrappers.lambdaQuery(EnterpriseDevelopmentIndexDO.class)
				.eq(EnterpriseDevelopmentIndexDO::getUniCode, uniCode));
	}
}
