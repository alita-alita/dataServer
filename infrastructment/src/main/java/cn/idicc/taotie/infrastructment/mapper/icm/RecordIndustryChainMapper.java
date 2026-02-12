/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产业链Mapper
 *
 * @author wd
 * @version $Id: IndustryChainMapper.java,v 0.1 2020.10.10 auto Exp $
 */
@Mapper
public interface RecordIndustryChainMapper extends BaseMapper<RecordIndustryChainDO> {

	Long getMaxBizId();

	Integer physicsBatchDeleteByChainId(@Param("chain_id") Long chainId);

	IPage<RecordIndustryChainDTO> chainPageSearch(@Param("page") IPage<RecordIndustryChainDTO> page,
												  @Param("notes") String notes,
												  @Param("chain_name") String chainName,
												  @Param("state") Integer state,
												  @Param("chain_ids") List<Long> chainIds
	);

	default RecordIndustryChainDO selectByBizId(Long bizId) {
		return this.selectOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.eq(RecordIndustryChainDO::getDeleted, false)
				.eq(bizId != null, RecordIndustryChainDO::getBizId, bizId));
	}
}