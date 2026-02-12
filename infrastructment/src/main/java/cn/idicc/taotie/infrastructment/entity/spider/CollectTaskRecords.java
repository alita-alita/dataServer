package cn.idicc.taotie.infrastructment.entity.spider;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Tag;

/**
 * 采集任务记录表
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class CollectTaskRecords {

    /**
     * 采集记录Id主键
     */
    private Long collectTaskRecordsId;

    /**
     * 采集记录文件名（文件名+采集时间）
     */
    private String collectTaskRecordsName;

    /**
     * 采集状态（1=导入成功 0=导入失败）
     */
    private Integer collectTaskRecordsState;

    /**
     * 采集类别（1=人才招商  2=资本招商  3=企业舆情关键词  4=产品舆情
     * 5=企业舆情公众号  6=亲缘舆情公众号  7=产业舆情公众号 8=官网采集
     * 9=核对公司数据）
     */
    private Integer collectTaskRecordsType;


}
