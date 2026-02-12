package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.entity.spider.CompressionFileStatus;
import cn.idicc.taotie.infrastructment.entity.spider.CompressionStatus;
import cn.idicc.taotie.infrastructment.entity.spider.FileStatus;
import cn.idicc.taotie.infrastructment.mapper.spider.CompressionFileStatusMapper;
import cn.idicc.taotie.infrastructment.request.data.CompressionFileReq;
import cn.idicc.taotie.service.services.data.taoti.CompressionFileStatusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Log4j2
@Service
public class CompressionFileStatusServiceImpl implements CompressionFileStatusService {
    @Autowired
    private CompressionFileStatusMapper compressionFileStatusMapper;


    @Override
    public PageInfo<CompressionStatus> listCompressionStatus(CompressionFileReq compressionFileReq) {
        PageHelper.startPage(compressionFileReq.getPageNum(),compressionFileReq.getPageSize());
        List<CompressionStatus> compressionStatuses = compressionFileStatusMapper.listCompressionStatus(compressionFileReq.getPackageStatus(), compressionFileReq.getFileStatus(), compressionFileReq.getDataMarket());
        PageInfo<CompressionStatus> listPageInfo = new PageInfo<>(compressionStatuses);
        return listPageInfo;
    }

    @Override
    public PageInfo<CompressionFileStatus> listCompressionFileStatus(CompressionFileReq compressionFileReq) {
        PageHelper.startPage(compressionFileReq.getPageNum(),compressionFileReq.getPageSize());
        List<CompressionFileStatus> compressionFileStatuses = compressionFileStatusMapper.listCompressionFileStatus(compressionFileReq.getFileCompressionId());
        PageInfo<CompressionFileStatus> compressionFileStatusPageInfo = new PageInfo<>(compressionFileStatuses);
        return  compressionFileStatusPageInfo;
    }

    @Override
    public List<FileStatus> listFileStatus(FileStatus fileStatusid) {
        return compressionFileStatusMapper.listFileStatus(Math.toIntExact(fileStatusid.getFileStatusId()));
    }

    @Override
    public Integer addCompressionStatus(CompressionStatus compressionStatus) {
        if(compressionStatus!=null){
            return 0;
        }

        try {
            Integer addCompressionStatus = compressionFileStatusMapper.addCompressionStatus(compressionStatus);
            if (addCompressionStatus == 0 ) {
                log.error("添加关键词失败，请检查参数是否正确");
                return 0;
            }
            return addCompressionStatus;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("添加关键词失败，请检查参数是否正确");
            return 0;
        }

    }

    @Override
    public Integer addFileStatus(FileStatus fileStatus) {
        if(fileStatus!=null){
            return 0;
        }

        try {
            Integer addFileStatus = compressionFileStatusMapper.addFileStatus(fileStatus);
            if (addFileStatus == 0 ) {
                log.error("添加关键词失败，请检查参数是否正确");
                return 0;
            }
            return addFileStatus;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("添加关键词失败，请检查参数是否正确");
            return 0;
        }
    }

    @Override
    public Integer updLogicCompressionStatus(CompressionStatus compressionStatusid) {
        Integer updLogicCompressionStatus = compressionFileStatusMapper.updLogicCompressionStatus(Math.toIntExact(compressionStatusid.getCompressionStatusId()));
        if (updLogicCompressionStatus == 0){
            return 0;
        }
        return updLogicCompressionStatus;
    }
}
