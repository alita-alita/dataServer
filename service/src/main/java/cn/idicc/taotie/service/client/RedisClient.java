package cn.idicc.taotie.service.client;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangZi
 * @Date: 2023/4/17
 * @Description: redis操作客户端类
 * @version: 1.0
 */
public interface RedisClient<K, V> {

    void setByMap(String key, K hashKey, V value);

    V getByMap(String key, K hashKey);

    Map getMap(String key);

    Long MapIncrement(String key,K hashKey,Long value);

    Boolean delete(K key);

    Boolean deleteByString(String key);

    void setByString(String key, String value);

    void setByString(String key, String value, Long time, TimeUnit timeUnit);

    String getByString(String key);

    Boolean hasKeyByString(String key);

    RLock getLock(String key);

    RBucket getBucket(String key);

    RKeys getKeys();


}
