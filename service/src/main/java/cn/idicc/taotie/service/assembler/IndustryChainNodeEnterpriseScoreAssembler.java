package cn.idicc.taotie.service.assembler;

import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeEnterpriseScoreDO;
import cn.idicc.taotie.service.kafka.data.message.UpdateIndustryChainNodeEnterpriseScoreMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wd
 * @description
 * @date 12/19/22 3:28 PM
 */
@Mapper
public interface IndustryChainNodeEnterpriseScoreAssembler {

    IndustryChainNodeEnterpriseScoreAssembler INSTANCE =  Mappers.getMapper(IndustryChainNodeEnterpriseScoreAssembler.class);

    /**
     * DOs转messages
     * @param enterpriseScoreDOS
     * @return
     */
    List<UpdateIndustryChainNodeEnterpriseScoreMessage> listDOToListMessage(List<IndustryChainNodeEnterpriseScoreDO> enterpriseScoreDOS);

    /**
     * message转dos
     * @param messages
     * @return
     */
    List<IndustryChainNodeEnterpriseScoreDO> messageListToDOs(List<UpdateIndustryChainNodeEnterpriseScoreMessage> messages);

}
