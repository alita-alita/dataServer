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
public class SaveRecordIndustryChainRequest implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 产业链代码
     */
    @NotNull(message = "产业链代码不能为空")
    private String chainCode;
    /**
     * 产业链名称
     */
    @NotNull(message = "产业链名称不能为空")
    private String chainName;
    /**
     * 备注
     */
    private String notes;


    private Long categoryId;


}
