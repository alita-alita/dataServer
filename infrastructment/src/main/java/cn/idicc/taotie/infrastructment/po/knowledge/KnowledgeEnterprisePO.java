package cn.idicc.taotie.infrastructment.po.knowledge;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.dto.ExcludeRecommendRelationDTO;
import cn.idicc.taotie.infrastructment.dto.RecommendRelationDTO;
import cn.idicc.taotie.infrastructment.dto.RelationshipDTO;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Set;

/**
 * 乡贤企业
 * 乡贤/校友/链式-复用该索引结构
 *
 * @author: guyongliang
 * @date: 2025-10-31
 * @since v4.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "knowledge_enterprise", createIndex = false)
public class KnowledgeEnterprisePO {

	private Long id;

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 统一社会信用代码
	 */
	private String unifiedSocialCreditCode;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区县
	 */
	private String area;

	private Long registerDate;

	/**
	 * 企业地址
	 */
	private String enterpriseAddress;

	private String legalPerson;

	/**
	 * 所属产业链（多条）
	 */
	private List<Long> chainIds;

	/**
	 * 注册资本(元)
	 */
	private String registeredCapitalYuan;

	/**
	 * 企业关联的投资机构
	 * */
	private Set<String> referenceInvestmentOrganization;

	/**
	 * 企业关联的协会
	 */
	private Set<String> referenceAssociationNames;

	/**
	 * 企业关联高校
	 *
	 */
	private Set<String> referenceAcademiaNames;

	/**
	 * 企业标签
	 */
	private List<Long> enterpriseLabelIds;

	/**
	 * 企业产品
	 */
	private List<String> enterpriseProducts;

	/**
	 * 企业扩张指数评分
	 */
	private Double expansionIndex;

	/**
	 * 企业成长指数
	 * */
	private Double growthIndex;

	/**
	 * 籍贯code，每一个code存三份，分别为AABBCC,AA0000,AABB00
	 */
	private List<String> ancestorCode;

	/**
	 * 学校code，每一个code存三份，分别为AABBCC,AA0000,AABB00
	 */
	private List<String> academicRegionCode;

	/**
	 * 对应学校Md5
	 */
	private List<String> academicMd5s;

	/**
	 * 关联的链式企业统一社会信用代码列表
	 */
	private List<String> partnerUniCodes;

	/**
	 * 创业项目Md5
	 */
	private List<String> projectNames;

	/**
	 * 索引类型（0乡贤1校友2链式3创业）
	 */
	private List<Long> indexTypes;

	/**
	 * 链式关系
	 */
	@Field(type = FieldType.Nested)
	private List<RelationshipDTO> relationships;

	/**
	 * 推荐关系
	 */
	@Field(type = FieldType.Nested)
	private List<RecommendRelationDTO> recommendRelations;

	/**
	 * 回避推荐关系
	 */
	@Field(type = FieldType.Nested)
	private List<ExcludeRecommendRelationDTO> excludeRecommendRelations;

	private String enterpriseSummary;

	public static KnowledgeEnterprisePO adapt(EnterprisePO param) {
		return BeanUtil.copyProperties(param, KnowledgeEnterprisePO.class);
	}
}
