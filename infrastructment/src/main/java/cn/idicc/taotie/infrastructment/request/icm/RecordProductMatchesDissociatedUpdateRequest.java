package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

/**
 * @author wd
 * @description 产业链分页查询DTO
 * @date 12/19/22 10:02 AM
 */
@Data
public class RecordProductMatchesDissociatedUpdateRequest {

	private Long id;

	private Long labelId;

	private Long nodeId;

	private String productName;

	private String productUrl;

	private String productDescription;

	private String productPurpose;
}
