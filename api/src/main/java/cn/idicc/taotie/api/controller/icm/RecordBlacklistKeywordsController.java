package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.entity.icm.RecordBlacklistKeywordsDO;
import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.icm.RecordBlacklistKeywordsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/record/admin/blacklistKeywords")
public class RecordBlacklistKeywordsController {

    @Autowired
    private RecordBlacklistKeywordsService recordBlacklistKeywordsService;


    /**
     * 查询所有与其产业链相匹配的黑名单关键词
     *
     * @param recordBlacklistKeywordsDO
     * @return
     */
    @PostMapping("/selectAll")
    @PermissionRelease
    ApiResult selectAll(@RequestBody RecordBlacklistKeywordsDO recordBlacklistKeywordsDO){
        List<RecordBlacklistKeywordsDO> blacklistKeywords = recordBlacklistKeywordsService.selectAll(recordBlacklistKeywordsDO);
        return ApiResult.success(blacklistKeywords);
    }

    /**
     * 添加黑名单关键词
     * @param recordBlacklistKeywordsDO
     * @return
     */
    @PostMapping("/addRecordBlacklistKeywords")
    @PermissionRelease
    ApiResult addRecordBlacklistKeywords(@RequestBody RecordBlacklistKeywordsDO recordBlacklistKeywordsDO){
        Integer addRecordBlacklistKeywords = recordBlacklistKeywordsService.addRecordBlacklistKeywords(recordBlacklistKeywordsDO);
        return addRecordBlacklistKeywords>0?ApiResult.success(addRecordBlacklistKeywords):ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(),"添加失败");

    }

    /**
     * 删除黑名单关键词(逻辑删)
     * @param ids
     * @return
     */
    @PostMapping("/deleteRecordBlacklistKeywords")
    @PermissionRelease
    ApiResult deleteRecordBlacklistKeywords(@RequestBody List<Long> ids){
        Integer result = recordBlacklistKeywordsService.deleteRecordBlacklistKeywords(ids);
        return result>0?ApiResult.success(result):ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(),"删除失败");
    }

    @PostMapping("/deleteRecordBlacklistKeywordsByName")
    @PermissionRelease
    ApiResult deleteRecordBlacklistKeywordsByName(@RequestBody RecordBlacklistKeywordsDO recordBlacklistKeywordsDO){
        Integer result = recordBlacklistKeywordsService.deleteRecordBlacklistKeywords(recordBlacklistKeywordsDO);
        return result>0?ApiResult.success(result):ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(),"删除失败");
    }

}
