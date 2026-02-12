/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.common.exception.BusinessException;
import cn.idicc.identity.utils.PinYin4jUtils;
import cn.idicc.pangu.common.RPCResult;
import cn.idicc.pangu.service.IndustryChainRpcService;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.constant.OssProperties;
import cn.idicc.taotie.infrastructment.dao.icm.RecordIndustryChainDao;
import cn.idicc.taotie.infrastructment.dao.icm.RecordIndustryChainNodeDao;
import cn.idicc.taotie.infrastructment.entity.data.IndustryLabelDO;
import cn.idicc.taotie.infrastructment.entity.icm.*;
import cn.idicc.taotie.infrastructment.enums.BooleanEnum;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.enums.RecordVersionStateEnum;
import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.*;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryChainQueryRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordUpdateIndustryChainRequest;
import cn.idicc.taotie.infrastructment.request.icm.SaveRecordIndustryChainNodeRequest;
import cn.idicc.taotie.infrastructment.request.icm.SaveRecordIndustryChainRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainDTO;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainNodeService;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainService;
import cn.idicc.taotie.service.util.OssUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * @author wd
 * @version $Id: IndustryChainServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 * @description 产业链ServiceImpl
 */
@Slf4j
@Service
@RefreshScope
public class RecordIndustryChainServiceImpl extends ServiceImpl<RecordIndustryChainMapper, RecordIndustryChainDO> implements RecordIndustryChainService {

	@Autowired
	private RecordIndustryChainDao industryChainDao;

	@Autowired
	private RecordIndustryChainNodeDao industryChainNodeDao;

	@Autowired
	private RecordIndustryChainMapper industryChainMapper;

	@Autowired
	private RecordIndustryChainNodeMapper industryChainNodeMapper;

	@Autowired
	private RecordIndustryLabelMapper industryLabelMapper;

	@Autowired
	private RecordIndustryChainCategoryMapper industryChainCategoryMapper;

	@Autowired
	private RecordIndustryChainCategoryRelationMapper industryChainCategoryRelationMapper;

	@Resource
	private RecordIndustryChainNodeService chainNodeService;

	@Autowired
	private OssProperties ossProperties;

	@Resource
	private OssUtil ossUtil;

	@Autowired
	private RecordIndustryChainMapper recordIndustryChainMapper;

	@DubboReference(interfaceClass = IndustryChainRpcService.class, check = false)
	private IndustryChainRpcService panguIndustryChainRpcService;

	@DubboReference(interfaceClass = cn.idicc.wenchang.service.IndustryChainRpcService.class, check = false)
	private cn.idicc.wenchang.service.IndustryChainRpcService wenchangIndustryChainRpcService;

	@Autowired
	private ChainNodeRefAtomNodeMapper chainNodeRefAtomNodeMapper;


	@Override
	public RecordIndustryChainDTO getByChainId(Long chainId) {
		RecordIndustryChainDO chainDO = industryChainDao.getOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.eq(RecordIndustryChainDO::getBizId, chainId)
		);
		Assert.notNull(chainDO, "产业链不存在");
		if (StringUtil.isNotBlank(chainDO.getIcon())) {
			String signUrl = ossUtil.signUrl(ossProperties.getBucketName(), chainDO.getIcon());
			chainDO.setIcon(signUrl);
		}
		RecordIndustryChainDTO res = RecordIndustryChainDTO.adapt(chainDO);

