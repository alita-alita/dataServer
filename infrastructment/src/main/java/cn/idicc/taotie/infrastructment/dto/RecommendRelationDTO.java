package cn.idicc.taotie.infrastructment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author guyongliang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendRelationDTO {

	/**
	 * 推荐产业链
	 */
	private Long chainId;

	/**
	 * 推荐省份
	 */
	private String province;

	/**
	 * 推荐城市
	 */
	private String city;

	/**
	 * 规避省份
	 * */
	private String excludeProvince;

	/**
	 * 规避城市
	 * */
	private String excludeCity;
}
