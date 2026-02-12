package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.xiaoai.AdministrativeDivisionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/1/4
 * @Description: 行政区划mapper
 * @version: 1.0
 */
@Mapper
public interface AdministrativeDivisionMapper extends BaseMapper<AdministrativeDivisionDO> {

	AdministrativeDivisionDO getByCodeAndDelete(@Param("code") String code);

	List<AdministrativeDivisionDO> listByCodeAndDelete(@Param("codeList") List<String> codeList);

	List<String> getByCodeList();

	List<AdministrativeDivisionDO> searchByCode(@Param("code") String code);

	AdministrativeDivisionDO accurateSearchByCode(@Param("code") String code);

	AdministrativeDivisionDO selectByRand(@Param("e_code") String eCode);

	AdministrativeDivisionDO selectByChildrenFirst(@Param("code") String code);
}
