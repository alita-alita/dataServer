package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.entity.icm.IndustryChainAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.request.icm.AddAtomNodeRefIndustryLabelRequest;
import cn.idicc.taotie.infrastructment.request.icm.IndustryChainAtomNodeAddRequest;
import cn.idicc.taotie.infrastructment.request.icm.IndustryChainAtomNodeUpdateRequest;
import cn.idicc.taotie.infrastructment.response.icm.AtomLabelChainRefBO;
import cn.idicc.taotie.infrastructment.response.icm.IndustryChainAtomNodeDTO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryLabelDTO;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.result.CommonPageDTO;
import cn.idicc.taotie.service.services.icm.AtomNodeRefIndustryLabelService;
import cn.idicc.taotie.service.services.icm.ChainNodeRefAtomNodeService;
import cn.idicc.taotie.service.services.icm.IndustryChainAtomNodeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/record/admin/atom")
public class IndustryChainAtomNodeController {

	private static final Logger logger = LoggerFactory.getLogger(IndustryChainAtomNodeController.class);

	@Autowired
	private IndustryChainAtomNodeService industryChainAtomNodeService;

	@Autowired
	private AtomNodeRefIndustryLabelService atomNodeRefIndustryLabelService;

	@Autowired
	private ChainNodeRefAtomNodeService chainNodeRefAtomNodeService;

