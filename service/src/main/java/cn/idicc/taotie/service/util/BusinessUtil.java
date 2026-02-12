package cn.idicc.taotie.service.util;

import cn.idicc.taotie.service.client.RedisClient;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.constant.NamespaceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: WangZi
 * @Date: 2023/8/17
 * @Description:
 * @version: 1.0
 */
@Component
public class BusinessUtil {

    @Autowired
    private NamespaceProperties namespaceProperties;

    @Resource(name = "redisClientInteger")
    private RedisClient<String,Integer> redisClient;

    public Boolean getBatchLatest(String batch){
        String uniqueBatch = batch.substring(0+namespaceProperties.getPrefix().length(),batch.lastIndexOf("_"));
        Integer now = Integer.valueOf(batch.substring(batch.lastIndexOf("_")+1));
        Integer latest = redisClient.getByMap(GlobalConstant.BATCH_COUNT_KEY,uniqueBatch);
        return now.equals(latest);
    }

    public void addBatchByRedis(String batch){
        redisClient.MapIncrement(batch,
                GlobalConstant.CONSUMED_RECORD,1L);
    }
}
