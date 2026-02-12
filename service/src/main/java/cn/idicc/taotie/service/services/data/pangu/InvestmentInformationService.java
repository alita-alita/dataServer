package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentInformationDO;
import cn.idicc.taotie.infrastructment.response.data.InvestmentInformationUploadDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 招商资讯接口层
 * @version: 1.0
 */
public interface InvestmentInformationService extends IService<InvestmentInformationDO> {

    /**
     * 批量导入
     *
     * @param list
     * @return
     */
    Set<InvestmentInformationUploadDTO> uploadBatchSave(List<InvestmentInformationUploadDTO> list, Long uploadFileRecordId);

}
