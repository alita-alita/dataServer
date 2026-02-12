package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseObject;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DataSyncBaseDO extends BaseObject implements Serializable {

	private static final long          serialVersionUID = 1L;
	@TableId(
			type = IdType.AUTO
	)
	private              Long          id;
	@TableField(
			fill = FieldFill.INSERT
	)
	private              LocalDateTime gmtCreate;
	@TableField(
			fill = FieldFill.INSERT_UPDATE
	)
	private              LocalDateTime gmtModify;
	@TableField(
			fill = FieldFill.INSERT
	)
	private String createBy;
	@TableField(
			fill = FieldFill.INSERT_UPDATE
	)
	private String updateBy;

	public DataSyncBaseDO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(LocalDateTime gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public LocalDateTime getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(LocalDateTime gmtModify) {
		this.gmtModify = gmtModify;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
