package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.taotie.infrastructment.mapper.data.MyCollectDataMapper;
import cn.idicc.taotie.infrastructment.entity.data.MyCollectDataDO;
import cn.idicc.taotie.service.services.data.pangu.MyCollectDataService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wd
 * @Date: 2023/5/6
 * @Description:我收藏的资讯/我关注的企业
 * @version: 1.0
 */
@Service
@Slf4j
public class MyCollectDataServiceImpl extends ServiceImpl<MyCollectDataMapper, MyCollectDataDO> implements MyCollectDataService {

    @Autowired
    MyCollectDataMapper myCollectDataMapper;

    @Override
    public List<MyCollectDataDO> listByEnterpriseIds(List<Long> enterpriseIds,Integer collectDataType) {
        return myCollectDataMapper.selectList(Wrappers.lambdaQuery(MyCollectDataDO.class)
                .in(MyCollectDataDO::getBusId,enterpriseIds)
                .eq(MyCollectDataDO::getBusType,collectDataType));
    }
}
