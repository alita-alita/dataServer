package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.RecordBlacklistKeywordsDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.*;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesAiCheckQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesAiCheckDTO;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2025/4/2
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class RecordProductMatchesServiceImpl implements RecordProductMatchesService {


	@Autowired
	private RecordProductMatchesMapper recordProductMatchesMapper;

	@Autowired
	private RecordBlacklistKeywordsMapper              recordBlacklistKeywordsMapper;
	@Autowired
	private RecordIndustryLabelMapper                  recordIndustryLabelMapper;
	@Autowired
	private RecordIndustryChainNodeMapper              recordIndustryChainNodeMapper;

	@Autowired
	private ChainNodeRefAtomNodeMapper chainNodeRefAtomNodeMapper;


	@Override
	public Integer updateRecord(RecordProductMatchesAiCheckDTO aiCheckDTO) {
		if (aiCheckDTO.getId() == null) {
			throw new BizException("ID为空");
		}
		if (aiCheckDTO.getVersion() == null) {
			throw new BizException("版本为空");
		}
		if (aiCheckDTO.getLabelId() == null) {
			throw new BizException("标签ID为空");
		}
		if (aiCheckDTO.getNodeId() == null) {
			throw new BizException("节点ID为空");
		}
		RecordIndustryLabelDO label = recordIndustryLabelMapper.selectList(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
				.eq(RecordIndustryLabelDO::getBizId, aiCheckDTO.getLabelId())
		).stream().findFirst().orElse(null);
		RecordIndustryChainNodeDO node = recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getBizId, aiCheckDTO.getNodeId())
		).stream().findFirst().orElse(null);
		;
		if (label == null || node == null) {
			throw new BizException("不存在的节点或标签");
		}
		Long count = chainNodeRefAtomNodeMapper.countByNodeIdsAndLabelIds(Sets.newHashSet(aiCheckDTO.getLabelId()),
				Sets.newHashSet(aiCheckDTO.getNodeId()));
		if (count == 0) {
			throw new BizException("节点和标签不存在关联关系");
		}
		return recordProductMatchesMapper.update(null, Wrappers.lambdaUpdate(RecordProductMatchesDO.class)
				.eq(RecordProductMatchesDO::getId, aiCheckDTO.getId())
				.set(RecordProductMatchesDO::getLabelId, aiCheckDTO.getLabelId())
				.set(RecordProductMatchesDO::getLabelName, label.getLabelName())
				.set(RecordProductMatchesDO::getNodeId, aiCheckDTO.getNodeId())
				.set(RecordProductMatchesDO::getNodeName, node.getNodeName())
		);
	}


	@Override
	public IPage<RecordProductMatchesAiCheckDTO> pageList(RecordProductMatchesAiCheckQueryRequest request) {
		List<RecordBlacklistKeywordsDO> recordBlacklistKeywordsDOS = recordBlacklistKeywordsMapper.selectAll(request.getChainId().intValue());
		Set<String>                     keywords                   = recordBlacklistKeywordsDOS.stream().map(RecordBlacklistKeywordsDO::getBlacklistKeywordsName).collect(Collectors.toSet());
		IPage<RecordProductMatchesAiCheckDTO> res = recordProductMatchesMapper.pageList(Page.of(request.getPageNum(), request.getPageSize()),
				request.getChainId(),
				request.getEnterpriseName(), request.getProductName());
		res.getRecords().forEach(e -> {
			for (String key : keywords) {
				if (e.getEnterpriseName().contains(key)) {
					e.setIsNegative(true);
				}
			}
		});
		return res;
	}

}
