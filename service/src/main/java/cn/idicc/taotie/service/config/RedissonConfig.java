//package cn.idicc.taotie.service.config;
//
//import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author ckz
// * @date 2021/10/27 下午6:15
// */
//
///**
// * @ClassName RedissonConfig
// * @Description TODO  RedissonConfig配置类
// * @Author WangZi
// * @Date 2023/1/17 11:37
// * @Version 1.0
// **/
//@Configuration
//public class RedissonConfig {
//
//    @Autowired
//    private RedisProperties redisProperties;
//
//    @Bean
//    public RedissonClient getRedisson() {
//        Config config = new Config();
//        if (redisProperties.getCluster() != null) {
//            //集群模式配置
//            List<String> nodes = redisProperties.getCluster().getNodes();
//
//            List<String> clusterNodes = new ArrayList<>();
//            for (int i = 0; i < nodes.size(); i++) {
//                clusterNodes.add("redis://" + nodes.get(i));
//            }
//            config.useClusterServers()
//                    .addNodeAddress(clusterNodes.toArray(new String[0]))
//                    .setPassword(redisProperties.getPassword());
//
//        } else {
//            //单节点配置
//            String address = "redis://" + redisProperties.getHost() + GlobalConstant.COLON + redisProperties.getPort();
//            config.useSingleServer().setAddress(address)
//                    .setPassword(redisProperties.getPassword())
//                    .setDatabase(redisProperties.getDatabase());
//        }
//        return Redisson.create(config);
//    }
//}
