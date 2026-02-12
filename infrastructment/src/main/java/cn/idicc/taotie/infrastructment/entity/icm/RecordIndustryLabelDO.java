package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wd
 * @description 产业链标签节点DO
 */
@TableName("record_industry_label")
@Data
@NoArgsConstructor
public class RecordIndustryLabelDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 业务主键ID
     */
    private Long bizId;


    /**
     * 标签名称
     */
    private String labelName;

    private String labelDesc;

    /**
     * 标签分类：0未知 1产品 2技术
     */
    private Byte labelType;

//    private Long chainId;

    /**
     * 0初始化 1执行中 2执行完成 3执行失败
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String failReason;

    public static RecordIndustryLabelDO adapter(JSONObject jsonObject){
        RecordIndustryLabelDO res = new RecordIndustryLabelDO();
        res.setBizId(jsonObject.getLong("id"));
        res.setLabelName(jsonObject.getString("label_name").isEmpty()?null:jsonObject.getString("label_name"));
        res.setLabelType(jsonObject.getByte("label_type"));
//        res.setChainId(jsonObject.getLong("chain_id"));
        res.setDeleted(jsonObject.getBoolean("deleted"));
        return res;
    }

    public RecordIndustryLabelDO(String labelName) {
        this.labelName = labelName;
    }
}
