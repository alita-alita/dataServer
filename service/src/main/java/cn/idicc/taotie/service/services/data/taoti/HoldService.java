package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.entity.spider.Hold;
import cn.idicc.taotie.infrastructment.entity.spider.HoldKafka;
import cn.idicc.taotie.infrastructment.request.data.HoldReq;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HoldService {


    PageInfo<Hold> listHold(HoldReq holdReq);


    //核对发送  按照企业名称主键进行发送
    List<HoldKafka> manualGather(List<Long> holdIds);


    Integer addHold(Hold hold);

    Integer addHoldBatch(List<Hold> holdDataList);

    Integer updateHoldTime(Integer HoldId);

    Integer delLogicHold(Hold hold);

}
