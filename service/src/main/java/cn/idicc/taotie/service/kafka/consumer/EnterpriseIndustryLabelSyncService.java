package cn.idicc.taotie.service.kafka.consumer;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseIndustryLabelRelationDO;
import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseIndustryLabelRelationMapper;
import cn.idicc.taotie.service.message.data.KafkaDataMessage;
import cn.idicc.taotie.service.services.data.billund.BillundAutoService;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: WangZi
 * @Date: 2023/8/22
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class EnterpriseIndustryLabelSyncService{

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BillundAutoService billundAutoService;

    @Autowired
    private EnterpriseIndustryLabelRelationMapper enterpriseIndustryLabelRelationMapper;

    public void add(KafkaDataMessage message) {
        try {
            billundAutoService.consumeMessage(message);
            Long eId = Long.valueOf(message.getData().getElement().get("enterprise_id").split("\\.")[0]);
            enterpriseService.doSyncDataToEs(eId);
        } catch (Exception e) {
            log.error(String.format("ads新增操作数据同步出现异常，message: %s", message.toString()), e);
        }
    }

    public void update(KafkaDataMessage message) {
        try {
            EnterpriseIndustryLabelRelationDO oldRelationDO = enterpriseIndustryLabelRelationMapper.selectById(message.getData().getKey_id());
            billundAutoService.consumeMessage(message);
            Long newEId = Long.valueOf(message.getData().getElement().get("enterprise_id").split("\\.")[0]);

            Set<Long> ids = new HashSet<>();
            ids.add(oldRelationDO.getEnterpriseId());
            ids.add(newEId);
            ids.forEach(e->{
                enterpriseService.doSyncDataToEs(e);
            });
        } catch (Exception e) {
            log.error(String.format("ads更新操作数据同步出现异常，message: %s", message.toString()), e);
        }

    }

    public void delete(KafkaDataMessage message) {
        try {
            EnterpriseIndustryLabelRelationDO relationDO = enterpriseIndustryLabelRelationMapper.selectByIdWithDeleted(message.getData().getKey_id());

            billundAutoService.consumeMessage(message);

            enterpriseService.doSyncDataToEs(relationDO.getEnterpriseId());
        } catch (Exception e) {
            log.error(String.format("ads删除操作数据同步出现异常，message: %s", message.toString()), e);
        }
    }
}
