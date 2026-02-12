/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryChainQueryRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordUpdateIndustryChainRequest;
import cn.idicc.taotie.infrastructment.request.icm.SaveRecordIndustryChainRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wd
 * @version $Id: IndustryChainService.java,v 0.1 2020.10.10 auto Exp $
 * @description 产业链Service
 */
public interface RecordIndustryChainService extends IService<RecordIndustryChainDO> {


	RecordIndustryChainDTO getByChainId(Long chainId);

	/**
	 * 保存产业链
	 *
	 * @param saveChainRequest
	 */
	void save(SaveRecordIndustryChainRequest saveChainRequest);

	/**
	 * 分页查询产业链数据
	 *
	 * @param chainQueryDTO
	 * @return
	 */
	IPage<RecordIndustryChainDTO> page(RecordIndustryChainQueryRequest chainQueryDTO);

	/**
	 * 根据产业链id查询产业链信息
	 *
	 * @param id
	 * @return
	 */
	RecordIndustryChainDO getById(Long id);

	/**
	 * 批量查询产业链信息
	 *
	 * @return
	 */
	List<RecordIndustryChainDO> listByChainIds(List<Long> chainIds);


//	String importData(MultipartFile file, Long chainId) throws IOException;

	Boolean exportChain(RecordIndustryChainDO chainDO);

	void update(RecordUpdateIndustryChainRequest request);

	boolean allowExit(Long chainId);
}
