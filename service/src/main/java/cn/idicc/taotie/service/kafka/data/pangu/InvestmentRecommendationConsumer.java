package cn.idicc.taotie.service.kafka.data.pangu;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.entity.data.UploadFileRecordDO;
import cn.idicc.taotie.infrastructment.constant.ExcelProperties;
import cn.idicc.taotie.infrastructment.constant.OssProperties;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.infrastructment.enums.UploadFileStatusEnum;
import cn.idicc.taotie.infrastructment.response.data.InvestmentEnterpriseRecommendUploadDTO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentEnterpriseRecommendService;
import cn.idicc.taotie.service.services.data.pangu.UploadFileRecordService;
import cn.idicc.taotie.service.util.PanGuExcelUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: WangZi
 * @Date: 2023/3/30
 * @Description: 处理导入招商推荐消息消费者
 * @version: 1.0
 */
@Slf4j
@Component
@DependsOn(value = {"namespaceProperties"})
public class InvestmentRecommendationConsumer {

    @Autowired
    private UploadFileRecordService uploadFileRecordService;

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private ExcelProperties excelProperties;

    @Autowired
    private InvestmentEnterpriseRecommendService investmentEnterpriseRecommendService;


    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.UPLOAD_INVESTMENT_RECOMMENDATION_TOPIC})
    public void consumerUploadInvestmentRecommendation(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Long id = Long.valueOf(record.value());
        UploadFileRecordDO uploadFileRecordDO = uploadFileRecordService.getById(id);
        OSS oss = null;
        try {
            if (Objects.nonNull(uploadFileRecordDO)) {
                String path = uploadFileRecordDO.getPath();
                oss = new OSSClientBuilder().build(ossProperties.getInternalEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
                OSSObject ossObject = oss.getObject(ossProperties.getBucketName(), path);
                InputStream content = ossObject.getObjectContent();
                if (content != null) {
                    List<InvestmentEnterpriseRecommendUploadDTO> list = PanGuExcelUtil.readExcel(content,
                            excelProperties.getHeaderRowIndex(),
                            excelProperties.getStartRowIndex(),
                            excelProperties.getEndRowIndex(),
                            InvestmentEnterpriseRecommendUploadDTO.class);
                    Set<InvestmentEnterpriseRecommendUploadDTO> errorList = investmentEnterpriseRecommendService.uploadBatchSave(list, id);
                    UploadFileStatusEnum statusEnum;
                    if (CollectionUtil.isEmpty(errorList)) {
                        statusEnum = UploadFileStatusEnum.DEAL_SUCCESS;
                    } else {
                        statusEnum = UploadFileStatusEnum.DEAL_FAIL;
                    }
                    uploadFileRecordService.updateStatus(id, statusEnum);
                    content.close();
                }
            }

        } catch (Exception e) {
            log.error("消费导入招商推荐任务失败，任务id为{}", uploadFileRecordDO.getId(), e);
            throw new RuntimeException(e);

        } finally {
            ack.acknowledge();
            if (oss != null) {
                oss.shutdown();
            }
        }
    }
}