		List<RecordIndustryChainCategoryRelationDO> relationDOS = industryChainCategoryRelationMapper.selectByChainIds(Collections.singletonList(res.getId()));
		if (!relationDOS.isEmpty()) {
			res.setCategoryId(relationDOS.get(0).getCategoryId());
			RecordIndustryChainCategoryDO categoryDOS = industryChainCategoryMapper.selectById(res.getCategoryId());
			res.setCategoryName(categoryDOS.getCategoryName());
		}
		return res;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SaveRecordIndustryChainRequest saveChainRequest) {
		Long                          categoryId              = saveChainRequest.getCategoryId();
		RecordIndustryChainCategoryDO industryChainCategoryDO = industryChainCategoryMapper.selectById(categoryId);
		if (industryChainCategoryDO == null) {
			throw new BizException(ErrorCode.PARAMS_ERROR.getCode(), "产业链分类不存在");
		}

		// 产业链代码
		String chainCode = saveChainRequest.getChainCode();
		// 产业链名称
		String chainName = saveChainRequest.getChainName();
		// 第一步，根据产业链code或者产业链名称查询产业链信息，校验数据是否已经存在
		List<RecordIndustryChainDO> chainDOS = industryChainDao.queryByParams(new RecordIndustryChainDO(chainCode, chainName));
		// 第二步，校验产业链代码或者产业链名称是否已经存在
		duplicateDataValidate(chainDOS, chainCode, chainName);
		Long maxBizId = industryChainMapper.getMaxBizId();
		// 第三步，保存产业链数据
		RecordIndustryChainDO chainDO = new RecordIndustryChainDO(chainCode, chainName, saveChainRequest.getNotes());
		chainDO.setChainNamePinyin(PinYin4jUtils.getPingYin(chainDO.getChainName()));
		chainDO.setFormerName(saveChainRequest.getChainName());
		chainDO.setBizId(maxBizId == null ? 1 : maxBizId + 1);
		industryChainDao.save(chainDO);


		//增加分类表
		RecordIndustryChainCategoryRelationDO industryChainCategoryRelationDO = new RecordIndustryChainCategoryRelationDO();
		industryChainCategoryRelationDO.setCategoryId(categoryId);
		industryChainCategoryRelationDO.setChainId(chainDO.getBizId());
		industryChainCategoryRelationMapper.insert(industryChainCategoryRelationDO);


		// 第四步，初始化一个产业链节点
		chainNodeService.saveOrUpdate(buildChainNodeRequest(chainDO.getBizId(), chainName));

	}

	/**
	 * 重复数据校验
	 *
	 * @param chainDOS
	 * @param chainCode
	 * @param chainName
	 */
	private void duplicateDataValidate(List<RecordIndustryChainDO> chainDOS, String chainCode, String chainName) {
		if (CollectionUtils.isNotEmpty(chainDOS)) {
			chainDOS.forEach(e -> {
				if (e.getChainCode().equals(chainCode)) {
					throw new BusinessException(ErrorCode.DUPLICATE_DATA.getCode(), "产业链代码已经存在");
				}
				if (e.getChainName().equals(chainName)) {
					throw new BusinessException(ErrorCode.DUPLICATE_DATA.getCode(), "产业链名称已经存在");
				}
			});
		}
	}

	/**
	 * 构建请求参数
	 *
	 * @param chainId
	 * @param chainName
	 * @return
	 */
	private SaveRecordIndustryChainNodeRequest buildChainNodeRequest(Long chainId, String chainName) {
		SaveRecordIndustryChainNodeRequest request = new SaveRecordIndustryChainNodeRequest();
		request.setChainId(chainId);
		request.setNodeName(chainName);
		request.setIsLeaf(BooleanEnum.NO.getCode());
		request.setNodeOrder(GlobalConstant.ZERO);
		request.setNodeLevel(GlobalConstant.ONE);
		request.setNodeParent(Long.parseLong(GlobalConstant.ZERO.toString()));
		return request;
	}

	@Override
	public IPage<RecordIndustryChainDTO> page(RecordIndustryChainQueryRequest chainQueryDTO) {
		// 1. 根据分类ID查询关联的产业链ID列表
		if (chainQueryDTO.getCategoryId() != null) {
			List<RecordIndustryChainCategoryRelationDO> tempRelations =
					industryChainCategoryRelationMapper.selectByCategoryId(chainQueryDTO.getCategoryId());

			// 如果没有关联的产业链，直接返回空分页结果
			if (CollectionUtils.isEmpty(tempRelations)) {
				return new Page<>();
			}

			// 设置查询条件中的产业链ID列表
			List<Long> chainIds = tempRelations.stream()
					.map(RecordIndustryChainCategoryRelationDO::getChainId)
					.collect(toList());
			chainQueryDTO.setChainIds(chainIds);
		}

		// 2. 执行分页查询
		IPage<RecordIndustryChainDTO> page = industryChainMapper.chainPageSearch(
				Page.of(chainQueryDTO.getCurrent(), chainQueryDTO.getSize()),
				chainQueryDTO.getNotes(),
				chainQueryDTO.getChainName(),
				chainQueryDTO.getState(),
				chainQueryDTO.getChainIds()
		);

		// 3. 如果查询结果为空，直接返回
		if (page.getRecords().isEmpty()) {
			return page;
		}

		// 4. 处理编辑权限状态
		processEditPermission(page.getRecords());

		// 5. 关联分类信息
		associateCategoryInfo(page.getRecords());

		return page;
	}

	/**
	 * 处理产业链记录的编辑权限状态
	 *
	 * @param records 产业链记录列表
	 */
	private void processEditPermission(List<RecordIndustryChainDTO> records) {
		List<RecordVersionStateEnum> allowList = RecordVersionStateEnum.getChainAllowEdit();

		records.forEach(record -> {
			RecordVersionStateEnum stateEnum = RecordVersionStateEnum.getByCode(record.getState());
			if (stateEnum != null) {
				record.setIsAllowEdit(allowList.contains(stateEnum));
			}
		});
	}

	/**
	 * 关联分类信息到产业链记录
	 *
	 * @param records 产业链记录列表
	 */
	private void associateCategoryInfo(List<RecordIndustryChainDTO> records) {
		// 获取所有记录的ID
		List<Long> chainIds = records.stream()
				.map(RecordIndustryChainDTO::getId)
				.collect(Collectors.toList());

		// 查询关联的分类关系
		List<RecordIndustryChainCategoryRelationDO> relationDOS =
				industryChainCategoryRelationMapper.selectByChainIds(chainIds);

		// 如果没有关联关系，直接返回
		if (org.springframework.util.CollectionUtils.isEmpty(relationDOS)) {
			return;
		}

		// 构建chainId到categoryId的映射
		Map<Long, Long> chainToCategoryMap = relationDOS.stream()
				.collect(Collectors.toMap(
						RecordIndustryChainCategoryRelationDO::getChainId,
						RecordIndustryChainCategoryRelationDO::getCategoryId,
						// 处理重复key的情况，保留第一个
						(existing, replacement) -> existing
				));

		// 获取所有相关的分类ID
		List<Long> categoryIds = relationDOS.stream()
				.map(RecordIndustryChainCategoryRelationDO::getCategoryId)
				.distinct()
				.collect(toList());

		// 查询分类信息
		List<RecordIndustryChainCategoryDO> categoryDOS = industryChainCategoryMapper.selectList(
				Wrappers.lambdaQuery(RecordIndustryChainCategoryDO.class)
						.in(RecordIndustryChainCategoryDO::getId, categoryIds)
		);

		// 构建categoryId到categoryName的映射
		Map<Long, String> categoryIdToNameMap = categoryDOS.stream()
				.collect(Collectors.toMap(
						RecordIndustryChainCategoryDO::getId,
						RecordIndustryChainCategoryDO::getCategoryName
				));

		// 关联分类信息到记录
		records.forEach(record -> {
			Long categoryId = chainToCategoryMap.get(record.getId());
			if (categoryId != null) {
				record.setCategoryId(categoryId);
				String categoryName = categoryIdToNameMap.get(categoryId);
				if (categoryName != null) {
					record.setCategoryName(categoryName);
				}
			}
		});
	}

	@Override
	public RecordIndustryChainDO getById(Long id) {
		return industryChainDao.getById(id);
	}

	@Override
	public List<RecordIndustryChainDO> listByChainIds(List<Long> chainIds) {
		return industryChainDao.list(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.in(RecordIndustryChainDO::getBizId, chainIds)
		);
	}

