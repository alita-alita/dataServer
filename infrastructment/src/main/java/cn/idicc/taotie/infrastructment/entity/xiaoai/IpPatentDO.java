package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 专利信息表
 * </p>
 *
 * @author MengDa
 * @since 2024-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ip_patent")
public class IpPatentDO extends DataSyncBaseDO {

    /**
     * 专利名称
     */
    private String patentName;

    /**
     * md5 专利申请号
     */
    private String patentMd5;

    /**
     * 申请号
     */
    private String applicationNumber;

    /**
     * 申请日
     */
    private Date applicationDate;

    /**
     * 公开号/公告号
     */
    private String publicNumber;

    /**
     * 公开日/公告日
     */
    private Date publicDate;

    /**
     * 授权号
     */
    private String authorizationNumber;

    /**
     * 授权日
     */
    private Date authorizationDate;

    /**
     * IPC分类号
     */
    private String ipcTypeNumber;

    /**
     * 申请(专利权)人(可以是个人或者单位)
     */
    private String applicant;

    /**
     * 申请人邮编
     */
    private String applicantPostcode;

    /**
     * 发明人
     */
    private String inventor;

    /**
     * 发明名称
     */
    private String inventionName;

    /**
     * 代理人
     */
    private String agent;

    /**
     * 代理机构
     */
    private String agency;

    /**
     * 文献类型: 1发明公开 2发明授权 3实用新型 4外观设计
     */
    private Integer documentType;

    /**
     * 授权状态：0未授权 1已授权
     */
    private Integer authorizationStatus;

    /**
     * 申请人所在国（省）
     */
    private String applicantsCountry;

    /**
     * 申请人省份
     */
    private String applicantsProvince;

    /**
     * 申请人城市
     */
    private String applicantsCity;

    /**
     * 申请人区县
     */
    private String applicantsArea;

    /**
     * 申请人区域代码
     */
    private String applicantsRegionCode;

    /**
     * 摘要
     */
    private String digest;


}
