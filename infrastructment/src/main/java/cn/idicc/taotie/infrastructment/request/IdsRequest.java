package cn.idicc.taotie.infrastructment.request;

import lombok.Data;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/5/8
 * @Description:
 * @version: 1.0
 */
@Data
public class IdsRequest {

    private List<Long> ids;
}
