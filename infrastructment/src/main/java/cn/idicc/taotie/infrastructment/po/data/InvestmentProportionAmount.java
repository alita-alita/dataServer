package cn.idicc.taotie.infrastructment.po.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wd
 * @Date: 2023/5/31
 * @Description:研发投入对象
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentProportionAmount {

        /**
         * 年份
         */
        private Integer year;
        /**
         * 研发金额
         */
        private Double amount;

}
