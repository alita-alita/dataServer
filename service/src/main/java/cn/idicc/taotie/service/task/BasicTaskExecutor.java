package cn.idicc.taotie.service.task;

import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskInstanceDO;
import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskInstanceLogDO;
import cn.idicc.taotie.infrastructment.mapper.icm.SyncTaskInstanceLogMapper;
import cn.idicc.taotie.service.task.logs.TaskLogDTO;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: MengDa
 * @Date: 2025/2/11
 * @Description:
 * @version: 1.0
 */
@Slf4j
public abstract class BasicTaskExecutor {

    @Resource(name = "commonHttpClient")
    private OkHttpClient client;

    @Autowired
    private SyncTaskInstanceLogMapper syncTaskInstanceLogMapper;

    @Value("${task.python.api:http://127.0.0.1:8500}")
    private String basicURL;

    @Value("${system.env}")
    private String topicEnv;
    protected static final Integer STATUS_WAIT_EXECUTE = 0;
    protected static final Integer STATUS_EXECUTING = 1;
    protected static final Integer STATUS_EXECUTE_SUCCESS = 2;
    protected static final Integer STATUS_EXECUTE_FAIL = 3;

    protected Integer saveAndWaitForEnd(SyncTaskInstanceDO instanceDO, String jobId) {
        SyncTaskInstanceLogDO instanceLogDO = new SyncTaskInstanceLogDO();
        instanceLogDO.setTaskId(instanceDO.getTaskId());
        instanceLogDO.setInstanceId(instanceDO.getId());
        instanceLogDO.setJobId(jobId);
        syncTaskInstanceLogMapper.insert(instanceLogDO);
        Integer status = STATUS_WAIT_EXECUTE;
        while (true) {
            status = checkStatus(jobId);
            instanceLogDO.setStatus(status);
            syncTaskInstanceLogMapper.updateById(instanceLogDO);
            if (Objects.equals(STATUS_WAIT_EXECUTE, status)) {
                log.info("task:{} is still wait for execute", jobId);
                break;
            } else if (Objects.equals(STATUS_EXECUTING, status)) {
                //正在执行，等待
            } else if (Objects.equals(STATUS_EXECUTE_SUCCESS, status)) {
                log.info("task:{} finished", jobId);
                List<TaskLogDTO> logDTOS = getLogs(jobId, 0, null);
                instanceLogDO.setJobLog(JSON.toJSONString(logDTOS));
                syncTaskInstanceLogMapper.updateById(instanceLogDO);
                break;
            } else if (Objects.equals(STATUS_EXECUTE_FAIL, status)) {
                log.info("task:{} fail", jobId);
                List<TaskLogDTO> logDTOS = getLogs(jobId, 0, null);
                instanceLogDO.setJobLog(JSON.toJSONString(logDTOS));
                syncTaskInstanceLogMapper.updateById(instanceLogDO);
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("wait for task end error", e);
            }
        }
        return status;
    }

