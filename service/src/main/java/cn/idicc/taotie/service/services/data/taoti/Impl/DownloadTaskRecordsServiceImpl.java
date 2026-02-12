package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.entity.spider.DownloadTaskRecords;
import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.mapper.spider.DownloadTaskRecordsMapper;
import cn.idicc.taotie.infrastructment.request.data.DownloadTaskRecordsReq;
import cn.idicc.taotie.service.services.data.taoti.DownloadTaskRecordsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Log4j2
@Service
public class DownloadTaskRecordsServiceImpl implements DownloadTaskRecordsService {
    @Autowired
    private DownloadTaskRecordsMapper downloadTaskRecordsMapper;

    @Override
    public Integer addDownloadTaskRecords(DownloadTaskRecords downloadTaskRecords) {
        if (downloadTaskRecords == null){
            log.error("参数为空，无法添加");
            return 0;
        }
        try {
            Integer addDownloadTaskRecords = downloadTaskRecordsMapper.addDownloadTaskRecords(downloadTaskRecords);
            if (addDownloadTaskRecords == 0 ) {
                log.error("添加失败，请检查参数是否正确");
                return 0;
            }
            return addDownloadTaskRecords;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("添加失败，请检查参数是否正确");
            return 0;
        }
    }

    @Override
    public PageInfo<DownloadTaskRecords> listDownloadTaskRecords(DownloadTaskRecordsReq downloadTaskRecordsReq) {
        PageHelper.startPage(downloadTaskRecordsReq.getPageNum(),downloadTaskRecordsReq.getPageSize());
        List<DownloadTaskRecords> downloadTaskRecords = downloadTaskRecordsMapper.listDownloadTaskRecords();
        PageInfo<DownloadTaskRecords> downloadTaskRecordsPageInfo = new PageInfo<>(downloadTaskRecords);
        return downloadTaskRecordsPageInfo;
    }

    @Override
    public Integer delLogicDownloadTaskRecords(DownloadTaskRecords downloadTaskRecords) {
        Integer logicDownloadTaskRecords = downloadTaskRecordsMapper.delLogicDownloadTaskRecords(Math.toIntExact(downloadTaskRecords.getDownloadTaskRecordsId()));
        if (logicDownloadTaskRecords == 0){
            return 0;
        }
        return logicDownloadTaskRecords;

    }
}
