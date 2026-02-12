package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.mapper.spider.ConllectTaskRecordsMapper;
import cn.idicc.taotie.infrastructment.entity.spider.CollectTaskRecords;
import cn.idicc.taotie.service.services.data.taoti.ConllectTaskRecordsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConllectTaskRecordsServiceImpl implements ConllectTaskRecordsService {
    @Autowired
    private ConllectTaskRecordsMapper conllectTaskRecordsMapper;

    @Override
    public Integer addConllectTaskRecords(CollectTaskRecords collectTaskRecords) {
       return conllectTaskRecordsMapper.addConllectTaskRecords(collectTaskRecords);
    }



    @Override
    public List<CollectTaskRecords> listConllectTaskRecords(CollectTaskRecords collectTaskRecordsType) {
        return conllectTaskRecordsMapper.listConllectTaskRecords(collectTaskRecordsType.getCollectTaskRecordsType());

    }


}