    protected String syncOds(String bizInfo, String env, List<String> tables) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("tables", tables);
        }};
        return submitTask(ScriptTaskConstants.SYNC_TO_ODS, bizInfo, param);
    }

    protected String syncEnterprise(String bizInfo, String env, String date) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("gmt_modify", date);
        }};
        return submitTask(ScriptTaskConstants.ENTERPRISE, bizInfo, param);
    }

    protected String syncEnterpriseIndustryLabelRelation(String bizInfo, String env, List<Long> chains) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("chains", chains);
        }};
        return submitTask(ScriptTaskConstants.ENTERPRISE_INDUSTRY_LABEL, bizInfo, param);
    }

    protected String syncRankingListBatch(String bizInfo, String env, List<Long> chains, List<Integer> metrics) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("chains", chains);
            put("metrics", metrics);
        }};
        return submitTask(ScriptTaskConstants.RANKING_LIST_12_19, bizInfo, param);
    }

    protected String syncRankingList1_11_20(String bizInfo, String env) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
        }};
        return submitTask(ScriptTaskConstants.RANKING_LIST_1_11_20, bizInfo, param);
    }

    protected String syncEnterpriseLabelTech(String bizInfo, String env) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
        }};
        return submitTask(ScriptTaskConstants.ENTERPRISE_CORRELATION_LABEL_TECH, bizInfo, param);
    }

    protected String syncEnterpriseLabelListed(String bizInfo, String env) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
        }};
        return submitTask(ScriptTaskConstants.ENTERPRISE_CORRELATION_LABEL_LISTED, bizInfo, param);
    }

    protected String syncEnterpriseLabelFinancing(String bizInfo, String env) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
        }};
        return submitTask(ScriptTaskConstants.ENTERPRISE_CORRELATION_LABEL_FINANCING, bizInfo, param);
    }

    protected String syncPatentInnovation(String bizInfo, String env, List<Long> chains) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("chains", chains);
        }};
        return submitTask(ScriptTaskConstants.PATENT_INNOVATION, bizInfo, param);
    }

    protected String syncIndustryInvestmentFlow(String bizInfo, String env, List<Long> chains) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("chains", chains);
        }};
        return submitTask(ScriptTaskConstants.INDUSTRY_INVESTMENT_FLOW, bizInfo, param);
    }

    protected String syncListedCompany(String bizInfo, String env, List<Long> chains) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("chains", chains);
        }};
        return submitTask(ScriptTaskConstants.LISTED_COMPANY, bizInfo, param);
    }

    protected String syncChainNodeAttributeAnalysis(String bizInfo, String env, List<Long> chains) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("chains", chains);
        }};
        return submitTask(ScriptTaskConstants.CHAIN_NODE_ATTRIBUTE_ANALYSIS, bizInfo, param);
    }

    protected String syncChainNodeAttribute(String bizInfo, String env, List<Long> chains) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("chains", chains);
        }};
        return submitTask(ScriptTaskConstants.CHAIN_NODE_ATTRIBUTE, bizInfo, param);
    }

    protected String syncEnterpriseXiaoAi(String bizInfo, String env, String date) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("gmt_modify", date);
        }};
        return submitTask(ScriptTaskConstants.ENTERPRISE_XIAO_AI, bizInfo, param);
    }

    protected String syncMessage(String bizInfo, String env, List<String> uniqueBatches, String partTime, String topic) {
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("env", env);
            put("unique_batches", uniqueBatches);
            put("part_time", partTime);
            put("topic", topic);
        }};
        return submitTask(ScriptTaskConstants.MESSAGE_SEND, bizInfo, param);
    }

    public String commonSubmitTask(String taskName, String bizInfo, Map<String, Object> param) {
        return submitTask(taskName, bizInfo, param);
    }

    private String submitTask(String type, String bizInfo, Map<String, Object> param) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("bizId", bizInfo);
        jsonObject.put("param", param);
        String url = basicURL + "/api/submit_task";
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url(url)
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonObject.toJSONString()))
                .build();
        try (Response resp = client.newCall(request).execute()) {
            if (resp.isSuccessful()) {
                String str = resp.body().string();
//                log.info("python task api:{} success, response:{}", url,str);
                JSONObject res = JSON.parseObject(str);
                return res.getString("jobId");
            } else {
                log.info("python task api:{} fail, response:{}", url, resp.toString());
            }
        } catch (Exception e) {
            log.error(String.format("python task api:%s error", url), e);
        }
        return null;
    }

    protected Integer checkStatus(String jobId) {
        String url = basicURL + "/api/query_task/" + jobId;
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url(url)
                .build();
        try (Response resp = client.newCall(request).execute()) {
            if (resp.isSuccessful()) {
                String str = resp.body().string();
//                log.info("python task api:{} success, response:{}", url,str);
                JSONObject res = JSON.parseObject(str);
                return res.getInteger("status");
            } else {
                log.info("python task api:{} fail, response:{}", url, resp.toString());
            }
        } catch (Exception e) {
            log.error(String.format("python task api:%s error", url), e);
        }
        return null;
    }

    public List<TaskLogDTO> getLogs(String jobId, Integer offset, String type) {
        if (offset == null) {
            offset = 0;
        }
        String url = basicURL + String.format("/api/query_logs?job_id=%s&sort=%s", jobId, offset);
        if (type != null) {
            url = url + "&type=" + type;
        }
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url(url)
                .build();
        try (Response resp = client.newCall(request).execute()) {
            if (resp.isSuccessful()) {
                String str = resp.body().string();
//                log.info("python task api:{} success, response:{}", url,str);
                JSONObject res = JSON.parseObject(str);
                List<TaskLogDTO> logDTOS = res.getList("logs", TaskLogDTO.class);
                logDTOS.sort(new Comparator<TaskLogDTO>() {
                    @Override
                    public int compare(TaskLogDTO o1, TaskLogDTO o2) {
                        return o1.getSort().compareTo(o2.getSort());
                    }
                });
                return logDTOS;
            } else {
                log.info("python task api:{} fail, response:{}", url, resp.toString());
            }
        } catch (Exception e) {
            log.error(String.format("python task api:%s error", url), e);
        }
        return null;
    }

}
