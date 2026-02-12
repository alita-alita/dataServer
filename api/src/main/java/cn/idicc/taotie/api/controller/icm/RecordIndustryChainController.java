package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryChainQueryRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordUpdateIndustryChainRequest;
import cn.idicc.taotie.infrastructment.request.icm.SaveRecordIndustryChainRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainCategoryDTO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainDTO;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainCategoryService;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author wd
 * @Description 产业链controller
 * @date 12/19/22 9:39 AM
 */
@RestController
@RequestMapping({"/record/admin/industryChain"})
@Slf4j
public class RecordIndustryChainController {

	@Resource
	RecordIndustryChainService         recordIndustryChainService;
	@Resource
	RecordIndustryChainCategoryService recordIndustryChainCategoryService;

	@PermissionRelease
	@GetMapping("categoryList")
	public ApiResult categoryList(@RequestParam(value = "name", required = false) String name) {
		List<RecordIndustryChainCategoryDTO> list = recordIndustryChainCategoryService.getAllList(name);
		return ApiResult.success(list);
	}

	/**
	 * 产业链详情
	 *
	 * @return
	 */
	@GetMapping("/detail")
	@PermissionRelease
	public ApiResult<?> detail(@RequestParam("chainId") Long chainId) {
		Asserts.notNull(chainId, "产业链id不能为空");
		return ApiResult.success(recordIndustryChainService.getByChainId(chainId));
	}

	@PermissionRelease
	@PostMapping("/save")
	public ApiResult<Boolean> save(@Valid @RequestBody SaveRecordIndustryChainRequest saveChainRequest) {
		log.info("IndustryChainController save param is:", JSON.toJSONString(saveChainRequest));
		try {
			recordIndustryChainService.save(saveChainRequest);
		} catch (DuplicateKeyException ex) {
			throw new BizException(ErrorCode.DUPLICATE_DATA.getCode(), ErrorCode.DUPLICATE_DATA.getMessage());
		} catch (Exception ex) {
			throw new BizException(ErrorCode.SYSTEM_ERROR.getCode(), ex.getMessage());
		}
		return ApiResult.success(true);
	}

	/**
	 * 更新产业链icon和产业链名称
	 *
	 * @return
	 */
	@PostMapping("/update")
	@PermissionRelease
	public ApiResult<?> update(@Valid RecordUpdateIndustryChainRequest request) {
		recordIndustryChainService.update(request);
		return ApiResult.success(true);
	}

	@PostMapping("/page")
	@PermissionRelease
	public ApiResult<IPage<RecordIndustryChainDTO>> page(@RequestBody RecordIndustryChainQueryRequest request) {
		return ApiResult.success((recordIndustryChainService.page(request)));
	}

	/**
	 * 产业链标签列表
	 *
	 * @return
	 */
//	@PermissionRelease
//	@PostMapping("/import")
//	public ApiResult<?> importFile(MultipartFile file, @RequestParam(required = false) Long chainId) throws IOException {
//		return ApiResult.success(recordIndustryChainService.importData(file, chainId));
//	}
}
