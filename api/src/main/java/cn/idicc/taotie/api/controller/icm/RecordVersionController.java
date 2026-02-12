package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.request.icm.ChainStartProducingDTO;
import cn.idicc.taotie.infrastructment.request.icm.RecordVersionCheckRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordVersionQueryRequest;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainService;
import cn.idicc.taotie.service.services.icm.RecordVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: MengDa
 * @Date: 2025/1/6
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping({"/record/admin/version"})
public class RecordVersionController {

	@Autowired
	private RecordVersionService recordVersionService;

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	@PermissionRelease
	@GetMapping("/chain/online")
	public ApiResult<?> chainOnline(@RequestParam Long chainId, @RequestParam Boolean isDw, @RequestParam Boolean isChain) {
		recordVersionService.chainStartOnline(chainId, isDw, isChain);
		return ApiResult.success();
	}

	@PermissionRelease
	@PostMapping("/chain/produce")
	public ApiResult<?> chainProduce(@RequestBody ChainStartProducingDTO producingDTO) {
		recordVersionService.chainStartProducing(producingDTO);
		return ApiResult.success();
	}

	@PermissionRelease
	@GetMapping("/chain/stop")
	public ApiResult<?> chainStopProduce(@RequestParam String chainId) {
		recordVersionService.chainStopProducing(chainId);
		return ApiResult.success();
	}


	@PermissionRelease
	@GetMapping("/chain/apply/edit")
	public ApiResult<?> chainProduce(@RequestParam("chainId") Long chainId) {
		return ApiResult.success(recordIndustryChainService.allowExit(chainId));
	}

	@PostMapping("/chain/produce/page")
	@PermissionRelease
	public ApiResult<?> producePage(@RequestBody RecordVersionQueryRequest request) {
		return ApiResult.success(recordVersionService.pageChainProduceRecords(request));
	}

	@GetMapping("/chain/produce/detail")
	@PermissionRelease
	public ApiResult<?> produceDetail(@RequestParam("chainId") String chainId) {
		return ApiResult.success(recordVersionService.getProducingStatus(chainId));
	}

	@PostMapping("/chain/produce/check")
	@PermissionRelease
	public ApiResult<?> produceCheck(@RequestBody RecordVersionCheckRequest request, HttpServletResponse response) {
		recordVersionService.checkExport(request, response);
		return ApiResult.success();
	}

	@PostMapping("/chain/online/page")
	@PermissionRelease
	public ApiResult<?> onlinePage(@RequestBody RecordVersionQueryRequest request) {
		return ApiResult.success(recordVersionService.pageChainOnlineRecords(request));
	}
}
