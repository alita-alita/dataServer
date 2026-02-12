package cn.idicc.taotie.infrastructment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcludeRecommendRelationDTO {
	/**
	 * 推荐产业链
	 */
	private Long chainId;

	/**
	 * 规避省份
	 * */
	private String excludeProvince;

	/**
	 * 规避城市
	 * */
	private String excludeCity;
}
