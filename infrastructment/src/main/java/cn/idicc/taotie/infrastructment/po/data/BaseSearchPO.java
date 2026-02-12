package cn.idicc.taotie.infrastructment.po.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * @Author: WangZi
 * @Date: 2022/12/27
 * @Description:
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseSearchPO {

    /**
     * 定义的是es中的主键id
     */
    @Id
    private Long id;

    /**
     * 是否逻辑删除   0否1是
     */
    private Boolean deleted;

    /**
     * 创建时间时间戳
     */
    private Long gmtCreate;

    /**
     * 更新时间时间戳
     */
    private Long gmtModify;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
}
