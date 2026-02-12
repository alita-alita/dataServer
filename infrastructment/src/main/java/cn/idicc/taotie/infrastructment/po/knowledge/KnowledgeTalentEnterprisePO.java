package cn.idicc.taotie.infrastructment.po.knowledge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Set;

/**
 * @author guyongliang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "knowledge_talent_enterprise", createIndex = false)
public class KnowledgeTalentEnterprisePO {

	@Id
	private String id;

	/**
	 * 人员md5
	 */
	private String talentMd5;

	/**
	 * 企业id
	 */
	private String enterpriseId;

	/**
	 * 人才姓名
	 */
	private String talentName;

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业联系方式
	 */
	private String enterpriseMobile;
	/**
	 * 人才职位
	 */
	private String occupation;

	/**
	 * 人才籍贯-市
	 */
	private List<String> talentAncestorCode;

	private String talentProvince;

	/**
	 * 人才籍贯-市
	 */
	private String talentCity;

	/**
	 * 人才籍贯-区
	 */
	private String talentArea;

	/**
	 * 企业注册地-市
	 */
	private String enterpriseCity;

	/**
	 * 企业注册地-省
	 */
	private String enterpriseProvince;

	/**
	 * 企业注册地-区
	 */
	private String enterpriseArea;

	/**
	 * 企业注册地
	 */
	private String enterpriseAddress;

	/**
	 * 研究方向/创新方向
	 */
	private String researchField;

	/**
	 * 企业规模
	 */
	private String enterpriseScale;

	/**
	 * 人才关联企业
	 */
	private String uniCode;

	/**
	 * 建立时间
	 */
	private Long establishment;

	/**
	 * 注册资本
	 */
	private String registeredCapital;
	/**
	 * 融资轮次
	 */
	private String financingRounds;
	/**
	 * 上市板块
	 */
	private String listedSector;

	/**
	 * 科技标签
	 */
	private List<Long> technologicalInnovation;

	/**
	 * 修改时间,目前为存储新增时间，有新增才会有会更新红点标识
	 */
	private Long      modifyTime;

	/**
	 * 关联的产业链id集合
	 */
	private Set<Long> chainIds;


}
