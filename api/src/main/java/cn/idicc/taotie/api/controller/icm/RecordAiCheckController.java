package cn.idicc.taotie.api.controller.icm;

import cn.idicc.common.exception.BusinessException;
import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAiCheckRecordDO;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckRecordStateEnum;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckStateEnum;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAiCheckRecordMapper;
import cn.idicc.taotie.infrastructment.request.icm.RecordAiCheckRecordQueryRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesAiCheckQueryRequest;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.job.AiCheckJobs;
import cn.idicc.taotie.service.services.icm.RecordAiCheckRecordService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesAiCheckService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping({"/record/admin/ai/check"})
public class RecordAiCheckController {

	@Autowired
	private RecordAiCheckRecordService aiCheckRecordService;

	@Autowired
	private RecordProductMatchesAiCheckService productMatchesAiCheckService;

	@Autowired
	private RecordAiCheckRecordMapper recordAiCheckRecordMapper;

	@Autowired
	private AiCheckJobs aiCheckJobs;

	@PermissionRelease
	@PostMapping("/page")
	public ApiResult<?> page(@RequestBody RecordAiCheckRecordQueryRequest request) {
		return ApiResult.success(aiCheckRecordService.pageList(request));
	}

	@PermissionRelease
	@PostMapping("/record/page")
	public ApiResult<?> recordPage(@RequestBody RecordProductMatchesAiCheckQueryRequest request) {
		return ApiResult.success(productMatchesAiCheckService.pageList(request));
	}

	@PermissionRelease
	@PostMapping("/record/export")
	public ApiResult<?> recordExport(@RequestBody RecordProductMatchesAiCheckQueryRequest request, HttpServletResponse response) {
		productMatchesAiCheckService.exportFileList(request, response);
		return ApiResult.success();
	}


	@PermissionRelease
	@GetMapping("/record/update")
	public ApiResult<?> recordPage(@RequestParam Long id, @RequestParam Integer status) {
		RecordAiCheckRecordStateEnum stateEnum = RecordAiCheckRecordStateEnum.getByCode(status);
		if (stateEnum != null) {
			productMatchesAiCheckService.updateRecordState(id, stateEnum);
		}
		return ApiResult.success();
	}

	@PermissionRelease
	@GetMapping("/record/update/all")
	public ApiResult<?> finishAiCheck(@RequestParam Long chainId,
								   @RequestParam(required = false) Integer oldStatus,
								   @RequestParam(required = false) Integer newStatus) {
		RecordAiCheckRecordStateEnum oldStatusEnum = RecordAiCheckRecordStateEnum.NOT_PASS;
		RecordAiCheckRecordStateEnum newStatusEnum = RecordAiCheckRecordStateEnum.NOT_PASS_READY;
		if (oldStatus != null) {
			oldStatusEnum = RecordAiCheckRecordStateEnum.getByCode(oldStatus);
		}
		if (newStatus != null) {
			newStatusEnum = RecordAiCheckRecordStateEnum.getByCode(newStatus);
		}
		productMatchesAiCheckService.updateAllRecordState(chainId, oldStatusEnum, newStatusEnum);
		return ApiResult.success();
	}


	@PermissionRelease
	@GetMapping("/add")
	public ApiResult<?> add(@RequestParam Long chainId) {
		long exists = recordAiCheckRecordMapper.selectCount(Wrappers.lambdaQuery(RecordAiCheckRecordDO.class)
				.eq(RecordAiCheckRecordDO::getChainId, chainId)
				.notIn(RecordAiCheckRecordDO::getStatus, RecordAiCheckStateEnum.COMPLETE.getCode()));
		if(exists > 0){
			return ApiResult.error("400", "已存在未完成的质检任务单");
		}

		return ApiResult.success(aiCheckRecordService.addRecordAiCheckRecord(chainId, null));
	}

	@PermissionRelease
	@GetMapping("/run")
	public ApiResult<?> run(@RequestParam Long id) {
		aiCheckRecordService.runRecord(id);
		return ApiResult.success();
	}

	@PermissionRelease
	@GetMapping("/reRunFail")
	public ApiResult<?> reRunFail(@RequestParam long chainId) {
		productMatchesAiCheckService.sendAllFailKafkaByVersion(chainId);
		return ApiResult.success();
	}

	@PermissionRelease
	@GetMapping("/stop")
	public ApiResult<?> stop(@RequestParam Long id) {
		aiCheckRecordService.stopRecord(id);
		return ApiResult.success();
	}

	@PermissionRelease
	@GetMapping("/checkStatus")
	public ApiResult<?> checkStatus() {
		aiCheckJobs.checkAiCheckRecordStatus();
		return ApiResult.success();
	}


//	@PermissionRelease
//	@GetMapping("/export")
//	public ApiResult<?> aiCheckDataOnline(@RequestParam Long id, @RequestParam(required = false) Boolean isCheck) {
//		if (isCheck == null) {
//			isCheck = true;
//		}
//		aiCheckRecordService.aiCheckDataOnline(id, isCheck);
//		return ApiResult.success();
//	}
}