//	public String importData(MultipartFile file, Long chainId) throws IOException {
//		ZipInputStream           zipInputStream = new ZipInputStream(file.getInputStream());
//		ZipEntry                 entry          = zipInputStream.getNextEntry();
//		Map<String, InputStream> fileMap        = new HashMap<>();
//		byte[]                   buffer         = new byte[1024];
//		while (entry != null) {
//			String fileName = entry.getName();
//			// 将条目内容读入字节数组
//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//			int                   len;
//			while ((len = zipInputStream.read(buffer)) > 0) {
//				byteArrayOutputStream.write(buffer, 0, len);
//			}
//			// 将字节数组转为 InputStream 并存储在 Map 中
//			InputStream entryInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//			zipInputStream.closeEntry();
//			fileMap.put(fileName, entryInputStream);
//			entry = zipInputStream.getNextEntry();
//		}
//		zipInputStream.close();
//		Map<String, JSONArray> fileDataMap = new HashMap<>();
//		for (Map.Entry<String, InputStream> entryFile : fileMap.entrySet()) {
//			JSONArray jsonArray = null;
//			try {
//				jsonArray = ExcelUtils.readFile(entryFile.getKey(), entryFile.getValue());
//			} catch (Exception e) {
//				log.error("产业链导入,解析EXCEL错误", e);
//			}
//			if (jsonArray != null) {
//				fileDataMap.put(entryFile.getKey(), jsonArray);
//			}
//		}
//		List<RecordIndustryChainDO>                  industryChainDOList                  = new ArrayList<>();
//		List<RecordIndustryChainNodeDO>              industryChainNodeDOS                 = new ArrayList<>();
//		List<RecordIndustryLabelDO>                  industryLabelDOList                  = new ArrayList<>();
//		List<RecordIndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOList = new ArrayList<>();
//		List<RecordIndustryChainCategoryDO>          industryChainCategoryDOS             = new ArrayList<>();
//		List<RecordIndustryChainCategoryRelationDO>  industryChainCategoryRelationDOList  = new ArrayList<>();
//		for (Map.Entry<String, JSONArray> entryData : fileDataMap.entrySet()) {
//			switch (entryData.getKey()) {
//				case "industry_chain.xlsx":
//					industryChainDOList = entryData.getValue().toJavaList(JSONObject.class).stream().map(e ->
//							RecordIndustryChainDO.adapter((JSONObject) e)
//					).collect(Collectors.toList());
//					break;
//				case "industry_chain_category.xlsx":
//					industryChainCategoryDOS = entryData.getValue().toJavaList(JSONObject.class).stream().map(e ->
//							RecordIndustryChainCategoryDO.adapter((JSONObject) e)
//					).collect(Collectors.toList());
//					break;
//				case "industry_chain_category_relation.xlsx":
//					industryChainCategoryRelationDOList = entryData.getValue().toJavaList(JSONObject.class).stream().map(e ->
//							RecordIndustryChainCategoryRelationDO.adapter((JSONObject) e)
//					).collect(Collectors.toList());
//					break;
//				case "industry_chain_node.xlsx":
//					industryChainNodeDOS = entryData.getValue().toJavaList(JSONObject.class).stream().map(e ->
//							RecordIndustryChainNodeDO.adapter((JSONObject) e)
//					).collect(Collectors.toList());
//					break;
//				case "industry_chain_node_label_relation.xlsx":
//					industryChainNodeLabelRelationDOList = entryData.getValue().toJavaList(JSONObject.class).stream().map(e ->
//							RecordIndustryChainNodeLabelRelationDO.adapter((JSONObject) e)
//					).collect(Collectors.toList());
//					break;
//				case "industry_label.xlsx":
//					industryLabelDOList = entryData.getValue().toJavaList(JSONObject.class).stream().map(e ->
//							RecordIndustryLabelDO.adapter((JSONObject) e)
//					).collect(Collectors.toList());
//					break;
//			}
//		}
//		industryChainDOList = industryChainDOList.stream().filter(e -> !e.getDeleted()).collect(toList());
//		industryChainNodeDOS = industryChainNodeDOS.stream().filter(e -> !e.getDeleted()).collect(toList());
//		industryLabelDOList = industryLabelDOList.stream().filter(e -> !e.getDeleted()).filter(e -> e.getChainId() != null).collect(toList());
//		industryChainNodeLabelRelationDOList = industryChainNodeLabelRelationDOList.stream().filter(e -> !e.getDeleted()).collect(toList());
//		industryChainCategoryDOS = industryChainCategoryDOS.stream().filter(e -> !e.getDeleted()).collect(toList());
//		industryChainCategoryRelationDOList = industryChainCategoryRelationDOList.stream().filter(e -> !e.getDeleted()).collect(toList());
//		Map<RecordIndustryChainDO, RecordChainImportDTO> importDTOMap = new LinkedHashMap<>();
//		for (RecordIndustryChainDO industryChainDO : industryChainDOList) {
//			RecordChainImportDTO importDTO = new RecordChainImportDTO();
//			importDTO.setIndustryChainDO(industryChainDO);
//			importDTO.setIndustryChainNodeDOS(industryChainNodeDOS.stream().filter(e -> e.getChainId().equals(importDTO.getIndustryChainDO().getBizId())).collect(toList()));
//			importDTO.setIndustryLabelDOS(industryLabelDOList.stream().filter(e -> e.getChainId().equals(importDTO.getIndustryChainDO().getBizId())).collect(toList()));
//			importDTO.setIndustryChainCategoryRelationDOS(industryChainCategoryRelationDOList.stream().filter(e -> e.getChainId().equals(importDTO.getIndustryChainDO().getBizId())).collect(toList()));
//			importDTO.setIndustryChainCategoryDOS(industryChainCategoryDOS);
//			Set<Long> nodeIds  = importDTO.getIndustryChainNodeDOS().stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet());
//			Set<Long> labelIds = importDTO.getIndustryLabelDOS().stream().map(RecordIndustryLabelDO::getBizId).collect(Collectors.toSet());
//			importDTO.setIndustryChainNodeLabelRelationDOS(industryChainNodeLabelRelationDOList.stream()
//					.filter(e -> nodeIds.contains(e.getChainNodeId()) || labelIds.contains(e.getIndustryLabelId())).collect(toList()));
//			importDTOMap.put(industryChainDO, importDTO);
//		}
//		for (Map.Entry<RecordIndustryChainDO, RecordChainImportDTO> importDTOEntry : importDTOMap.entrySet()) {
//			if (chainId != null && !Objects.equals(importDTOEntry.getKey().getBizId(), chainId)) {
//				continue;
//			}
//			importChain(importDTOEntry.getValue());
//		}
//		return "success";
//	}

	@Override
	public Boolean exportChain(RecordIndustryChainDO chainDO) {


		List<RecordIndustryChainNodeDO> industryChainNodeDOS = industryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getChainId, chainDO.getBizId())
		);
		List<RecordIndustryLabelDO> industryLabelDOS = industryLabelMapper.getLabelByChainId(chainDO.getBizId());
		List<IndustryLabelDO> industryLabelForSync =
				industryLabelDOS.stream().map(row -> {
					IndustryLabelDO industryLabelDO = new IndustryLabelDO();
					industryLabelDO.setLabelType(row.getLabelType());
					industryLabelDO.setLabelName(row.getLabelName());
//					industryLabelDO.setChainId(chainDO.getBizId());
					industryLabelDO.setId(row.getBizId());
					return industryLabelDO;
				}).collect(Collectors.toList());
		Set<Long>                   nodeIds          = industryChainNodeDOS.stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet());
		Set<Long>                   labelIds         = industryLabelDOS.stream().map(RecordIndustryLabelDO::getBizId).collect(Collectors.toSet());
