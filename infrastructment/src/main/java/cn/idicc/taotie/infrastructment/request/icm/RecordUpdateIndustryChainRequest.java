package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wd
 * @description 更新产业链request
 * @date 23-08-10
 */
@Data
public class RecordUpdateIndustryChainRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @NotNull(message = "version不能为空")
    private Long chainId;

    /**
     * 节点ID
     */
    @NotNull(message = "节点ID不能为空")
    private Long nodeId;
    /**
     * 产业链名称
     */
    @NotNull(message = "产业链名称不能为空")
    private String chainName;
    /**
     * 产业链简称
     */
    private String formerName;
    /**
     * 产业链icon
     */
    private MultipartFile icon;
}
