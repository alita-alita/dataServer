package cn.idicc.taotie.infrastructment.entity.wenchang;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 知识库
 * </p>
 *
 * @author baomidou
 * @since 2024-03-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("knowledge_library")
public class KnowledgeLibraryDO extends BaseDO {

	/**
	 * 知识名称（与产业链名称一一对应）
	 */
	private String name;

	/**
	 * 运营人
	 */
	private String person;

	/**
	 * 知识类型 1产业链知识 2综合知识
	 */
	private Integer type;

	/**
	 * 状态 1可用 2开发中(2:敬请期待)
	 */
	private Integer state;

	/**
	 * 图标路径
	 */
	private String iconPath;

	/**
	 * 是否在用户可选知识列表展示 0否 1是(用户权益中是否显示)
	 */
	private Boolean isShow;

	/**
	 * 是否免费 0否 1是
	 */
	private Boolean isFree;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * 最近上线时间
	 */
	private LocalDateTime lastOnlineTime;
}
