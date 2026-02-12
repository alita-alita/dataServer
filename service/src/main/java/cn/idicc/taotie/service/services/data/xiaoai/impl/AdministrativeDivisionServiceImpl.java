package cn.idicc.taotie.service.services.data.xiaoai.impl;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.pangu.dto.AdministrativeDivisionDTO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.AdministrativeDivisionDO;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.AdministrativeDivisionMapper;
import cn.idicc.taotie.service.client.RedisClient;
import cn.idicc.taotie.service.services.data.xiaoai.AdministrativeDivisionService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/1/4
 * @Description: 行政区划实现层
 * @version: 1.0
 */
@Slf4j
@Service
public class AdministrativeDivisionServiceImpl extends ServiceImpl<AdministrativeDivisionMapper, AdministrativeDivisionDO> implements AdministrativeDivisionService {

    @Resource
    private AdministrativeDivisionMapper administrativeDivisionMapper;
    @Resource(name = "redisClientInteger")
    private RedisClient<String, Integer> redisClient;

    @Override
    public List<AdministrativeDivisionDTO> getAll() {
        // 从缓存中获取数据
        String key = "administrativeDivisionToRedis.getAllAreaData";
        List<AdministrativeDivisionDTO> administrativeDivisionDTOS = null;
        try {
            String administrativeDivisionJson = redisClient.getByString(key);
            if (administrativeDivisionJson != null) {
                administrativeDivisionDTOS = JSONObject.parseArray(administrativeDivisionJson, AdministrativeDivisionDTO.class);
            }
        } catch (Exception e) {
            log.error("查询es失败", e);
        }
        if (administrativeDivisionDTOS == null) {
            List<AdministrativeDivisionDO> administrativeDivisionDOS = administrativeDivisionMapper.selectList(Wrappers.lambdaQuery(AdministrativeDivisionDO.class));
            administrativeDivisionDTOS = BeanUtil.copyList(administrativeDivisionDOS, AdministrativeDivisionDTO.class);
            redisClient.setByString(key, JSONObject.toJSONString(administrativeDivisionDTOS));
        }
        return administrativeDivisionDTOS;
    }

    @Override
    public void clearRedis() {
        String key = "administrativeDivisionToRedis.getAllAreaData";
        redisClient.deleteByString(key);
    }
}
