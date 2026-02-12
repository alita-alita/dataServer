package cn.idicc.taotie.service.services.data.pangu.impl;


import cn.idicc.taotie.infrastructment.mapper.data.UploadFileRecordMapper;
import cn.idicc.taotie.infrastructment.entity.data.UploadFileRecordDO;
import cn.idicc.taotie.infrastructment.enums.UploadFileStatusEnum;
import cn.idicc.taotie.service.services.data.pangu.UploadFileRecordService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: WangZi
 * @Date: 2023/3/29
 * @Description: 文件上传记录实现层
 * @version: 1.0
 */
@Slf4j
@Service
public class UploadFileRecordServiceImpl extends ServiceImpl<UploadFileRecordMapper, UploadFileRecordDO> implements UploadFileRecordService {

    @Autowired
    private UploadFileRecordMapper uploadFileRecordMapper;

    /**
     * 更新指定上传记录状态
     *
     * @param id
     * @param statusEnum
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, UploadFileStatusEnum statusEnum) {
        uploadFileRecordMapper.update(null,
                Wrappers.lambdaUpdate(new UploadFileRecordDO())
                        .eq(UploadFileRecordDO::getId, id)
                        .eq(UploadFileRecordDO::getStatus, UploadFileStatusEnum.TO_BE_PROCESSED.getCode())
                        .set(UploadFileRecordDO::getStatus, statusEnum.getCode()));
    }
}
