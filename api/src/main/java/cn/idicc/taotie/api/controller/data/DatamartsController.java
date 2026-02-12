package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.service.services.data.billund.DatamartsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping("/taoTi/datamarts")
public class DatamartsController {
    @Autowired
    private DatamartsService datamartsService;

    @PostMapping("/uploadZip")
    @PermissionRelease
    public APIResponse uploadZipFile(@RequestParam("zipFile") MultipartFile zipFile) {
        String errorMsg = datamartsService.processZipFile(zipFile);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }


}
