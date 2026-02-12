package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.UploadFileRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: WangZi
 * @Date: 2023/3/29
 * @Description: 文件上传记录mapper
 * @version: 1.0
 */
@Mapper
public interface UploadFileRecordMapper extends BaseMapper<UploadFileRecordDO> {
}
