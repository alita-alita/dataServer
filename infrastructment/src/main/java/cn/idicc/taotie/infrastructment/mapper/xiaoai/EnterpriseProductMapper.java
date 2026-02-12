package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseProductDO;
import cn.idicc.taotie.infrastructment.po.data.EnterpriseProductPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2022/12/24
 * @Description: 企业mapper
 * @version: 1.0
 */
@Mapper
public interface EnterpriseProductMapper extends BaseMapper<EnterpriseProductDO> {


	Page<EnterpriseProductPO> getProductByChainIdOrUniCode(@Param("page") Page<?> page, @Param("chainId") Long chainId, @Param("enterpriseUniCode") String enterpriseUniCode);

	/**
	 * 根据统一社会信用代码查询产品列表
	 *
	 * @param uniCodes
	 * @return
	 */
	List<EnterpriseProductDO> selectProductNameByUniCode(@Param("uniCodes") List<String> uniCodes);
}
