package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wd
 * @description 保存产业链request
 * @date 12/19/22 10:02 AM
 */
@Data
public class RecordVersionQueryRequest implements Serializable {

    private static final long serialVersionUID=1L;

    private String chainId;

    private long pageSize = 10;

    /**
     * 当前页
     */
    private long pageNum = 1;


}
