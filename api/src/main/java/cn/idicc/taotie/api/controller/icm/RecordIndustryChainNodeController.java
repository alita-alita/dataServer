package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.entity.icm.ChainNodeRefAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.request.icm.SaveRecordIndustryChainNodeRequest;
import cn.idicc.taotie.infrastructment.request.icm.UpdateNodeScoreRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainNodeDTO;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.icm.ChainNodeRefAtomNodeService;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainNodeService;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wd
 * @Description 产业链节点controller
 * @date 12/19/22 9:39 AM
 */
@Validated
@RestController
@RequestMapping({"/record/admin/industryChainNode"})
@Slf4j
public class RecordIndustryChainNodeController {

    @Resource
    RecordIndustryChainNodeService recordIndustryChainNodeService;
	@Autowired
	private ChainNodeRefAtomNodeService chainNodeRefAtomNodeService;

    /**
     * 保存/更新产业链节点
     *
     * @param saveChainRequest
     * @return
     */
    @PermissionRelease
    @PostMapping("/save")
    public ApiResult<?> save(@Valid @RequestBody SaveRecordIndustryChainNodeRequest saveChainRequest) {
        log.info("IndustryChainNodeController save params is:{}", JSON.toJSONString(saveChainRequest));
        try {
            if(saveChainRequest.getIsLeaf() == 0 && StringUtils.isEmpty(saveChainRequest.getNodeName())){
                return ApiResult.error("400", "节点名称不可为空");
            }
            if(saveChainRequest.getIsLeaf() == 1 && StringUtils.isEmpty(saveChainRequest.getAtomNodeValue())){
                return ApiResult.error("400","请选择一个原子节点");
            }
            if(saveChainRequest.getIsLeaf() == 1) {
                List<RecordIndustryChainDO> exist = chainNodeRefAtomNodeService.findRefChain(Long.valueOf(saveChainRequest.getAtomNodeValue()), saveChainRequest.getChainNodeId());
                Set<RecordIndustryChainDO> duplicateChainIds = exist.stream()
                        .filter(row->row.getBizId().equals(saveChainRequest.getChainId()))
                        .collect(Collectors.toSet());
                if (!duplicateChainIds.isEmpty()) {
                    return ApiResult.error("400", "该原子节点已被当前产业链引用，无法重复");
                }
            }

            recordIndustryChainNodeService.saveOrUpdate(saveChainRequest);
        } catch (DuplicateKeyException ex) {
            throw new BizException(ErrorCode.DUPLICATE_DATA.getCode(), ErrorCode.DUPLICATE_DATA.getMessage());
        } catch (Exception ex) {
            log.error("node save error:", ex);
            throw new BizException(ErrorCode.SYSTEM_ERROR.getCode(), ex.getMessage());
        }
        return ApiResult.success(true);
    }

    @PermissionRelease
    @PostMapping("/score/update")
    public ApiResult<?> scoreUpdate(@Valid @RequestBody UpdateNodeScoreRequest saveChainRequest) {
        recordIndustryChainNodeService.updateThresholdScore(saveChainRequest);
        return ApiResult.success(true);
    }

    /**
     * 根据产业链id查询产业链节点树
     *
     * @param chainId 产业链id
     * @return
     */
    @PermissionRelease
    @GetMapping("/tree")
    public ApiResult<?> tree(@NotNull(message = "chainId为空") @RequestParam("chainId") Long chainId) {
        return ApiResult.success(recordIndustryChainNodeService.queryChainNodeTreeByChainId(chainId));
    }

    @PermissionRelease
    @GetMapping("/leaf/list")
    public ApiResult<?> leafList(@NotNull(message = "chainId为空") @RequestParam("chainId") Long chainId) {
        return ApiResult.success(recordIndustryChainNodeService.queryChainNodeLeafListByChainId(chainId));
    }

    /**
     * 根据产业链节点id查询产业链节点详情
     *
     * @param id 产业链节点id
     * @return
     */
    @PermissionRelease
    @GetMapping("/detail")
    public ApiResult<RecordIndustryChainNodeDTO> detail(@NotNull(message = "节点Id不可为空") @RequestParam(value = "id") Long id) {
        return ApiResult.success(recordIndustryChainNodeService.detail(id));
    }

    /**
     * 删除产业链节点
     *
     * @param id 产业链节点id
     * @return
     */
    @PermissionRelease
    @GetMapping("/delete")
    public ApiResult<Boolean> delete(@NotNull(message = "节点Id不可为空") @RequestParam(value = "id") Long id) {
        try {
            recordIndustryChainNodeService.delete(id);
        } catch (Exception ex) {
            log.error("delete chainNode failed,error msg is:{}", ex.getMessage());
            return ApiResult.error(ErrorCode.SYSTEM_ERROR.getCode(), ex.getMessage());
        }
        return ApiResult.success(Boolean.TRUE);
    }

}
