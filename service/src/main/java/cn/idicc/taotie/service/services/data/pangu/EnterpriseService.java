package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import cn.idicc.taotie.infrastructment.response.data.EnterpriseDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2022/12/24
 * @Description: 企业接口层
 * @version: 1.0
 */
public interface EnterpriseService extends IService<EnterpriseDO> {

    /**
     * 获取指定社会统一信用代码的企业信息
     *
     * @param unifiedSocialCreditCode
     * @return
     */
    EnterpriseDTO getByUnifiedSocialCreditCode(String unifiedSocialCreditCode);


    void doSyncDataToEs(String uniCode);

    void doSyncDataToEs(Long enterpriseId);
    void doSyncDataToEs(EnterpriseDO enterpriseDO);
    /**
     * 执行同步企业数据到es中操作
     *
     * @param enterprises
     */
    void doSyncDataToEs(List<EnterpriseDO> enterprises);

    /**
     * 根据企业名称批量查询企业信息
     *
     * @param enterpriseNames
     * @return
     */
    List<EnterpriseDO> queryByNames(List<String> enterpriseNames);

    /**
     * 获取一条指定名称的企业信息
     *
     * @param enterpriseName
     * @return
     */
    EnterpriseDTO getByName(String enterpriseName);

    /**
     * 获取指定名称的企业信息集合
     *
     * @param enterpriseName
     * @return
     */
    List<EnterpriseDTO> listByName(String enterpriseName);
}
