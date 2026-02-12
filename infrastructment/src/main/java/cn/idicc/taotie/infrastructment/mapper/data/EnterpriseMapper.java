package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: WangZi
 * @Date: 2022/12/24
 * @Description: 企业mapper
 * @version: 1.0
 */
@Mapper
public interface EnterpriseMapper extends BaseMapper<EnterpriseDO> {

    default EnterpriseDO selectByUnicode(String uniCode){
        return selectOne(Wrappers.lambdaQuery(EnterpriseDO.class).eq(EnterpriseDO::getUnifiedSocialCreditCode,uniCode));
    }
}
