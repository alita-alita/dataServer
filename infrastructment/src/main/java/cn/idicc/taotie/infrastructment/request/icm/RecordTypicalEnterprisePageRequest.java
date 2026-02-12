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
public class RecordTypicalEnterprisePageRequest implements Serializable {


    private Long pageNum=1L;

    private Long pageSize=10L;

    private Long labelId;
}