//		List<RecordIndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOS = new ArrayList<>();
//		if (!nodeIds.isEmpty() && !labelIds.isEmpty()) {
//			industryChainNodeLabelRelationDOS = industryChainNodeLabelRelationMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeLabelRelationDO.class)
//					.in(RecordIndustryChainNodeLabelRelationDO::getChainNodeId, nodeIds)
//					.in(RecordIndustryChainNodeLabelRelationDO::getIndustryLabelId, labelIds)
//			);
//		}
		List<RecordIndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOS = new ArrayList<>();
		if (!nodeIds.isEmpty() && !labelIds.isEmpty()) {
			industryChainNodeLabelRelationDOS = chainNodeRefAtomNodeMapper.selectNodeRefLabelByNodeIdsAndLabelIds(labelIds, nodeIds);
		}
		List<RecordIndustryChainCategoryRelationDO> industryChainCategoryRelationDOS = industryChainCategoryRelationMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainCategoryRelationDO.class)
				.eq(RecordIndustryChainCategoryRelationDO::getChainId, chainDO.getBizId()));
		List<RecordIndustryChainCategoryDO> industryChainCategoryDOS = new ArrayList<>();
		if (!industryChainCategoryRelationDOS.isEmpty()) {
			industryChainCategoryDOS = industryChainCategoryMapper.selectBatchIds(industryChainCategoryRelationDOS.stream()
					.map(RecordIndustryChainCategoryRelationDO::getCategoryId)
					.collect(toList()));
		}
		JSONObject jsonObject = new JSONObject();
		//适配格式
		chainDO.setId(chainDO.getBizId());
		chainDO.setGmtCreate(null);
		chainDO.setGmtModify(null);
		industryChainNodeDOS.forEach(e -> {
			e.setId(e.getBizId());
			e.setGmtCreate(null);
			e.setGmtModify(null);
		});
