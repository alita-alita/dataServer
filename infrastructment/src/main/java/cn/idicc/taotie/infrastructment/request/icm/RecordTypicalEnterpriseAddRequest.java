package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wd
 * @description 保存产业链request
 * @date 12/19/22 10:02 AM
 */
@Data
public class RecordTypicalEnterpriseAddRequest implements Serializable {


    @NotNull(message = "请传入企业名称")
    private String enterpriseName;

    private Long labelId;
}
