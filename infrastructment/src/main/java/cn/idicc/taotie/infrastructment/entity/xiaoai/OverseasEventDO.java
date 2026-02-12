package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 出海事件表
 * </p>
 *
 * @author MengDa
 * @since 2025-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_event")
public class OverseasEventDO extends DataSyncBaseDO {

	private String country;

	/**
	 * 动态名称
	 */
	private String eventTitle;

	/**
	 * 发布日期
	 */
	private Date publishDate;

	/**
	 * 事件内容
	 */
	private String eventContent;

	/**
	 * 动态类型id
	 */
	private String eventTypeMd5;

	/**
	 * 事件星级
	 */
	private Integer eventStars;

	/**
	 * 关联园区id
	 */
	private String relateIndustryParkMd5;

	/**
	 * 关联产业链id
	 */
	private Long relateIndustryChainId;

	/**
	 * 舆情id
	 */
	private String newsMd5;

	/**
	 * 舆情文章链接
	 */
	private String newsUrl;

	/**
	 * 是否人工核查
	 */
	private Integer isChecked;

}
