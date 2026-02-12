package cn.idicc.taotie.service.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.idicc.taotie.infrastructment.constant.OssProperties;
import cn.idicc.taotie.infrastructment.exception.BizException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.processing.FilerException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Author: WangZi
 * @Date: 2023/3/28
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Component
public class OssUtil {

    @Autowired
    private OssProperties ossProperties;

    public static final Integer SUCCESS_CODE = 200;

    public String upload(InputStream is, String originalFilename) {
        Assert.notNull(is, "输入流不能为空");
        Assert.notNull(originalFilename, "文件全名称不能为空");
        //定义子文件的格式
        String dateStr = LocalDate.now().toString();
        long currentTimeMillis = System.currentTimeMillis();
        String fileExt = FileUtil.extName(originalFilename);
        OSS oss = new OSSClientBuilder().build(ossProperties.getInternalEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            //设置文件路径和名称
            String filePath = String.format("%s/%s/%s-%s", fileExt, dateStr, currentTimeMillis, originalFilename);
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketName(), filePath, is);
            putObjectRequest.setProcess("true");
            PutObjectResult result = oss.putObject(putObjectRequest);
            //文件上传成功后,返回当前文件的路径
            if (result != null) {
                if (SUCCESS_CODE == result.getResponse().getStatusCode()) {
                    return filePath;

                } else {
                    throw new BizException(String.format("OSS上传失败，返回结果为：%s", JSONUtil.toJsonStr(result)));
                }
            }
        } catch (Exception e) {
            log.error("OSS上传失败：", e);
        } finally {
            oss.shutdown();
        }
        return null;
    }

    public String signUrl(String bucketName, String objectName) {
        OSS oss = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            URL url = oss.generatePresignedUrl(bucketName, objectName, expiration);
            if (url != null) {
                return url.toString();
            }
        } catch (Exception e) {
            log.error("OSS加签失败：", e);
        } finally {
            oss.shutdown();
        }
        return null;
    }

    /**
     * pdf文件预览
     *
     * @param url
     * @param ossBucketName
     * @param response
     * @throws Exception
     */
    public void policyDownloadOrRot(String url, String ossBucketName, HttpServletResponse response) throws Exception {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getInternalEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(ossBucketName, url);
        int i = url.lastIndexOf("/");
        //将流上传到网页
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        //通知浏览器以附件形式下载
        response.setContentType("application/pdf; charset=UTF-8");
        response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(url.substring(i + 1), "utf-8"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        // 读去Object内容  返回
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(ossObject.getObjectContent());
            byte[] car = new byte[1024];
            int length = 0;
            while ((length = bufferedInputStream.read(car)) != -1) {
                out.write(car, 0, length);
            }
        } catch (FilerException e) {
            e.getMessage();
            log.error("文件下载异常:{}", e.getMessage());
        } finally {
            out.flush();
            out.close();
            bufferedInputStream.close();
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    public Boolean delete(String bucketName, String objectName) {
        boolean result = Boolean.FALSE;
        OSS oss = new OSSClientBuilder().build(ossProperties.getInternalEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            oss.deleteObject(bucketName, objectName);
            result = Boolean.TRUE;
        } catch (Exception e) {
            log.error("OSS删除失败：", e);

        } finally {
            if (oss != null) {
                oss.shutdown();
            }
        }
        return result;
    }
}
