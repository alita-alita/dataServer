package cn.idicc.taotie.service.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分页返回体
 *
 * @author guyl
 */
@Data
public class CommonPageDTO<T> implements Serializable {

    private static final long serialVersionUID = -2603858436256630965L;

    /**
     * 列表数据
     */
    private List<T> records;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 总数
     */
    private Integer total;

    public CommonPageDTO(List<T> records, Long pageSize, Long pageNum, Long total) {
        this.records = records;
        this.pageSize = pageSize.intValue();
        this.pageNum = pageNum.intValue();
        this.total = total.intValue();
    }

    public static <T> CommonPageDTO<T> of(List<T> records, Long pageSize, Long pageNum, Long total) {
        return new CommonPageDTO<>(records, pageSize, pageNum, total);
    }
}
