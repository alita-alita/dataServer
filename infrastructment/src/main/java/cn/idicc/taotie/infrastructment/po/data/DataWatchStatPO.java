package cn.idicc.taotie.infrastructment.po.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "data_watch_stats", createIndex = false)
public class DataWatchStatPO {
	/**
	 * 定义的是es中的主键id
	 */
	@Id
	@Field(type = FieldType.Keyword)
	private String id;

	/**
	 * 统计类型（DataWatchTypeEnum）
	 */
	private Integer dataType;

	/**
	 * 统计数值【业务数据】
	 */
	private Long odsCount;

    /**
     * 统计数值【数据组数据】
     */
    private Long dwdCount;

    /**
	 * 产业链ID
	 */
	private Long chainId;

	/**
	 * 记录日期
	 */
	@JsonFormat(
			pattern = "yyyy-MM-dd",
			timezone = "GMT+8" // 东八区，匹配 ES 时区（避免日期偏移）
	)
	private LocalDate recordDate; // 改用 LocalDate 替代 Date

	/**
	 * 地区编码
	 */
	private String regionCode;
}
