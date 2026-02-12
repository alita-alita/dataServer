package cn.idicc.taotie.service.job;

import cn.idicc.taotie.provider.api.service.ChainNodeSyncRpcService;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndustryChainNodeSyncESJob {

    private static final Logger logger = LoggerFactory.getLogger(IndustryChainNodeSyncESJob.class);

    @Autowired
    private ChainNodeSyncRpcService chainNodeSyncRpcService;

    //产业链节点日更新
    @XxlJob("IndustryChainNodeSyncESJob")
    public void run() {
//获取所有已经上线的产业链
        MDC.put("requestId", "产业链企业同步至ES");
        try {
            String     chainIds = XxlJobHelper.getJobParam();
            JsonObject obj      = JsonParser.parseString(chainIds).getAsJsonObject();
            if(!obj.has("chainIds")){
                logger.error("【产业链任务】参数异常");
                return;
            }
            for (JsonElement chainId : obj.get("chainIds").getAsJsonArray()) {
                logger.info("【产业链任务】开始同步产业链企业：{}", chainId);
                chainNodeSyncRpcService.updateByChain(Long.valueOf(chainId.getAsString()));
                logger.info("【产业链任务】结束同步产业链企业：{}", chainId);
            }
        } catch (Exception e){
            logger.error("获取所有已经上线的产业链异常", e);
        } finally {
            MDC.remove("requestId");
        }

    }
}
