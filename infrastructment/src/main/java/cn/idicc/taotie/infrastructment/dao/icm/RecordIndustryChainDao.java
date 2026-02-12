package cn.idicc.taotie.infrastructment.dao.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.enums.BooleanEnum;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainMapper;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryChainQueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wd
 * @description 产业链dao
 * @date 12/19/22 4:14 PM
 */
@Component
public class RecordIndustryChainDao extends ServiceImpl<RecordIndustryChainMapper, RecordIndustryChainDO> {

    @Resource
    RecordIndustryChainMapper recordIndustryChainMapper;

    /**
     * 根据条件查询产业链数据
     *
     * @param chainDO
     * @return
     */
    public List<RecordIndustryChainDO> queryByParams(RecordIndustryChainDO chainDO) {
        return recordIndustryChainMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
                .eq(RecordIndustryChainDO::getDeleted, BooleanEnum.NO.getCode())
                .eq(RecordIndustryChainDO::getChainCode, chainDO.getChainCode())
                .or().eq(RecordIndustryChainDO::getChainName, chainDO.getChainName()));
    }

    /**
     * 分页查询产业链数据
     *
     * @param queryDTO
     * @return
     */
    public IPage<RecordIndustryChainDO> page(RecordIndustryChainQueryRequest queryDTO) {
        return recordIndustryChainMapper.selectPage(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()),
                Wrappers.lambdaQuery(RecordIndustryChainDO.class)
                        .eq(RecordIndustryChainDO::getDeleted, BooleanEnum.NO.getCode())
                        .like(StringUtils.isNotBlank(queryDTO.getChainName()), RecordIndustryChainDO::getChainName, queryDTO.getChainName())
                        .like(StringUtils.isNotBlank(queryDTO.getNotes()), RecordIndustryChainDO::getNotes, queryDTO.getNotes())
                        .orderByDesc(RecordIndustryChainDO::getGmtModify));
    }

    /**
     * 根据id获取产业链
     *
     * @param id
     * @return
     */
    public RecordIndustryChainDO selectById(Long id) {
        return recordIndustryChainMapper.selectById(id);
    }
}
