package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseAcademiaDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseAncestorDO;
import cn.idicc.taotie.infrastructment.response.xiaoai.EnterpriseCodesDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 企业学校表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2024-11-27
 */
@Mapper
public interface EnterpriseAcademiaMapper extends BaseMapper<EnterpriseAcademiaDO> {

    default List<EnterpriseAcademiaDO> selectByUniCodes(List<String> uniCodes){
        return selectList(Wrappers.lambdaQuery(EnterpriseAcademiaDO.class)
                .in(EnterpriseAcademiaDO::getUniCode,uniCodes));
    }

    IPage<EnterpriseCodesDTO> selectByPage(@Param("page")IPage<?> page);

    Integer insertBatch(@Param("dos") List<EnterpriseAcademiaDO> enterpriseAcademiaDOS);

    Integer countByCodeAndChainId(@Param("code")String code,@Param("chain_id")String chainId);

    Integer countByAll(@Param("academia_md5s")List<String> academiaMd5S,
                       @Param("chain_id")String chainId,
                       @Param("eProvince")String eProvince, @Param("eCity")String eCity, @Param("eArea")String eArea);

    IPage<EnterpriseAcademiaDO> pageByAll(@Param("page")IPage<EnterpriseAcademiaDO> page,
                                          @Param("academia_md5s")List<String> academiaMd5S,
                                          @Param("chain_id")String chainId,
                                          @Param("eProvince")String eProvince, @Param("eCity")String eCity, @Param("eArea")String eArea);

    IPage<EnterpriseAcademiaDO> pageByCode(@Param("page")IPage<EnterpriseAcademiaDO> page,
                                          @Param("code")String code,
                                          @Param("chain_id")String chainId,
                                           @Param("province")String province, @Param("city")String city, @Param("area")String area,
                                           @Param("eProvince")String eProvince, @Param("eCity")String eCity, @Param("eArea")String eArea);

    Integer physicsDeleteAll();

    Integer physicsDeleteByIds(@Param("ids") List<Long> ids);

}
