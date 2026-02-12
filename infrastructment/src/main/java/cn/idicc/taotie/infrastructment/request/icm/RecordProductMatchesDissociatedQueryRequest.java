package cn.idicc.taotie.infrastructment.request.icm;

import cn.idicc.common.util.QueryPage;
import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesDissociatedDO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wd
 * @description 产业链分页查询DTO
 * @date 12/19/22 10:02 AM
 */
@Data
public class RecordProductMatchesDissociatedQueryRequest extends QueryPage<RecordProductMatchesDissociatedDO> implements Serializable {

	private static final long serialVersionUID = 1L;


	private Long chainId;

	private String enterpriseName;

	private String productName;

	private Integer status;

	private Long labelId;

	private Long nodeId;

	private BigDecimal minMatchedScore;
}