	@PermissionRelease
	@GetMapping("/pageList")
	@ResponseBody
	public ApiResult<?> list(@RequestParam("nodeName") String nodeName,
							 @RequestParam("pageNum") Integer pageNum) {
		Page<IndustryChainAtomNodeDO> page = new Page<>(pageNum, 10);
		IPage<IndustryChainAtomNodeDO> pageData = industryChainAtomNodeService.page(page,
				Wrappers.lambdaQuery(IndustryChainAtomNodeDO.class)
						.orderByDesc(IndustryChainAtomNodeDO::getId)
						.like(StringUtils.isNotEmpty(nodeName), IndustryChainAtomNodeDO::getAtomNodeName, nodeName));

		Map<Long, List<AtomLabelChainRefBO>> atomNodeMap = new HashMap<>();
		// 原子节点IDs
		List<Long> atomNodeIds = pageData.getRecords().stream().map(IndustryChainAtomNodeDO::getId).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(atomNodeIds)) {
			atomNodeMap = atomNodeRefIndustryLabelService.queryByAtomNodeIds(atomNodeIds).stream().collect(Collectors.groupingBy(AtomLabelChainRefBO::getAtomNodeId));
		}
		Map<Long, List<AtomLabelChainRefBO>> finalAtomNodeMap = atomNodeMap;
		List<IndustryChainAtomNodeDTO> data = pageData.getRecords().stream().map(item -> {
			IndustryChainAtomNodeDTO industryChainAtomNodeDTO = IndustryChainAtomNodeDTO.adapt(item);
			industryChainAtomNodeDTO.setAtomLabelChainRefList(finalAtomNodeMap.get(item.getId()));
			return industryChainAtomNodeDTO;
		}).collect(Collectors.toList());
		return ApiResult.success(CommonPageDTO.of(data, page.getSize(), page.getCurrent(), page.getTotal()));
	}

	@PermissionRelease
	@GetMapping("/listOptions")
	@ResponseBody
	public ApiResult<?> listOptions(@Param("nodeName") String nodeName) {
		List<IndustryChainAtomNodeDO> data = industryChainAtomNodeService.list(
				Wrappers.lambdaQuery(IndustryChainAtomNodeDO.class)
						.orderByDesc(IndustryChainAtomNodeDO::getId)
						.like(StringUtil.isNotEmpty(nodeName), IndustryChainAtomNodeDO::getAtomNodeName, nodeName));

		List<Map<String, Object>> options = data.stream().map(row -> {
			Map<String, Object> raw = new HashMap<>();
			raw.put("label", row.getAtomNodeName());
			raw.put("value", row.getId());
			raw.put("nodeDesc", row.getNodeDesc());
			return raw;
		}).collect(Collectors.toList());
		return ApiResult.success(options);
	}

	@PermissionRelease
	@PostMapping("/add")
	@ResponseBody
	public ApiResult<?> add(@Valid @RequestBody IndustryChainAtomNodeAddRequest request) {

		IndustryChainAtomNodeDO existDO = industryChainAtomNodeService.selectOne(request.getAtomNodeName());
		if (existDO != null) {
			return ApiResult.error("400", "原子节点已存在");
		}

		IndustryChainAtomNodeDO atomNodeDO = new IndustryChainAtomNodeDO();
		atomNodeDO.setAtomNodeName(request.getAtomNodeName());
		atomNodeDO.setNodeDesc(request.getNodeDesc());
		industryChainAtomNodeService.save(atomNodeDO);
		return ApiResult.success();
	}

	@PermissionRelease
	@GetMapping("/delete")
	@ResponseBody
	public ApiResult<?> add(@RequestParam("atomNodeId") Long atomNodeId) {
		ApiResult<?> result = checkRefChainState(atomNodeId);
		if (result != null) {
			return result;
		}
		if (atomNodeId <= 0) {
			return ApiResult.error("400", "请输入合法信息");
		}
		int effectRows = industryChainAtomNodeService.deleteById(atomNodeId);
		if (effectRows > 0) {
			return ApiResult.success();
		} else {
			return ApiResult.error("400", "原子节点删除失败");
		}
	}

	@PermissionRelease
	@PostMapping("/update")
	@ResponseBody
	public ApiResult<?> update(@Valid @RequestBody IndustryChainAtomNodeUpdateRequest request) {
		if(request.getAtomId() == null){
			return ApiResult.error("400", "请选择原子节点进行操作");
		}
		ApiResult<?> result = checkRefChainState(request.getAtomId());
		if (result != null) {
			return result;
		}
		int effectRows = industryChainAtomNodeService.updateAndRefActions(
				request.getAtomId(), request.getAtomNodeName(), request.getNodeDesc()
		);

		if (effectRows > 0) {
			return ApiResult.success();
		} else {
			return ApiResult.error("400", "原子节点更新失败");
		}
	}

	/**
	 * 原子节点关联产业链标签
	 */
	@PermissionRelease
	@PostMapping("/refIndustryLabel")
	@ResponseBody
	public ApiResult<?> addAtomNodeRefIndustryLabel(@RequestBody AddAtomNodeRefIndustryLabelRequest request) {
		//校验该原子节点 关联产业链的状态，如果产业链在生产中，则不允许更改
		//原因：生产过程中，完整的产业链信息会存储在向量数据库中，当判断产品是否处于某节点时，需要通过向量搜索召回相似的节点信息并进行计算，
		//若此时发生变更，则已完成计算的任务就作废
		ApiResult<?> result = checkRefChainState(request.getAtomNodeId());
		if (result != null) {
			return result;
		}

		atomNodeRefIndustryLabelService.doRefLabel(request.getAtomNodeId(), request.getIndustryLabelId());
		return ApiResult.success();
	}

	/**
	 * 高危操作确认
	 * 1. 更新节点定义
	 * 2. 删除节点
	 * 3. 新增标签关系
	 * 4. 删除标签关系
	 */
	@PermissionRelease
	@GetMapping("/highDangerActionConfirm")
	@ResponseBody
	public ApiResult<?> highDangerActionConfirm(@Param("atomNode") Long atomNodeId) {
		String message = industryChainAtomNodeService.highDangerActionConfirm(atomNodeId);
		return ApiResult.success(message);
	}

	private ApiResult<?> checkRefChainState(Long atomNodeId) {
		List<RecordIndustryChainDO> refChains = chainNodeRefAtomNodeService.findRefChain(atomNodeId, null);
		if (!refChains.isEmpty()) {
			for (RecordIndustryChainDO chainDO : refChains) {
				if (chainDO.getState() > RecordChainStateEnum.NORMAL.getValue()
				&& chainDO.getState() < RecordChainStateEnum.SYNC_PRODUCTION_FINISH.getValue()) {
					return ApiResult.error("400", "该原子节点关联的产业链【" + chainDO.getChainName() + "】正在生产中，无法进行变更");
				}
			}
		}
		return null;
	}
}
