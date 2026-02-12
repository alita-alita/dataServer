package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author wd
 * @description 保存产业链request
 * @date 12/19/22 10:02 AM
 */
@Data
public class UpdateNodeScoreRequest implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 产业链节点ID
     */
    @NotNull(message = "节点ID不可为空")
    private Long chainNodeId;



    @NotNull(message = "匹配度阈值不能为空")
    private Double thresholdScore;
}
