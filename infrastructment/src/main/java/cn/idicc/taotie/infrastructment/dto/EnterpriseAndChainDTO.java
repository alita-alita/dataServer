package cn.idicc.taotie.infrastructment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseAndChainDTO {

    private List<String> enterpriseUnicode;

    private Long chainId;

    private Long memberEnterpriseCount;
}