//		industryLabelDOS.forEach(e -> {
//			e.setId(e.getBizId());
//			e.setGmtCreate(null);
//			e.setGmtModify(null);
//		});
		industryChainNodeLabelRelationDOS.forEach(e -> {
			e.setId(null);
			e.setGmtCreate(null);
			e.setGmtModify(null);
		});
		industryChainCategoryDOS.forEach(e -> {
			e.setGmtCreate(null);
			e.setGmtModify(null);
		});
		industryChainCategoryRelationDOS.forEach(e -> {
			e.setId(null);
			e.setGmtCreate(null);
			e.setGmtModify(null);
		});
		jsonObject.put("industryChainDO", chainDO);
		jsonObject.put("industryChainNodeDOS", industryChainNodeDOS);
		jsonObject.put("industryLabelDOS", industryLabelForSync);
		jsonObject.put("industryChainNodeLabelRelationDOS", industryChainNodeLabelRelationDOS);
		jsonObject.put("industryChainCategoryDOS", industryChainCategoryDOS);
		jsonObject.put("industryChainCategoryRelationDOS", industryChainCategoryRelationDOS);

		try {
			//更新状态为同步中
			recordIndustryChainMapper.update(null, Wrappers.lambdaUpdate(RecordIndustryChainDO.class)
					.set(RecordIndustryChainDO::getState, RecordChainStateEnum.SYNCING_PRODUCTION.getValue())
					.eq(RecordIndustryChainDO::getBizId, chainDO.getBizId()));
			RPCResult<String> res1 = panguIndustryChainRpcService.importChain(jsonObject.toString());
			if (res1.getStatus() != 0) {
				log.error(String.format("pangu chain import fail:%s", res1.getMsg()));
			}
			cn.idicc.wenchang.common.RPCResult<String> res2 = wenchangIndustryChainRpcService.importChain(jsonObject.toString());
			if (res2.getStatus() != 0) {
				log.error(String.format("wenchang chain import fail:%s", res2.getMsg()));
			}
			if (res1.getStatus() == 0 && res2.getStatus() == 0) {
				return true;
			}
			recordIndustryChainMapper.update(null, Wrappers.lambdaUpdate(RecordIndustryChainDO.class)
					.set(RecordIndustryChainDO::getState, RecordChainStateEnum.SYNC_PRODUCTION_FINISH.getValue())
					.eq(RecordIndustryChainDO::getBizId, chainDO.getBizId()));
		} catch (Exception e) {
			log.error("chain import rpc fail:", e);
			return false;
		}
		return false;
	}


	@Override
	public void update(RecordUpdateIndustryChainRequest request) {
		if (!this.allowExit(request.getChainId())) {
			throw new BizException("当前版本状态不允许编辑");
		}
		RecordIndustryChainNodeDO nodeDO = industryChainNodeDao.getOne(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getBizId, request.getNodeId())
		);
		Assert.notNull(nodeDO, "节点不存在");
		try {

			RecordIndustryChainDO chainDO = industryChainDao.getOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
					.eq(RecordIndustryChainDO::getBizId, nodeDO.getChainId())
			);
			if (request.getIcon() != null) {
				// 上传图片
				String pathImage = ossUtil.upload(request.getIcon().getInputStream(), request.getIcon().getOriginalFilename());
				chainDO.setIcon(pathImage);
			}

			chainDO.setChainName(request.getChainName());
			chainDO.setFormerName(request.getChainName());
			// 更新产业链名称、简称、icon
			industryChainDao.updateById(chainDO);
			// 更新根节点名称
			nodeDO.setNodeName(request.getChainName());
			chainNodeService.updateById(nodeDO);
		} catch (Exception ex) {
			log.error("更新跟节点数据失败,request is:{}", JSON.toJSONString(request));
			throw new BizException("更新失败," + ex.getMessage());
		}
	}

	@Override
	public boolean allowExit(Long chainId) {
		RecordIndustryChainDO recordIndustryChainDO = recordIndustryChainMapper.selectByBizId(chainId);
		if (recordIndustryChainDO == null) {
			return false;
		}
		return RecordChainStateEnum.NORMAL.getValue() == recordIndustryChainDO.getState()
				|| recordIndustryChainDO.getState() >= RecordChainStateEnum.SYNC_PRODUCTION_FINISH.getValue();
	}

}
