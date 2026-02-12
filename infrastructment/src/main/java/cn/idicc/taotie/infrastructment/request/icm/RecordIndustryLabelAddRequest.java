package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: WangZi
 * @Date: 2023/1/5
 * @Description: 产业链标签新增request
 * @version: 1.0
 */
@Data
public class RecordIndustryLabelAddRequest {

	/**
	 * 产业链标签名称
	 */
	@NotBlank(message = "产业链标签名称为空")
	private String labelName;

	@NotBlank(message = "产业链标签描述为空")
	private String labelDesc;

}
