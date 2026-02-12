package cn.idicc.taotie.infrastructment.po.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wd
 * @Date: 2022/12/24
 * @Description:资讯
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCollectInfo {

        /**
         * 关注人id
         */
        private Long collectUserId;
        /**
         * 关注日期
         */
        private Long collectDate;

}
