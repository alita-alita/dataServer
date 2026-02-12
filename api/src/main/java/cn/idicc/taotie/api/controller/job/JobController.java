package cn.idicc.taotie.api.controller.job;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.provider.api.service.EnterpriseSyncRpcService;
import cn.idicc.taotie.service.job.utils.KnowledgeEnterpriseIndexUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;


@RestController
public class JobController {

	@Autowired
	private EnterpriseSyncRpcService enterpriseSyncRpcService;

	@Autowired
	private KnowledgeEnterpriseIndexUtil knowledgeEnterpriseIndexUtil;

	@TokenByPass
	@PermissionRelease
	@RequestMapping("/syncEnterpriseByChainId")
	@ResponseBody
	public APIResponse<?> updateEsEnterpriseByChainId(@RequestParam("chainId") String chainId) {
		if (StringUtils.isEmpty(chainId)) {
			return new APIResponse<>(400, "chainId不能为空");
		}
		try {
			Executors.callable(() -> {
				enterpriseSyncRpcService.updateByChain(NumberUtils.createLong(chainId));
			}).call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new APIResponse<>(200, "ok");
	}

	@TokenByPass
	@PermissionRelease
	@RequestMapping("/checkKnowledgeEnterpriseIndex")
	@ResponseBody
	public APIResponse<?> checkKnowledgeEnterpriseIndex(@RequestParam("chainId") Long chainId){
		knowledgeEnterpriseIndexUtil.compareElasticSearchAndMysqlData(chainId);
		return new APIResponse<>(200, "ok");
	}

}
