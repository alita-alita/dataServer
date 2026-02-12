package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.api.controller.icm.util.ChainEditableUtil;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAppEnterpriseIndustryChainSuspectedDO;
import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.request.icm.RecordAppEnterpriseIndustryChainSuspectedReq;
import cn.idicc.taotie.infrastructment.request.icm.TemplateDownload;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.service.services.icm.RecordAppEnterpriseIndustryChainSuspectedService;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Log4j2
@RestController
@RequestMapping("/record/admin/AppEnterpriseIndustryChainSuspected")
public class RecordAppEnterpriseIndustryChainSuspectedController {
	@Autowired
	private RecordAppEnterpriseIndustryChainSuspectedService recordAppEnterpriseIndustryChainSuspectedService;

	@Autowired
	private ChainEditableUtil chainEditableUtil;

	/**
	 * 根据企业名称查询疑似名录
	 *
	 * @param recordAppEnterpriseIndustryChainSuspectedReq--enterpriseName
	 * @return
	 */
	@PostMapping("/selectAll")
	@PermissionRelease
	ApiResult<PageInfo<RecordAppEnterpriseIndustryChainSuspectedDO>> selectAll(@RequestBody RecordAppEnterpriseIndustryChainSuspectedReq recordAppEnterpriseIndustryChainSuspectedReq) {
		PageInfo<RecordAppEnterpriseIndustryChainSuspectedDO> pageInfo = recordAppEnterpriseIndustryChainSuspectedService.selectAll(recordAppEnterpriseIndustryChainSuspectedReq);
		return ApiResult.success(pageInfo);
	}

	/**
	 * 单次删除疑似名录
	 *
	 * @param recordAppEnterpriseIndustryChainSuspectedDO-- bizId
	 * @return
	 */
	@PermissionRelease
	@PostMapping("/deleteRecordAppEnterpriseIndustryChainSuspected")
	public ApiResult deleteRecordAppEnterpriseIndustryChainSuspected(@RequestBody RecordAppEnterpriseIndustryChainSuspectedDO recordAppEnterpriseIndustryChainSuspectedDO) {
		if (!chainEditableUtil.allowEdit(recordAppEnterpriseIndustryChainSuspectedDO.getIndustryChainId())) {
            return ApiResult.error("400", "产业链状态已锁定，不允许编辑，如需编辑请先停止生产");
		}
		Integer deleteRecordAppEnterpriseIndustryChainSuspected = recordAppEnterpriseIndustryChainSuspectedService.deleteRecordAppEnterpriseIndustryChainSuspected(recordAppEnterpriseIndustryChainSuspectedDO);
		return deleteRecordAppEnterpriseIndustryChainSuspected > 0 ? ApiResult.success(deleteRecordAppEnterpriseIndustryChainSuspected) : ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), "删除失败");
	}


	/**
	 * 【疑似企业名录】模板下载
	 */
	@GetMapping("/templateDownload")
	@PermissionRelease
	public void templateDownload(HttpServletResponse response) {
		ExcelUtils.exportTemplate(response, "疑似企业名录", TemplateDownload.class, true);
	}


	/**
	 * 疑似企业导入名录采集
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/suspectedEnterprise")
	@PermissionRelease
	public ApiResult suspectedEnterprise(MultipartFile file, RecordAppEnterpriseIndustryChainSuspectedReq recordAppEnterpriseIndustryChainSuspectedReq) throws Exception {
		if (!chainEditableUtil.allowEdit(recordAppEnterpriseIndustryChainSuspectedReq.getIndustryChainId())) {
			return ApiResult.error("400", "产业链状态已锁定，不允许编辑，如需编辑请先停止生产");
		}
		String errorMsg = recordAppEnterpriseIndustryChainSuspectedService.suspectedEnterprise(file, recordAppEnterpriseIndustryChainSuspectedReq);
		return errorMsg == null ? ApiResult.success() : ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), errorMsg);
	}


}
