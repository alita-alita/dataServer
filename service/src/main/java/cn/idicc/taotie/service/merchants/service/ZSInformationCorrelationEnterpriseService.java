package cn.idicc.taotie.service.merchants.service;

import cn.idicc.taotie.infrastructment.entity.data.InformationCorrelationEnterpriseDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description:
 * @version: 1.0
 */
public interface ZSInformationCorrelationEnterpriseService extends IService<InformationCorrelationEnterpriseDO> {

    /**
     * 获取指定资讯id和社会统一信用代码对应的记录
     *
     * @param informationId
     * @param unifiedSocialCreditCode
     * @return
     */
    InformationCorrelationEnterpriseDO getByInformationIdAndUnifiedSocialCreditCode(Long informationId, String unifiedSocialCreditCode);

    /**
     * 根据资讯查询关联的企业
     *
     * @param informationIds
     * @return
     */
    Map<Long, List<String>> queryByInformationIds(List<Long> informationIds);
}
