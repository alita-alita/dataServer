package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentAttractionClueFollowUpRecordDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * @Author: WangZi
 * @Date: 2023/2/24
 * @Description:
 * @version: 1.0
 */
public interface InvestmentAttractionClueFollowUpRecordService extends IService<InvestmentAttractionClueFollowUpRecordDO> {

    /**
     * 获取指定线索的历史走访人id集合
     *
     * @param clueId
     * @return
     */
    Set<Long> getHisVisitPersonIdsByClueId(Long clueId);

}
