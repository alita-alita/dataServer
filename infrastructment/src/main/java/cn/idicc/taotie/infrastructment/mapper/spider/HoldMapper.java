package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.Hold;
import cn.idicc.taotie.infrastructment.entity.spider.HoldKafka;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsite;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsiteKafka;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HoldMapper {

    List<Hold> listHold(@Param("holderName") String holderName,
                        @Param("holderState") Integer holderState);


    //核对发送  按照企业名称主键进行发送
    List<HoldKafka> manualGather(@Param("holdIds") List<Long> holdIds);


    List<Hold> findByCollectTime(String holderNextTime);

    Integer addHold(Hold hold);

    Integer addHoldBatch(List<Hold> holdDataList);

    Integer updateHoldTime(@Param("HoldId")Integer HoldId);

    Integer delLogicHold(Integer holdId);



}
