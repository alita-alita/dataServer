package cn.idicc.taotie.infrastructment.response.icm;

import cn.hutool.core.date.DatePattern;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAiCheckRecordDO;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author: MengDa
 * @Date: 2025/4/8
 * @Description:
 * @version: 1.0
 */
@Data
public class RecordAiCheckRecordDTO {

    private Long id;

    private Long chainId;

    private String chainName;

    private String runDesc;

    private String version;

    private Integer status;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date gmtCreate;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date completeDate;


//    public RecordAiCheckRecordDTO(RecordAiCheckRecordDO recordDO){
//        this.id = recordDO.getId();
//        this.chainName = recordDO.getChainName();
//        this.version = recordDO.getVersion();
//        this.status = RecordAiCheckStateEnum.getByCode(recordDO.getStatus()).getDesc();
//        this.gmtCreate = Date.from(recordDO.getGmtCreate().atZone(ZoneId.systemDefault()).toInstant());
//        this.completeDate = recordDO.getCompleteDate();
//        this.runDesc = recordDO.getRunDesc();
//    }
}
