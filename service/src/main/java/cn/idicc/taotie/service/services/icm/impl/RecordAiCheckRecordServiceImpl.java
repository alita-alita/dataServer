package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.common.exception.BusinessException;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAgentProductMatchesDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAiCheckRecordDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckRecordStateEnum;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckStateEnum;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAgentProductMatchesMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAiCheckRecordMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordProductMatchesMapper;
import cn.idicc.taotie.infrastructment.request.icm.RecordAiCheckRecordQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordAiCheckRecordDTO;
import cn.idicc.taotie.service.services.icm.DwSyncService;
import cn.idicc.taotie.service.services.icm.RecordAiCheckRecordService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesAiCheckService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2025/4/2
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class RecordAiCheckRecordServiceImpl implements RecordAiCheckRecordService {

	@Autowired
	private RecordAiCheckRecordMapper recordAiCheckRecordMapper;

	@Autowired
	private RecordIndustryChainMapper industryChainMapper;

	@Autowired
	private DwSyncService dwSyncService;

	@Autowired
	private RecordProductMatchesAiCheckService productMatchesAiCheckService;

	@Autowired
	private RecordProductMatchesService productMatchesService;

	@Autowired
	private RecordProductMatchesMapper recordProductMatchesMapper;

	@Resource(name = "executor-icm")
	private ExecutorService                    executor;
	@Autowired
	private RecordAgentProductMatchesMapper    recordAgentProductMatchesMapper;
	@Autowired
	private RecordProductMatchesAiCheckService recordProductMatchesAiCheckService;
	@Autowired
	private RecordIndustryChainMapper          recordIndustryChainMapper;

	@Override
	public IPage<RecordAiCheckRecordDTO> pageList(RecordAiCheckRecordQueryRequest request) {
		IPage<RecordAiCheckRecordDTO> res = recordAiCheckRecordMapper.page(
				Page.of(request.getPageNum(), request.getPageSize()),
				request.getChainId()
		);
//        res.getRecords().forEach(e->{
//            e.setStatus(RecordAiCheckStateEnum.getByCode(Integer.valueOf(e.getStatus())).getDesc());
//        });
		return res;
	}

	@Override
	public RecordAiCheckRecordDO addRecordAiCheckRecord(Long chainId, String desc) {

		RecordIndustryChainDO chainDO  = industryChainMapper.selectByBizId(chainId);
		RecordAiCheckRecordDO recordDO = new RecordAiCheckRecordDO();
		recordDO.setChainId(chainDO.getBizId());
		recordDO.setChainName(chainDO.getChainName());
		recordDO.setRunDesc(desc);
		recordDO.setType(0);
		recordAiCheckRecordMapper.insert(recordDO);
		return recordDO;
	}


	@Override
	public void runRecord(Long id) {
		RecordAiCheckRecordDO        recordDO   = recordAiCheckRecordMapper.selectById(id);
		List<RecordAiCheckStateEnum> allowState = RecordAiCheckStateEnum.getAllowRunning();
		RecordAiCheckStateEnum       stateEnum  = RecordAiCheckStateEnum.getByCode(recordDO.getStatus());
		if (stateEnum == null || !allowState.contains(stateEnum)) {
			throw new BizException(String.format("仅%s状态的质检单允许启动", allowState.stream().map(RecordAiCheckStateEnum::getDesc).collect(Collectors.joining("、"))));
		}

		Long runCount = recordAiCheckRecordMapper.selectCount(Wrappers.lambdaQuery(RecordAiCheckRecordDO.class)
				.eq(RecordAiCheckRecordDO::getChainId, recordDO.getChainId())
				.in(RecordAiCheckRecordDO::getStatus, Arrays.asList(RecordAiCheckStateEnum.RUNNING.getCode(), RecordAiCheckStateEnum.STARTING.getCode()))
		);
		if (runCount > 0) {
			throw new BizException("存在相同产业链已在运行中");
		}
		recordAiCheckRecordMapper.updateStatus(id, RecordAiCheckStateEnum.STARTING.getCode());
		executor.submit(() -> {
			try {
				while (true) {
					List<RecordAgentProductMatchesDO> agentProductMatchesDOS = recordAgentProductMatchesMapper.selectList(Wrappers.lambdaQuery(RecordAgentProductMatchesDO.class)
							.eq(RecordAgentProductMatchesDO::getIndustryChainId, recordDO.getChainId())
							.eq(RecordAgentProductMatchesDO::getStatus, RecordChainStateEnum.MATCH_ENTERPRISE_END.getValue())
							.eq(RecordAgentProductMatchesDO::getDeleted, false)
							.last("limit 1000")
					);
					if(agentProductMatchesDOS.isEmpty()){
						break;
					}
					Set<Long> ids = agentProductMatchesDOS.stream().map(RecordAgentProductMatchesDO::getId).collect(Collectors.toSet());
					//如果matched_product为空，则认为没有挂载上，那么过滤掉
					agentProductMatchesDOS = agentProductMatchesDOS.stream()
							.filter(e -> e.getMatchedProduct() != null).collect(Collectors.toList());
					if (!agentProductMatchesDOS.isEmpty()) {
						// 构建AI质检任务
						recordProductMatchesAiCheckService.createAICheckTask(recordDO.getChainId(), agentProductMatchesDOS);
					}


					//更新记录为待质检
					recordAgentProductMatchesMapper.update(null,
							Wrappers.lambdaUpdate(RecordAgentProductMatchesDO.class)
									.set(RecordAgentProductMatchesDO::getStatus, RecordChainStateEnum.WAITING_AI_QUALIFICATION.getValue())
									.eq(RecordAgentProductMatchesDO::getStatus, RecordChainStateEnum.MATCH_ENTERPRISE_END.getValue())
									.in(RecordAgentProductMatchesDO::getId, ids));
				}
				recordAiCheckRecordMapper.updateStatus(id, RecordAiCheckStateEnum.RUNNING.getCode());
				//更新产业链状态为AI检测中
				recordIndustryChainMapper.update(null,
						Wrappers.lambdaUpdate(RecordIndustryChainDO.class)
								.set(RecordIndustryChainDO::getState, RecordChainStateEnum.AI_QUALIFICATION_PROCESS.getValue())
								.eq(RecordIndustryChainDO::getBizId, recordDO.getChainId()));
			} catch (Exception e) {
				log.error("Record Ai Check Insert Fail,e", e);
				recordAiCheckRecordMapper.updateStatus(id, RecordAiCheckStateEnum.ERROR.getCode());
			}
		});

	}

	@Override
	public void stopRecord(Long id) {
		RecordAiCheckRecordDO        recordDO   = recordAiCheckRecordMapper.selectById(id);
		List<RecordAiCheckStateEnum> allowState = RecordAiCheckStateEnum.getAllowStop();
		RecordAiCheckStateEnum       stateEnum  = RecordAiCheckStateEnum.getByCode(recordDO.getStatus());
		if (stateEnum == null || !allowState.contains(stateEnum)) {
			throw new BizException(String.format("仅%s版本允许停止", allowState.stream().map(RecordAiCheckStateEnum::getDesc).collect(Collectors.joining("、"))));
		}
		recordAiCheckRecordMapper.updateStatus(id, RecordAiCheckStateEnum.STOP.getCode());
		executor.submit(() -> {
			//TODO 要修改
//            productMatchesAiCheckService.deleteByVersion(recordDO.getVersion());
		});
	}

	@Override
	public void aiCheckDataOnline(Long chainId, Boolean isCheck) {
		RecordAiCheckRecordDO recordDO = recordAiCheckRecordMapper.selectOne(
				Wrappers.lambdaQuery(RecordAiCheckRecordDO.class)
						.eq(RecordAiCheckRecordDO::getChainId, chainId)
						.orderByDesc(RecordAiCheckRecordDO::getId)
						.last("limit 1")
		);
		RecordAiCheckStateEnum stateEnum = RecordAiCheckStateEnum.getByCode(recordDO.getStatus());
		if (!RecordAiCheckStateEnum.WAITING_MANUAL.equals(stateEnum)) {
			throw new BizException("不符合预期的状态");
		}
		if (isCheck) {
			Long count = productMatchesAiCheckService.countByChainId(recordDO.getChainId(), Arrays.asList(
					RecordAiCheckRecordStateEnum.DEFAULT,
					RecordAiCheckRecordStateEnum.NOT_PASS,
					RecordAiCheckRecordStateEnum.RUNNING));
			if (count != 0L) {
				throw new BizException("存在尚未完成的记录");
			}
		}
		//更新质检单状态为已完成
		recordAiCheckRecordMapper.updateStatus(recordDO.getId(), RecordAiCheckStateEnum.COMPLETE.getCode());
		recordAiCheckRecordMapper.updateCompleteDate(recordDO.getId(), new Date());
//		executor.submit(() -> {
//			//并存储入下一阶段库``
//			//TODO 要修改
////			productMatchesService.insertRecord(recordDO.getChainId());
//			//complete
//			recordAiCheckRecordMapper.updateStatus(recordDO.getId(), RecordAiCheckStateEnum.COMPLETE.getCode());
//			recordAiCheckRecordMapper.updateCompleteDate(recordDO.getId(), new Date());
//		});
	}
}
