package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dws_enterprise_origin")
public class DwsEnterpriseOriginDO extends DataSyncBaseDO{

	private String uniCode;
	private String enterpriseName;
	private String regionCode;
	private String province;
	private String city;
	private String area;

}
