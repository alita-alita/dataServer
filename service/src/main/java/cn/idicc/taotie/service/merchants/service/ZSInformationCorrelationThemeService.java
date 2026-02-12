package cn.idicc.taotie.service.merchants.service;

import cn.idicc.taotie.infrastructment.entity.data.InformationCorrelationThemeDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description:
 * @version: 1.0
 */
public interface ZSInformationCorrelationThemeService extends IService<InformationCorrelationThemeDO> {

    /**
     * 查询资讯关联的新闻主题
     * @param informationIds
     * @return
     */
    Map<Long, List<String>> queryByInformationIds(List<Long> informationIds);
}
