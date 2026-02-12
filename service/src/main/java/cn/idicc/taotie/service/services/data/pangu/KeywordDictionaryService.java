package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.KeywordDictionaryDO;
import cn.idicc.taotie.infrastructment.response.data.KeywordDictionaryDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 关键字词典接口层
 * @version: 1.0
 */
public interface KeywordDictionaryService extends IService<KeywordDictionaryDO> {

    /**
     * 获取指定id集合的关键字词典集合
     *
     * @param ids
     * @return
     */
    List<KeywordDictionaryDTO> listByIds(List<Long> ids);

}
