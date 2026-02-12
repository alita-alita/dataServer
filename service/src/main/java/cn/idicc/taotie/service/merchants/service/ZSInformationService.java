package cn.idicc.taotie.service.merchants.service;


import cn.idicc.taotie.infrastructment.entity.data.InformationDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/5/31
 * @Description:
 * @version: 1.0
 */
public interface ZSInformationService extends IService<InformationDO> {

    /**
     * 通过资讯url获取资讯信息
     *
     * @param url
     * @return
     */
    InformationDO getByUrl(String url);

    void syncToEsById(Long id);
}
