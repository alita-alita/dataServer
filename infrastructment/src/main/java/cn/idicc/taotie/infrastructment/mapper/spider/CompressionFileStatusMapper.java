package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.CompressionFileStatus;
import cn.idicc.taotie.infrastructment.entity.spider.CompressionStatus;
import cn.idicc.taotie.infrastructment.entity.spider.FileStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompressionFileStatusMapper {

    /**
     * 压缩包列表
     * @return
     */
    List<CompressionStatus> listCompressionStatus(@Param("packageStatus") Integer packageStatus,
                                                  @Param("fileStatus") Integer fileStatus,
                                                  @Param("dataMarket") String dataMarket);
    /**
     * 对应压缩包-文件列表
     * @return
     */
    List<CompressionFileStatus> listCompressionFileStatus(@Param("fileCompressionId")Integer fileCompressionId);

    /**
     * 获取文件的错误日志信息
     * @param fileStatusId
     * @return
     */
    List<FileStatus> listFileStatus(@Param("fileStatusId")Integer fileStatusId);

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
     * @param compressionStatusId
     * @return
     */
    Integer updLogicCompressionStatus(Integer compressionStatusId);





}
