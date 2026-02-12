package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: wd
 * @Date: 2023/6/13
 * @Description: 个人简介
 * @version: 1.0
 */
@Data
@TableName("personal_information_new")
public class PersonalInformationDO extends BaseDO {

    /**
     * 用户唯一标识
     */
    @TableField("uniq_id")
    private Integer uniqId;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 年龄
     */
    @TableField("age")
    private String age;

    /**
     * 头像图片url
     */
    @TableField("head_url")
    private String headUrl;

    /**
     * 学历
     */
    @TableField("education")
    private String education;

    /**
     * 职务
     */
    @TableField("position")
    private String position;

    /**
     * 任职时间
     */
    @TableField("employment_date")
    private String employmentDate;

    /**
     * 简历
     */
    @TableField("resume")
    private String resume;
}
