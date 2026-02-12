package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.entity.spider.CompressionFileStatus;
import cn.idicc.taotie.infrastructment.entity.spider.CompressionStatus;
import cn.idicc.taotie.infrastructment.entity.spider.FileStatus;
import cn.idicc.taotie.infrastructment.request.data.CompressionFileReq;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompressionFileStatusService {
    /**
     * 压缩包列表
     * @return
     */
    PageInfo<CompressionStatus> listCompressionStatus(CompressionFileReq compressionFileReq);
    /**
     * 对应压缩包-文件列表
     * @return
     */
    PageInfo<CompressionFileStatus> listCompressionFileStatus(CompressionFileReq compressionFileReq);
    /**
     * 获取文件的错误日志信息
     * @param fileStatusid
     * @return
     */
    List<FileStatus> listFileStatus(FileStatus fileStatusid);

    /**
     * 添加压缩包信息
     * @param compressionStatus
     * @return
     */

    Integer addCompressionStatus(CompressionStatus compressionStatus);

    /**
     * 添加文件信息
     * @param fileStatus
     * @return
     */
    Integer addFileStatus(FileStatus fileStatus);

    /**
     * 删除压缩包信息
     * @param compressionStatusid
     * @return
     */
    Integer updLogicCompressionStatus(CompressionStatus compressionStatusid);





}
