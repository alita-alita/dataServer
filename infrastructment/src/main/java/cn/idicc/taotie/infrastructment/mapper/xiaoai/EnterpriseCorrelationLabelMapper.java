package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseCorrelationLabelDO;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseLabelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2022/12/24
 * @Description: 企业和企业标签关联关系
 * @version: 1.0
 */
@Mapper
public interface EnterpriseCorrelationLabelMapper extends BaseMapper<EnterpriseCorrelationLabelDO> {

	/**
	 * 通过企业id获取关联的所有企业标签名称
	 *
	 * @param enterpriseId
	 * @return
	 */
	List<EnterpriseLabelDO> getLabelsByEnterpriseId(@Param("enterpriseId") Long enterpriseId, @Param("labelTypeId") Integer labelTypeId);

}
