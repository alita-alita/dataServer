package cn.idicc.taotie.service.services.data.xiaoai;

import cn.idicc.pangu.dto.AdministrativeDivisionDTO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.AdministrativeDivisionDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/1/4
 * @Description: 行政区划接口层
 * @version: 1.0
 */
public interface AdministrativeDivisionService extends IService<AdministrativeDivisionDO> {

    /**
     * 获取所有行政区划数据
     *
     * @return
     */
    List<AdministrativeDivisionDTO> getAll();

    /**
     * 清除行政区划redis缓存
     */
    void clearRedis();

}
