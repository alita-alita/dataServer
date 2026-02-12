package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.request.data.*;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.service.services.data.billund.ExcelReaderAndKafkaSenderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/exceldispose")
public class ExcelDisposeController {

    public ExcelDisposeController() {
        log.info("{}初始化", getClass().getName());
    }

    @Autowired
    private ExcelReaderAndKafkaSenderService excelReaderAndKafkasenderservice;

    //---------------------------------------采集数据至库----------------------------------------------
    /**
     * 人才招商-关键词 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/talentInformationCollection")
    @PermissionRelease
    public APIResponse talentInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.talentInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 资本招商-关键词 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/capitalInformationCollection")
    @PermissionRelease
    public APIResponse capitalInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.capitalInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }


    /**
     * 产品舆情-关键词 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/productInformationCollection")
    @PermissionRelease
    public APIResponse productInformationCollection(MultipartFile file)throws Exception{
        String errorMsg = excelReaderAndKafkasenderservice.ProductInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 企业舆情-关键词 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/enterpriseInformationCollection")
    @PermissionRelease
    public APIResponse enterpriseInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.enterpriseInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 企业舆情-公众号 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/enterpriseOFInformationCollection")
    @PermissionRelease
    public APIResponse enterpriseOFInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.enterpriseOFInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 亲缘舆情-公众号 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/qinshangOFInformationCollection")
    @PermissionRelease
    public APIResponse qinshangOFInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.qinshangOFInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 产业舆情-公众号 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/industryOFInformationCollection")
    @PermissionRelease
    public APIResponse industryOFInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.industryOFInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 官网 信息采集
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/OfficialWebsiteInformationCollection")
    @PermissionRelease
    public APIResponse OfficialWebsiteInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.OfficialWebsiteInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }
    /**
     * 核对
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/HoldInformationCollection")
    @PermissionRelease
    public APIResponse HoldInformationCollection (MultipartFile file) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.HoldInformationCollection(file);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }






//-------------------------------------------手动采集任务--------------------------------------------

    /**
     * 人才招商-关键词-手动采集任务
     */
    @PostMapping( "/manualGatherTalent")
    @PermissionRelease
    public APIResponse manualGatherTalent (@RequestBody List<Integer> keywordIds) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherTalent(keywordIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 资本招商-关键词-动采集任务
     */
    @PostMapping( "/manualGatherCapital")
    @PermissionRelease
    public APIResponse manualGatherCapital (@RequestBody List<Integer> keywordIds) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherCapital(keywordIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 产品招商-关键词-手动采集任务
     */
    @PostMapping( "/manualGatherProducts")
    @PermissionRelease
    public APIResponse manualGatherProducts(@RequestBody List<Integer> keywordIds)throws Exception{
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherProducts(keywordIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 企业舆情-关键词-手动采集任务
     */
    @PostMapping( "/manualGatherEnterprise")
    @PermissionRelease
    public APIResponse manualGatherEnterprise (@RequestBody List<Integer> keywordIds) throws Exception {
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherEnterprise(keywordIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 企业舆情-公众号-手动采集任务
     */
    @PostMapping( "/manualGatherEnterpriseOF")
    @PermissionRelease
    public APIResponse manualGatherEnterpriseOF (@RequestBody List<Integer> keywordIds) throws Exception{
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherEnterpriseOF(keywordIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 亲缘舆情-公众号-手动采集任务
     */
    @PostMapping( "/manualGatherQinshangOF")
    @PermissionRelease
    public APIResponse manualGatherQinshangOF (@RequestBody List<Integer> keywordIds) throws Exception{
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherQinshangOF(keywordIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 产业舆情-公众号-手动采集任务
     */
    @PostMapping( "/manualGatherIndustryOF")
    @PermissionRelease
    public APIResponse manualGatherIndustryOF (@RequestBody List<Integer> keywordIds) throws Exception{
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherIndustryOF(keywordIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 官网采集 -手动采集任务
     */

    @PostMapping( "/manualGatherOfficialWebsite")
    @PermissionRelease
    public APIResponse manualGatherOfficialWebsite (@RequestBody List<Long> officialWebsiteIds) throws Exception{
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherOfficialWebsiteIds(officialWebsiteIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }

    /**
     * 校验
     */
    @PostMapping( "/manualGatherHold")
    @PermissionRelease
    public APIResponse manualGatherHold (@RequestBody List<Long> HoldIds) throws Exception{
        String errorMsg = excelReaderAndKafkasenderservice.manualGatherHoldIds(HoldIds);
        return errorMsg == null ? APIResponseBuilder.success() : APIResponseBuilder.fail(500, errorMsg);
    }



    //-------------------------------------导出模板----------------------------------------
    /**
     * 【人才招商-关键词采集】导出模板
     */
    @GetMapping("/excelExportTalent")
    @PermissionRelease
    public void excelExportTalent(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "人才招商-关键词采集", ExcelExportTalent.class,true);
    }

    /**
     * 【资本招商-关键词采集】导出模板
     */
    @GetMapping("/excelExportCapital")
    @PermissionRelease
    public void excelExportCapital(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "资本招商-关键词采集", ExcelExportCapital.class ,true);
    }

    /**
     * 【企业舆情-关键词采集】导出模板
     */
    @GetMapping("/excelExportEnterprise")
    @PermissionRelease
    public void excelExportEnterprise(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "企业舆情-关键词采集", ExcelExportEnterprise.class,true);
    }

    /**
     * 【产品舆情-关键词采集】导出模板
     */
    @GetMapping("/excelExportProduct")
    @PermissionRelease
    public void excelExportProduct(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "产品舆情-关键词采集", ExcelExportProduct.class,true);
    }

    /**
     * 【企业舆情-微信公众号采集】导出模板
     */
    @GetMapping("/excelExportEnterpriseOF")
    @PermissionRelease
    public void excelExportEnterpriseOF(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "企业舆情-微信公众号采集", ExcelExportEnterpriseOF.class ,true);
    }

    /**
     * 【亲缘舆情-微信公众号采集】导出模板
     */
    @GetMapping("/excelExportQinshangOF")
    @PermissionRelease
    public void excelExportQinshangOF(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "亲缘舆情-微信公众号采集", ExcelExportQinshangOF.class,true);
    }
    /**
     * 【产业舆情-微信公众号采集】导出模板
     */
    @GetMapping("/excelExportIndustryOF")
    @PermissionRelease
    public void excelExportIndustryOF(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "产业舆情-微信公众号采集", ExcelExportIndustryOF.class,true);
    }
    /**
     * 【官网信息采集】导出模板
     */
    @GetMapping("/excelExportOfficialWebsite")
    @PermissionRelease
    public void excelExportOfficialWebsite(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "官网信息采集", ExcelExportOfficialWebsite.class,true);
    }

    /**
     * 【公司核对】导出模板
     */
    @GetMapping("/excelExportHold")
    @PermissionRelease
    public void excelExportHold (HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "【核对】导入公司信息模版", ExcelExportPublisher.class,true);
    }


}
