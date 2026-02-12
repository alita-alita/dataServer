package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.entity.spider.Hold;
import cn.idicc.taotie.infrastructment.entity.spider.HoldKafka;
import cn.idicc.taotie.infrastructment.mapper.spider.HoldMapper;
import cn.idicc.taotie.infrastructment.request.data.HoldReq;
import cn.idicc.taotie.service.services.data.taoti.HoldService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Log4j2
@Service
public class HoldServiceImpl implements HoldService {
    private static final Integer FAIL_CODE = 0;
    @Autowired
    private HoldMapper holdMapper;
    @Override
    public PageInfo<Hold> listHold(HoldReq holdReq) {
        PageHelper.startPage(holdReq.getPageNum(),holdReq.getPageSize());
        List<Hold> holds = holdMapper.listHold(holdReq.holderName, holdReq.holderState);
        PageInfo<Hold> listPageInfo = new PageInfo<>(holds);
        return listPageInfo;
    }

    @Override
    public List<HoldKafka> manualGather(List<Long> holdIds) {
        return holdMapper.manualGather(holdIds);
    }

    @Override
    public Integer addHold(Hold hold) {
        if (hold == null){
            log.error("参数为空，无法添加关键词");
            return FAIL_CODE;
        }
        try {
            Integer addHold = holdMapper.addHold(hold);
            if (addHold.equals(FAIL_CODE)) {
                log.error("添加关键词失败，请检查参数是否正确");
                return FAIL_CODE;
            }
            return addHold;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("添加关键词失败，请检查参数是否正确");
            return FAIL_CODE;
        }
    }

    @Override
    public Integer addHoldBatch(List<Hold> holdDataList) {
        return holdMapper.addHoldBatch(holdDataList);
    }

    @Override
    public Integer updateHoldTime(Integer HoldId) {
        return holdMapper.updateHoldTime(HoldId);
    }

    @Override
    public Integer delLogicHold(Hold hold) {
        if (hold.getHoldId() == null){
            log.error("参数为空，无法添加关键词");
            return 0;
        }
        try {
            Integer delLogicHold = holdMapper.delLogicHold(Math.toIntExact(hold.getHoldId()));
            if (delLogicHold == 0 ) {
                log.error("逻辑删除失败，请检查参数是否正确");
                return 0;
            }
            return delLogicHold;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("逻辑删除失败，请检查参数是否正确");
            return 0;
        }
    }
}
