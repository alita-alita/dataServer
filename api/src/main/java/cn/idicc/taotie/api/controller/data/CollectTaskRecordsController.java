package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.entity.spider.CollectTaskRecords;

import cn.idicc.taotie.service.services.data.taoti.ConllectTaskRecordsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/taoTi/collectRecords")
public class CollectTaskRecordsController {
    @Autowired
    private ConllectTaskRecordsService conllectTaskRecordsService;

    /**
     * 获取采集任务记录
     */
    @PostMapping("/listConllectRecords")
    @PermissionRelease
    public APIResponse<List<CollectTaskRecords>> listConllectRecords(@RequestBody CollectTaskRecords collectTaskRecordsType) {
        List<CollectTaskRecords> collectTaskRecordsList = conllectTaskRecordsService.listConllectTaskRecords(collectTaskRecordsType);
        return APIResponseBuilder.success(collectTaskRecordsList);
    }

}
