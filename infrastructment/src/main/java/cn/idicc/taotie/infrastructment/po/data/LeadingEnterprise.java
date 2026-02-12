package cn.idicc.taotie.infrastructment.po.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wd
 * @Date: 2023/5/31
 * @Description:龙头企业对象
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadingEnterprise {

        /**
         * 是否龙头企业
         */
        private Boolean isLeading;
        /**
         * 排名
         */
        private Integer leadingSort;

}
