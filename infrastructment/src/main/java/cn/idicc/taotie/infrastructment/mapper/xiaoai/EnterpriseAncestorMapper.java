package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseAncestorDO;
import cn.idicc.taotie.infrastructment.response.xiaoai.EnterpriseCodesDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 企业籍贯表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2024-11-26
 */
public interface EnterpriseAncestorMapper extends BaseMapper<EnterpriseAncestorDO> {


    default List<EnterpriseAncestorDO> selectByUniCodes(List<String> uniCodes){
        return selectList(Wrappers.lambdaQuery(EnterpriseAncestorDO.class)
                .in(EnterpriseAncestorDO::getUniCode,uniCodes));
    }

    IPage<EnterpriseCodesDTO> selectByPage(@Param("page")IPage<?> page);

    Integer physicsDeleteAll();

    Integer physicsDeleteById(@Param("id")Long id);
    Integer physicsDeleteByIds(@Param("ids") List<Long> ids);

    Integer insertBatch(@Param("dos")List<EnterpriseAncestorDO> enterpriseAncestorDOS);

    Integer countByCodeAndChainId(@Param("code")String code,@Param("chain_id")String chainId);

    Integer countByAll(@Param("code")String code,
                       @Param("chain_id")String chainId,
                       @Param("province")String province, @Param("city")String city, @Param("area")String area,
                       @Param("eProvince")String eProvince, @Param("eCity")String eCity, @Param("eArea")String eArea);

    IPage<EnterpriseAncestorDO> pageByAll(@Param("page")IPage<EnterpriseAncestorDO> page,@Param("code")String code,
                                           @Param("chain_id")String chainId,
                                           @Param("province")String province, @Param("city")String city, @Param("area")String area,
                                           @Param("eProvince")String eProvince, @Param("eCity")String eCity, @Param("eArea")String eArea);
}
