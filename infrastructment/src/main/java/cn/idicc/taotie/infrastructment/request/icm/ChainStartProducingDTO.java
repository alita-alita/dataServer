package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: MengDa
 * @Date: 2025/1/6
 * @Description:
 * @version: 1.0
 */
@Data
public class ChainStartProducingDTO {
    @NotNull(message = "产业链不可为空")
    private Long chainId;

//    @NotNull(message = "是否修改文档索引ID不可为空")
//    private Boolean isChangeLibFileId;
//
//    private String libFileId;
}
