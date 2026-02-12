package cn.idicc.taotie.api.controller.sync;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.job.TalentEnterpriseDataSyncEsJob;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 同步知识库ES数据
 *
 * @author guyongliang
 * @date 2025-10-31
 * @since v4.0
 */
@Log4j2
@RestController
@RequestMapping("/sync/knowledge")
public class SyncKnowledgeController {

    @Autowired
    private TalentEnterpriseDataSyncEsJob talentEnterpriseDataSyncEsJob;

    /**
     * 同步人才企业
     */
    @PermissionRelease
    @TokenByPass
    @GetMapping("syncTalentEnterprise")
    public ApiResult<?> syncTalentEnterprise() {
        talentEnterpriseDataSyncEsJob.doSyncEs();
        return ApiResult.success(true);
    }
}
