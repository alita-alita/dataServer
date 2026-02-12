package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryLabelAddRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryLabelUpdateRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryLabelDTO;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.result.CommonPageDTO;
import cn.idicc.taotie.service.services.icm.RecordIndustryLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wd
 * @Description 产业链标签controller
 * @date 12/19/22 9:39 AM
 */
@RestController
@RequestMapping({"/record/admin/industryLabel"})
public class RecordIndustryLabelController {

	@Autowired
	private RecordIndustryLabelService recordIndustryLabelService;



	/**
	 * 添加产业链标签
	 *
	 * @param param
	 * @return
	 */
	@PermissionRelease
	@PostMapping("/add")
	public ApiResult<?> add(@Valid @RequestBody RecordIndustryLabelAddRequest param) {
		recordIndustryLabelService.add(param);
		return ApiResult.success();
	}

	@PermissionRelease
	@GetMapping("/delete")
	public ApiResult<?> delete(@RequestParam("labelId") Long labelId) {
		int effectRow = recordIndustryLabelService.deleteById(labelId);
		if (effectRow > 0) {
			return ApiResult.success();
		} else {
			return ApiResult.error("400", "没有对应的产业链标签");
		}
	}

	@PermissionRelease
	@PostMapping("/update")
	public ApiResult<?> update(@Valid @RequestBody RecordIndustryLabelUpdateRequest updateRequest) {
		int effectRow = recordIndustryLabelService.updateById(updateRequest.getLabelId(),
				updateRequest.getLabelName(), updateRequest.getLabelDesc());
		if (effectRow > 0) {
			return ApiResult.success();
		} else {
			return ApiResult.error("400", "没有对应的产业链标签");
		}
	}


	@PermissionRelease
	@GetMapping("/pageList")
	public ApiResult<?> pageList(@RequestParam("labelName") String labelName,
								 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		return ApiResult.success(recordIndustryLabelService.pageList(labelName, pageNum, 10));
	}

	@PermissionRelease
	@GetMapping("/all")
	public ApiResult<?> all(@RequestParam("labelName") String labelName) {
		int                          pageNum  = 1;
		int                          pageSize = 1000;
		CommonPageDTO<RecordIndustryLabelDTO> pageData = recordIndustryLabelService.pageList(labelName, pageNum, pageSize);
		List<RecordIndustryLabelDTO> data     = new ArrayList<>(pageData.getRecords());
		long                         total    = pageData.getTotal();
		while (total > (long) pageNum * pageSize) {
			pageData = recordIndustryLabelService.pageList(labelName, pageNum, pageSize);
			data.addAll(pageData.getRecords());
			pageNum++;
		}
		return ApiResult.success(data);
	}

}
