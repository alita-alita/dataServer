/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryLabelAddRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryLabelDTO;
import cn.idicc.taotie.service.result.CommonPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 产业链标签Service
 *
 * @author wd
 * @version $Id: IndustryLabelService.java,v 0.1 2020.10.10 auto Exp $
 */
public interface RecordIndustryLabelService extends IService<RecordIndustryLabelDO> {


	/**
	 * 添加产业链标签
	 *
	 * @param param
	 * @return
	 */
	void add(RecordIndustryLabelAddRequest param);

	/**
     * 分页获取产业链标签列表
     */
	CommonPageDTO<RecordIndustryLabelDTO> pageList(String searchKeyword, Integer pageNum, Integer pageSize);

	int deleteById(Long labelId);

	int updateById(Long labelId, String labelName, String labelDesc);

	List<RecordIndustryLabelDO> listAllLabelRefByChainId(Long chainId);

}
