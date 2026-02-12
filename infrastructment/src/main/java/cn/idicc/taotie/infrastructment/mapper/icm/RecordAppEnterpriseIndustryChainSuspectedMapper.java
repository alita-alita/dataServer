package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppEnterpriseIndustryChainSuspectedDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 产业链企业疑似名录 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-02
 */
@Mapper
public interface RecordAppEnterpriseIndustryChainSuspectedMapper extends BaseMapper<RecordAppEnterpriseIndustryChainSuspectedDO> {

	/**
	 * 根据企业名称查询疑似名录
	 *
	 * @param enterpriseName
	 * @return
	 */
	List<RecordAppEnterpriseIndustryChainSuspectedDO> selectAll(@Param("enterpriseName") String enterpriseName, @Param("industryChainId") Long industryChainId);

	/**
	 * 批量添加疑似名录
	 *
	 * @param recordAppEnterpriseIndustryChainSuspectedDOList
	 * @return
	 */
	Integer addBatchRecordAppEnterpriseIndustryChainSuspected(@Param("list") List<RecordAppEnterpriseIndustryChainSuspectedDO> recordAppEnterpriseIndustryChainSuspectedDOList);

	/**
	 * 单次删除疑似名录
	 *
	 * @param bizId
	 * @return
	 */
	Integer deleteRecordAppEnterpriseIndustryChainSuspected(@Param("bizId") String bizId);


	/**
	 * 修改疑似企业是否加入黑名单
	 * 添加黑名单关键词同时-需要修改疑似企业是否加入黑名单状态
	 *
	 * @param enterpriseName  企业名称
	 * @param industryChainId 产业链id
	 * @return
	 */
	Integer updateNegative(@Param("enterpriseName") String enterpriseName,
						   @Param("industryChainId") Integer industryChainId);


	List<RecordAppEnterpriseIndustryChainSuspectedDO> selectPositive(@Param("chain_id") Long chainId
			, @Param("uni_codes") List<String> uniCodes);

	boolean isNegative(@Param("enterpriseName") String enterpriseName);

	default RecordAppEnterpriseIndustryChainSuspectedDO selectByBizId(String bizId) {
		return this.selectOne(Wrappers.lambdaQuery(RecordAppEnterpriseIndustryChainSuspectedDO.class)
				.eq(RecordAppEnterpriseIndustryChainSuspectedDO::getDeleted, false)
				.eq(RecordAppEnterpriseIndustryChainSuspectedDO::getBizId, bizId));
	}

	int setStatusInitByEnterpriseIds(@Param("enterpriseIds") Set<String> enterpriseIds);

}


