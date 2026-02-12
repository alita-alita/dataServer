package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author wd
 * @description 保存产业链request
 * @date 12/19/22 10:02 AM
 */
@Data
public class RecordVersionCheckRequest implements Serializable {


    @NotNull(message = "请传入版本")
    private Long chainId;


    @NotNull(message = "请确认是否限制评分")
    private Boolean isLimitScore;

//    @NotNull(message = "请输入节点ID")
    private Long nodeId;
}
