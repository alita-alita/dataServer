package cn.idicc.taotie.provider.api.service;

import cn.idicc.taotie.provider.api.common.RPCResult;

/**
 * @Author: MengDa
 * @Date: 2025/3/10
 * @Description:
 * @version: 1.0
 */
public interface EnterpriseSyncRpcService {

    /**
     * 依据企业ID同步ES
     * @param uniCode 社会信用代码
     * @return
     */
    RPCResult<String> updateByUnicode(String uniCode);

    /**
     * 同步指定产业链，指产业链企业关系表中，所有与该产业链相关企业，包含已删除
     * @param chainId
     * @return
     */
    RPCResult<String> updateByChain(Long chainId);

    /**
     * 同步所有企业发展指标
     * @return
     */
    RPCResult<String> updateAllDevelopmentIndex();

    /**
     * 依据社会信用代码同步ES
     * @param uniCode 社会信用代码
     * @return
     */
    RPCResult<String> updateDevelopmentIndexByUnicode(String uniCode);

    /**
     * 同步所有企业籍贯
     * @return
     */
    RPCResult<String> updateAllAncestor();

    /**
     * 同步所有企业校友
     * @return
     */
    RPCResult<String> updateAllAcademia();
}
