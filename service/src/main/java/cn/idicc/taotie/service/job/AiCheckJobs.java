package cn.idicc.taotie.service.job;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAiCheckRecordDO;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckStateEnum;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAiCheckRecordMapper;
import cn.idicc.taotie.service.services.icm.RecordAiCheckRecordService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesAiCheckService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/4/8
 * @Description:
 * @version: 1.0
 */
@Component
@Slf4j
public class AiCheckJobs {

	@Autowired
	private RecordAiCheckRecordService recordAiCheckRecordService;

	@Autowired
	private RecordAiCheckRecordMapper recordAiCheckRecordMapper;

	@Autowired
	private RecordProductMatchesAiCheckService productMatchesAiCheckService;

	@XxlJob("checkAiCheckRecordStatus")
	public void checkAiCheckRecordStatus() {
		List<RecordAiCheckRecordDO> recordDOS = recordAiCheckRecordMapper.selectList(Wrappers.lambdaQuery(RecordAiCheckRecordDO.class)
				.eq(RecordAiCheckRecordDO::getStatus, RecordAiCheckStateEnum.RUNNING.getCode()));
		for (RecordAiCheckRecordDO recordDO : recordDOS) {
			//check
			//TODO 要修改
//            Long count = productMatchesAiCheckService.countByVersion(recordDO.getVersion(), Arrays.asList(
//                    RecordAiCheckRecordStateEnum.DEFAULT,
//                    RecordAiCheckRecordStateEnum.RUNNING));
//            if (count == 0L){
//                //待人工确认
//                recordAiCheckRecordMapper.updateStatus(recordDO.getId(),RecordAiCheckStateEnum.WAITING_MANUAL.getCode());
//            }
		}
	}

	@XxlJob("aiCheckMatchesReRun")
	public void aiCheckMatchesReRun() {
//        Long count = recordAiCheckRecordMapper.selectCount(Wrappers.lambdaQuery(RecordAiCheckRecordDO.class)
//                .eq(RecordAiCheckRecordDO::getStatus, RecordAiCheckStateEnum.RUNNING.getCode()));
//        if (count != 0){
//            log.info("存在运行中质检任务，跳过巡检");
//            return;
//        }
//        List<Long> chainIds =recordProductMatchesMapper.getExistsChainIds();
//        Date sevenDaysBefore = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
//        for (Long chainId:chainIds){
//            RecordAiCheckRecordDO aiCheckRecordDO = recordAiCheckRecordMapper.selectOne(Wrappers.lambdaQuery(RecordAiCheckRecordDO.class)
//                    .eq(RecordAiCheckRecordDO::getChainId, chainId)
//                    .orderByDesc(RecordAiCheckRecordDO::getCompleteDate)
//            );
//            if (aiCheckRecordDO == null || aiCheckRecordDO.getCompleteDate().before(sevenDaysBefore)){
//                RecordAiCheckRecordDO recordDO = recordAiCheckRecordService.reRun(chainId);
//                recordAiCheckRecordService.runRecord(recordDO.getId());
//                log.info("{}巡检开始,任务ID:{}",chainId,recordDO.getId());
//                break;
//            }
//        }
	}
}
