package cn.idicc.taotie.infrastructment.response.icm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MengDa
 * @Date: 2025/1/7
 * @Description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionProduceStatusDTO {
    private String name;

    private Long total;

    private Long now;

    private String status;
}
