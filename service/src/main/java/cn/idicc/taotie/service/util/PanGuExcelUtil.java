package cn.idicc.taotie.service.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.idicc.identity.error.BizErrorCode;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.exception.BizException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName ExcelUtil
 * @Description excel工具类
 * @Author WangZi
 * @Date 2022/12/19
 * @Version 1.0
 **/
@Slf4j
public class PanGuExcelUtil {

    /**
     * 读取excel文件
     *
     * @param file           文件
     * @param headerRowIndex excel头起始下标，下标从0开始
     * @param startRowIndex  excel数据读取起始下标
     * @param endRowIndex    excel数据读取结束下标
     * @param beanType       目标类的类型对象
     * @return
     * @throws IOException
     */
    public static <T> List<T> readExcel(MultipartFile file, int headerRowIndex, int startRowIndex, int endRowIndex, Class<T> beanType) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        return reader.read(headerRowIndex, startRowIndex, endRowIndex, beanType);
    }

    /**
     * 读取excel文件
     *
     * @param inputStream    文件流
     * @param headerRowIndex excel头起始下标，下标从0开始
     * @param startRowIndex  excel数据读取起始下标
     * @param endRowIndex    excel数据读取结束下标
     * @param beanType       目标类的类型对象
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> readExcel(InputStream inputStream, int headerRowIndex, int startRowIndex, int endRowIndex, Class<T> beanType) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        return reader.read(headerRowIndex, startRowIndex, endRowIndex, beanType);
    }

    /**
     * 导出excel文件
     *
     * @param list     导出的集合对象
     * @param response http响应
     * @param fileName 文件名称
     * @param errorMsg 错误信息
     */
    public static void exportExcel(List<?> list, HttpServletResponse response, String fileName, String errorMsg) {
        if (CollectionUtil.isEmpty(list) || Objects.isNull(response) || StringUtils.isBlank(fileName)) {
            throw new BizException("调用excel工具类的导出方法的入参不满足要求");
        }

        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            writer.setOnlyAlias(Boolean.TRUE);
            writer.write(list, Boolean.TRUE);
            writer.autoSizeColumnAll();
            StringBuilder newFileName = new StringBuilder();
            newFileName.append(fileName).append(DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN)).append(GlobalConstant.EXCEL_XLSX_SUFFIX);
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(newFileName.toString(), "UTF-8"));
            response.setContentType("application/octet-stream");
            writer.flush(response.getOutputStream());
        } catch (Exception e) {
            log.error(errorMsg, e);
            throw new BizException(ErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param fileName  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String fileName, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");
        response.getOutputStream().write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        EasyExcelFactory.write(response.getOutputStream(), head)
                .sheet(sheetName)
                .doWrite(data);
        response.getOutputStream().flush();
    }

    public static void export(String filename, List<?> dataResult, Class<?> clazz, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            filename = URLEncoder.encode(filename, "UTF-8").concat(ExcelTypeEnum.XLSX.getValue());
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            EasyExcel.write(response.getOutputStream(), clazz).sheet(filename).doWrite(dataResult);
        } catch (Exception ex) {
            throw new BizException("导出excel异常，" + ex.getMessage());
        }
    }

    /**
     * 校验excel导入模板是否符合规则
     *
     * @param reader
     * @param excelHeard excel头列表属性值
     */
    public static void checkExcel(ExcelReader reader, String[] excelHeard) {
        List<List<Object>> read = reader.read(0, 0);
        if (CollectionUtil.isEmpty(read)) {
            throw new BizException(BizErrorCode.EXCEL_MODULE_ERROR.getMessage());
        }
    }

    /**
     * 过滤社会统一信用代码或企业名称无效的excel数据
     *
     * @param source                       读取的excel对象
     * @param patentNo 专利号在excel读取的集合中的下标
     * @param enterpriseNameIndex          企业名称在excel读取的集合中的下标
     * @return
     */
    public static List<List<Object>> filterPatentNoAndEnterpriseNameIsBlank(List<List<Object>> source, int patentNo, int enterpriseNameIndex) {
        List<List<Object>> result = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(source)) {
            //专利号的值在list集合13的下标中
            result = source.stream()
                    .filter(l -> Objects.nonNull(l.get(patentNo))
                            && StringUtils.isNotBlank(StringUtils.trim(l.get(patentNo).toString()))
                            && !StringUtils.equals(GlobalConstant.WHIPPLETREE, StringUtils.trim(l.get(patentNo).toString()))
                            && Objects.nonNull(l.get(enterpriseNameIndex))
                            && StringUtils.isNotBlank(StringUtils.trim(l.get(enterpriseNameIndex).toString()))
                            && !StringUtils.equals(GlobalConstant.WHIPPLETREE, StringUtils.trim(l.get(enterpriseNameIndex).toString())))
                    .collect(Collectors.toList());
        }
        return result;
    }

//    /**
//     * 获取社会信用代码或企业名称无效的excel数据
//     *
//     * @param list 企业信息导入从excel中读取的企业信息集合
//     */
//    public static List<EnterpriseUploadDTO> getUnifiedSocialCreditCodeOrEnterpriseNameIsVoid(List<EnterpriseUploadDTO> list) {
//        List<EnterpriseUploadDTO> result = CollectionUtil.newArrayList();
//        if (CollectionUtil.isNotEmpty(list)) {
//            result = list.parallelStream().filter(l -> StringUtils.isBlank(l.getUnifiedSocialCreditCode())
//                            || StringUtils.equals(GlobalConstant.WHIPPLETREE, StringUtils.trim(l.getUnifiedSocialCreditCode()))
//                            || StringUtils.isBlank(l.getEnterpriseName())
//                            || StringUtils.equals(GlobalConstant.WHIPPLETREE, StringUtils.trim(l.getEnterpriseName())))
//                    .collect(Collectors.toList());
//        }
//        return result;
//    }
}
