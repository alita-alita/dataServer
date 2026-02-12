package cn.idicc.taotie.infrastructment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipDTO {
	private String counterpartEnterpriseName;
	private String hisRelationship;
	private String hisRelationshipDegree;

}
