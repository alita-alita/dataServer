package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inst_alumni_association_member")
public class InstAlumniAssociationMemberDO extends DataSyncBaseDO {

	private String alumniMd5;

	private Integer relatedType;

	private String relatedUniCode;

	private String relatedTalentMd5;

	private String memberUniCode;

	private String memberType;

	private Date joinDate;

	private Date leaveDate;

	private String position;

}
