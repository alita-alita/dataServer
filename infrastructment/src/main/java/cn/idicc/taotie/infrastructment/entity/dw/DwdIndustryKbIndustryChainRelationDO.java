package cn.idicc.taotie.infrastructment.entity.dw;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dwd_industry_kb_industry_chain_relation")
public class DwdIndustryKbIndustryChainRelationDO {

    private String id;

    private Long industryChainId;

    private String industryChainName;

    private Long industryChainNodeId;

    private String industryChainNodeName;

    private String kbId;

    private String kbTitle;

    /**
     * 逻辑删除标志,1:删除 0:未删除
     */
    @TableLogic(value = "0", delval = "1")
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 最近一次更新时间
     */
    private LocalDateTime gmtModify;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 数据来源
     */
    private String dataSource;
}
