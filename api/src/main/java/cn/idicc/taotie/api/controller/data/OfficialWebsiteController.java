package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsite;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsiteKafka;
import cn.idicc.taotie.infrastructment.request.data.OfficialWebsiteReq;
import cn.idicc.taotie.service.services.data.taoti.OfficialWebsiteService;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/taoTie/officialWebsite")
public class OfficialWebsiteController {
    @Autowired
    private OfficialWebsiteService officialWebsiteService;

    /**
     * 根据 企业名称 企业官网采集状态 查找关键词
     * 官网 展示列表
     */
    @PostMapping( "/listOfficialWebsite")
    @PermissionRelease
    public APIResponse<List<OfficialWebsite>> listOfficialWebsite(@RequestBody OfficialWebsiteReq officialWebsiteReq){
        PageInfo<OfficialWebsite> officialWebsitePageInfo = officialWebsiteService.listOfficialWebsite(officialWebsiteReq);
        return APIResponseBuilder.success(officialWebsitePageInfo);
    }

    /**
     * 添加企业官网
     */
    @PostMapping( "/addOfficialWebsite")
    @PermissionRelease
    public APIResponse addOfficialWebsite(@RequestBody OfficialWebsite officialWebsite){
        Integer addOfficialWebsite = officialWebsiteService.addOfficialWebsite(officialWebsite);
        return  addOfficialWebsite>0?APIResponseBuilder.success(addOfficialWebsite):APIResponseBuilder.fail(500,"添加失败");

    }


    /**
     * 修改企业官网
     */
    @PostMapping( "/updateOfficialWebsite")
    @PermissionRelease
    public APIResponse updateOfficialWebsite(@RequestBody OfficialWebsite officialWebsite){
        Integer updateOfficialWebsite = officialWebsiteService.updateOfficialWebsite(officialWebsite);
        return updateOfficialWebsite>0?APIResponseBuilder.success(updateOfficialWebsite):APIResponseBuilder.fail(500,"修改失败");
    }

    /**
     * 逻辑删除企业官网
     */
    @PostMapping( "/delLogicOfficialWebsite")
    @PermissionRelease
    public APIResponse delLogicOfficialWebsite(@RequestBody OfficialWebsite officialWebsite){
        Integer delLogicOfficialWebsite = officialWebsiteService.delLogicOfficialWebsite(officialWebsite);
        return delLogicOfficialWebsite>0?APIResponseBuilder.success(delLogicOfficialWebsite):APIResponseBuilder.fail(500,"删除失败");
    }




}
