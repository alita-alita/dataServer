package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.entity.spider.Hold;
import cn.idicc.taotie.infrastructment.entity.spider.HoldKafka;
import cn.idicc.taotie.infrastructment.request.data.HoldReq;
import cn.idicc.taotie.service.services.data.taoti.HoldService;
import cn.idicc.taotie.service.services.data.taoti.OfficialWebsiteService;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/taoTie/hold")
public class HoldController {

    @Autowired
    private HoldService holdService;

    @PostMapping( "/listHold")
    @PermissionRelease
    public APIResponse <List<Hold>> listHold(@RequestBody HoldReq holdReq){
        PageInfo<Hold> holdPageInfo = holdService.listHold(holdReq);
        return APIResponseBuilder.success(holdPageInfo);

    }

    @PostMapping( "/addHold")
    @PermissionRelease
    public APIResponse  addHold(@RequestBody Hold hold){
        Integer addHold = holdService.addHold(hold);
        return  addHold>0? APIResponseBuilder.success(addHold):APIResponseBuilder.fail(500,"添加失败");

    }


    @PostMapping( "/delLogicHold")
    @PermissionRelease
    public APIResponse delLogicHold(@RequestBody Hold hold){
        Integer logicHold = holdService.delLogicHold(hold);
        return  logicHold>0?APIResponseBuilder.success(logicHold):APIResponseBuilder.fail(500,"删除失败");

    }



}
