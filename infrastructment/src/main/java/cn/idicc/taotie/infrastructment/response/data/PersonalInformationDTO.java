package cn.idicc.taotie.infrastructment.response.data;

import lombok.Data;

/**
 * @Author: wd
 * @Date: 2023/6/13
 * @Description:个人简介
 * @version: 1.0
 */
@Data
public class PersonalInformationDTO {

    /**
     * 用户唯一标识
     */
    private Integer uniqId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 学历
     */
    private String education;

    /**
     * 职位
     */
    private String position;

    /**
     * 任职时间
     */
    private String employmentDate;

    /**
     * 简历
     */
    private String resume;

}
