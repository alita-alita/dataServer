package cn.idicc.taotie.infrastructment.response.icm;

import cn.idicc.common.model.BaseDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAppTypicalEnterpriseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 典型企业表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-10
 */
@Data
public class RecordAppTypicalEnterpriseDTO{

    private Long id;
    /**
     * 版本
     */
    private String version;

    /**
     * 企业id
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业社会统一信用代码
     */
    private String enterpriseUniCode;

    /**
     * 产业链id
     */
    private Long industryChainId;

    /**
     * 产业链
     */
    private String industryChainName;

    /**
     * 产业链节点id
     */
    private Long industryNodeId;

    /**
     * 产业链节点
     */
    private String industryNodeName;

    /**
     * 产业链标签id
     */
    private Long industryLabelId;

    /**
     * 产业链标签
     */
    private String industryLabelName;

    /**
     * 数据来源
     */
    private String dataSource;

    public static RecordAppTypicalEnterpriseDTO adapter(RecordAppTypicalEnterpriseDO recordAppTypicalEnterpriseDO){
        RecordAppTypicalEnterpriseDTO res = new RecordAppTypicalEnterpriseDTO();
        BeanUtils.copyProperties(recordAppTypicalEnterpriseDO,res);
        return res;
    }
}
