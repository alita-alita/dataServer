package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 海外政策基本信息表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "overseas_policy", autoResultMap = true)
public class OverseasPolicyDO extends DataSyncBaseDO {

	/**
	 * 政府政策表主键MD5
	 */
	private String policyMd5;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 政府政策标题
	 */
	private String title;

	/**
	 * 政府政策发文字号
	 */
	private String releaseNumber;

	/**
	 * 政府政策发布时间
	 */
	private LocalDateTime releaseTime;

	/**
	 * 成文日期
	 */
	private LocalDate issueDate;

	/**
	 * 政府政策正文内容
	 */
	private String content;

	@TableField(typeHandler = FastjsonTypeHandler.class)
	private List<JSONObject> attachFiles;

	/**
	 * 政府政策发布单位名称
	 */
	private String adminUnit;

	/**
	 * 政府政策发布单位层级分类，1代表国务院直属单位，2代表国务院部委单位，3代表省级政府 ，4代表市级政府 ，5代表区县级政府，6代表未知
	 */
	private Integer adminUnitLevel;

	/**
	 * 政府政策发布url地址或网址
	 */
	private String policyUrl;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区县
	 */
	private String area;

	/**
	 * 区域代码
	 */
	private String regionMd5;

	/**
	 * 政府政策实施生效时间
	 */
	private LocalDate effectiveDate;

	/**
	 * 政府政策时效时间，如2年,3个月等时长数据
	 */
	private String effectiveDuration;

}
