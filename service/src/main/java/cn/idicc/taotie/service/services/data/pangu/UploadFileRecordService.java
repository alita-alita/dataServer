package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.UploadFileRecordDO;
import cn.idicc.taotie.infrastructment.enums.UploadFileStatusEnum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: WangZi
 * @Date: 2023/3/29
 * @Description: 上传文件记录接口层
 * @version: 1.0
 */
public interface UploadFileRecordService extends IService<UploadFileRecordDO> {

    /**
     * 更新指定上传记录状态
     *
     * @param id
     * @param statusEnum
     */
    void updateStatus(Long id, UploadFileStatusEnum statusEnum);
}
