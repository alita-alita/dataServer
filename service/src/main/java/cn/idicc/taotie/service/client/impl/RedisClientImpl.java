package cn.idicc.taotie.service.client.impl;

import cn.idicc.taotie.service.client.RedisClient;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.constant.NamespaceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangZi
 * @Date: 2023/4/17
 * @Description:  系统封装的redis操作类，做具体实现时需要添加命名空间前缀做环境隔离
 * @version: 1.0
 */
@Slf4j
@Component("redisClientInteger")
public class RedisClientImpl<K, V> implements RedisClient<K, V> {

    @Autowired
    private NamespaceProperties namespaceProperties;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void setByMap(String key, K hashKey, V value) {
        redisTemplate.opsForHash().put(namespaceProperties.getNamespaceRedisPrefix()+key, hashKey, value);
    }

    @Override
    public Long MapIncrement(String key,K hashKey,Long value){
        return redisTemplate.opsForHash().increment(namespaceProperties.getNamespaceRedisPrefix()+key,hashKey,value);
    }


    @Override
    public V getByMap(String key, K hashKey) {
        checkKey(key);
        return (V) redisTemplate.opsForHash().get(namespaceProperties.getNamespaceRedisPrefix()+key, hashKey);
    }

    @Override
    public Map getMap(String key){
        return redisTemplate.opsForHash().entries(namespaceProperties.getNamespaceRedisPrefix()+key);
    }

    @Override
    public Boolean delete(K key) {
        return redisTemplate.delete(namespaceProperties.getNamespaceRedisPrefix()+key);
    }

    @Override
    public Boolean deleteByString(String key) {
        checkKey(key);
        return redisTemplate.delete(namespaceProperties.getNamespaceRedisPrefix() + key);
    }

    @Override
    public void setByString(String key, String value) {
        checkKey(key);
        stringRedisTemplate.opsForValue().set(namespaceProperties.getNamespaceRedisPrefix() + key, value);
    }

    @Override
    public void setByString(String key, String value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(namespaceProperties.getNamespaceRedisPrefix() + key, value, time, timeUnit);
    }

    @Override
    public String getByString(String key) {
        checkKey(key);
        return stringRedisTemplate.opsForValue().get(namespaceProperties.getNamespaceRedisPrefix() + key);
    }

    @Override
    public Boolean hasKeyByString(String key) {
        checkKey(key);
        return stringRedisTemplate.hasKey(namespaceProperties.getNamespaceRedisPrefix() + key);
    }

    @Override
    public RLock getLock(String key) {
        RLock lock = redissonClient.getLock(namespaceProperties.getNamespaceRedisPrefix().concat(GlobalConstant.COLON).concat(key));
        return lock;
    }

    @Override
    public RBucket getBucket(String key) {
        return redissonClient.getBucket(namespaceProperties.getNamespaceRedisPrefix().concat(GlobalConstant.COLON).concat(key));
    }

    @Override
    public RKeys getKeys() {
        return redissonClient.getKeys();
    }

    private void checkKey(String key){
        if (StringUtils.isBlank(key)){
            throw new RuntimeException("传入key不能为空");
        }
    }
}
