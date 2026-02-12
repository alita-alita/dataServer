package cn.idicc.taotie.infrastructment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CounterpartEnterpriseDTO{

    //合作日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime relationDate;

    //合作关系类型（客户0、供应商1、战略合作伙伴2）
    private Integer relationshipType;

    //合作关系类型
    private String relationshipTypeStr;

    //合作程度（非常紧密、紧密、一般）
    private String relationshipDegree;

    //合作企业社会唯一编码
    private String counterpartUniCode;

    //合作企业地区编码
    private String counterpartRegionCode;

    //合作关系企业名称
    private String counterpartEnterpriseName;

    //合作关系企业联系方式
    private String counterpartMobile;


    //更新日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
