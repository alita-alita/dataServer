package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.MyCollectDataDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: wd
 * @Date: 2023/4/19
 * @Description: 我收藏的资讯/我关注的企业
 * @version: 1.0
 */
public interface MyCollectDataService extends IService<MyCollectDataDO> {
    /**
     * 查询企业关联的关注数据
     * @param enterpriseIds
     * @param collectDataType
     * @return
     */
    List<MyCollectDataDO> listByEnterpriseIds(List<Long> enterpriseIds,Integer collectDataType);

}
