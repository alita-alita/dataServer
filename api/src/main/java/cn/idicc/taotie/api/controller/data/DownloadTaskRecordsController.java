package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.entity.spider.DownloadTaskRecords;
import cn.idicc.taotie.infrastructment.request.data.DownloadTaskRecordsReq;
import cn.idicc.taotie.service.services.data.taoti.DownloadTaskRecordsService;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/taoTie/downloadTaskRecords")
public class DownloadTaskRecordsController {
    @Autowired
    private DownloadTaskRecordsService downloadTaskRecordsService;
    @RequestMapping("/listDownloadTaskRecords")
    @PermissionRelease
    public APIResponse<List<DownloadTaskRecords>>listDownloadTaskRecords(DownloadTaskRecordsReq downloadTaskRecordsReq){
        PageInfo<DownloadTaskRecords> recordsPageInfo = downloadTaskRecordsService.listDownloadTaskRecords(downloadTaskRecordsReq);
        return APIResponseBuilder.success(recordsPageInfo);
    }


//    @RequestMapping("/addDownloadTaskRecords")
//    public APIResponse addDownloadTaskRecords(DownloadTaskRecords downloadTaskRecords){
//        Integer addDownloadTaskRecords = downloadTaskRecordsService.addDownloadTaskRecords(downloadTaskRecords);
//        return addDownloadTaskRecords>0?APIResponseBuilder.success(addDownloadTaskRecords):APIResponseBuilder.fail(500,"修改失败");
//
//    }

    @RequestMapping("/delLogicDownloadTaskRecords")
    @PermissionRelease
    public APIResponse delLogicDownloadTask (@RequestBody DownloadTaskRecords downloadTaskRecords){
        Integer delLogicDownloadTaskRecords = downloadTaskRecordsService.delLogicDownloadTaskRecords(downloadTaskRecords);
        return delLogicDownloadTaskRecords>0?APIResponseBuilder.success(delLogicDownloadTaskRecords):APIResponseBuilder.fail(500,"修改失败");
    }











}
