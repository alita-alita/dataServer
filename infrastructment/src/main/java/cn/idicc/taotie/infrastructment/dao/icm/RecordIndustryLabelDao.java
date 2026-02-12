package cn.idicc.taotie.infrastructment.dao.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryLabelMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wd
 * @description 产业链标签dao
 * @date 12/19/22 4:14 PM
 */
@Component
public class RecordIndustryLabelDao extends ServiceImpl<RecordIndustryLabelMapper, RecordIndustryLabelDO> {

    @Resource
    RecordIndustryLabelMapper recordIndustryLabelMapper;


    public List<RecordIndustryLabelDO> listByLabelIds(List<Long> labelIds) {
        return recordIndustryLabelMapper.selectList(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
                .in(RecordIndustryLabelDO::getBizId,labelIds));
    }
}
