package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.request.data.KeywordReq;
import cn.idicc.taotie.service.services.data.taoti.KeywordService;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/taoTie/keyword")
public class KeywordController {
    @Autowired
    private KeywordService keywordService;

    /**
     * 根据 关键词名称 采集状态 查找关键词
     * 关键词展示列表
     */
    @PostMapping( "/listKeyword")
    @PermissionRelease
    public APIResponse<List<Keyword>> listKeyword(@RequestBody KeywordReq keywordReq){
        PageInfo<Keyword> keywords = keywordService.listKeyword(keywordReq);
        return APIResponseBuilder.success(keywords);
    }


    /**
     * 添加关键词
     *///√
    @PostMapping( "/addKeyword")
    @PermissionRelease
    public APIResponse addKeyword(@RequestBody Keyword keyword){
        Integer addedKeyword = keywordService.addKeyword(keyword);
        return addedKeyword>0?APIResponseBuilder.success(addedKeyword):APIResponseBuilder.fail(500,"添加失败");

    }


    /**
     * 修改关键词
     */
    @PostMapping( "/updateKeyword")
    @PermissionRelease
    public APIResponse updateKeyword(@RequestBody Keyword keyword){
        Integer result = keywordService.updateKeyword(keyword);
        return result>0?APIResponseBuilder.success(result):APIResponseBuilder.fail(500,"修改失败");
    }

    @PostMapping( "/updateKeywordOF")
    @PermissionRelease
    public APIResponse updateKeywordOF(@RequestBody Keyword keyword){
        Integer result = keywordService.updateKeywordOF(keyword);
        return result>0?APIResponseBuilder.success(result):APIResponseBuilder.fail(500,"修改失败");
    }


    /**
     * 逻辑删除关键字
     */
    @PostMapping( "/delLogicKeyword")
    @PermissionRelease
    public APIResponse delLogicKeyword(@RequestBody Keyword keyword){
        Integer delLogicKeyword = keywordService.delLogicKeyword(keyword);
        return delLogicKeyword>0?APIResponseBuilder.success(delLogicKeyword):APIResponseBuilder.fail(500,"删除失败");
    }

    /**
     * 删除关键词(物理删除)
     */
    @PostMapping( "/deleteKeyword")
    @PermissionRelease
    public APIResponse deleteKeyword(@RequestBody Keyword keyword){
        Integer result = keywordService.deleteKeyword(keyword);
        return result>0?APIResponseBuilder.success(result):APIResponseBuilder.fail(500,"修改失败");
    }

}
