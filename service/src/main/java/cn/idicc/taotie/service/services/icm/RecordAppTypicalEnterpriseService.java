package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterpriseAddRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterprisePageRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterpriseUpdateRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordAppTypicalEnterpriseDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author: MengDa
 * @Date: 2025/2/10
 * @Description:
 * @version: 1.0
 */
public interface RecordAppTypicalEnterpriseService {

    String addTypicalEnterprise(RecordTypicalEnterpriseAddRequest request);
    Boolean updateTypicalEnterprise(RecordTypicalEnterpriseUpdateRequest request);
    Boolean deleteById(Long id);
    Boolean addAllToSuspected(String chainId);
    IPage<RecordAppTypicalEnterpriseDTO> pageSearch(RecordTypicalEnterprisePageRequest request);
}
