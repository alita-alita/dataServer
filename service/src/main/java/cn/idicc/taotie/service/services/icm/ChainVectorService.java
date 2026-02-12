package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.po.icm.IndustryChainProductEmbeddingPO;
import cn.idicc.taotie.infrastructment.response.icm.chain.IndustryChainProductEmbeddingDTO;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/3/25
 * @Description:
 * @version: 1.0
 */
public interface ChainVectorService {

    void buildVector(Long chainId);

    void deleteVector(Long chainId);

    List<IndustryChainProductEmbeddingDTO> recall(String keyword, Integer limit);
}
