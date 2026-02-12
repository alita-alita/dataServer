package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentAttractionClueAssignRecordDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Author: WangZi
 * @Date: 2023/5/12
 * @Description:
 * @version: 1.0
 */
public interface InvestmentAttractionClueAssignRecordService extends IService<InvestmentAttractionClueAssignRecordDO> {

    /**
     * 获取指定线索的历史指派人id集合
     *
     * @param clueId
     * @return
     */
    Set<Long> getHisAssignPersonIds(Long clueId);


    /**
     * 获取指定线索的历史被指派人id集合
     *
     * @param clueId
     * @return
     */
    Set<Long> getHisBeAssignPersonIds(Long clueId);

    /**
     * 获取指定线索的指派记录集合
     *
     * @param clueId
     * @return
     */
    List<InvestmentAttractionClueAssignRecordDO> listByClueId(Long clueId);

}
