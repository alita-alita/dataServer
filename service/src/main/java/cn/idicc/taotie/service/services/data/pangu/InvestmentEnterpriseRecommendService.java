package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseRecommendDO;
import cn.idicc.taotie.infrastructment.response.data.InvestmentEnterpriseRecommendUploadDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description:
 * @version: 1.0
 */
public interface InvestmentEnterpriseRecommendService extends IService<InvestmentEnterpriseRecommendDO> {


    /**
     * 同步招商推荐数据到es
     *
     * @param id
     */
    void doSyncToEs(Long id);

    /**
     * 批量导入
     *
     * @param list
     * @param uploadFileRecordId
     * @return
     */
    Set<InvestmentEnterpriseRecommendUploadDTO> uploadBatchSave(List<InvestmentEnterpriseRecommendUploadDTO> list, Long uploadFileRecordId);

}
