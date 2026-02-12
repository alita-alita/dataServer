package cn.idicc.taotie.service.external;

import cn.idicc.identity.common.RPCResult;
import cn.idicc.identity.common.RPCStatus;
import cn.idicc.identity.interfaces.rpc.InstitutionRPCInterface;
import cn.idicc.identity.request.AdministrativeDivisionRequest;
import cn.idicc.identity.response.InstitutionDTO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wd
 * @description 获取机构信息相关接口
 * @date 10/31/23 10:28 AM
 */
@Component
@Slf4j
public class InstitutionService {

    @DubboReference(interfaceClass = InstitutionRPCInterface.class, check = false,version = "1.0.0")
    InstitutionRPCInterface institutionRPCInterface;

    /**
     * 根据机构code获取机构信息
     * @return
     */
    public InstitutionDTO queryByOrgCode(String orgCode){
        RPCResult rpcResult = institutionRPCInterface.queryByOrgCode(orgCode);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("InstitutionService.queryByOrgCode failed,orgCode is[{}],rpcResult is[{}]",orgCode, JSON.toJSONString(rpcResult));
            throw new BizException("获取机构信息异常");
        }
        return (InstitutionDTO) rpcResult.getData();
    }

    /**
     * 根据机构ID获取机构信息
     * @return
     */
    public InstitutionDTO getById(Long orgId){
        RPCResult rpcResult = institutionRPCInterface.queryByOrgId(orgId);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("InstitutionService.getById failed,orgId is[{}],rpcResult is[{}]",orgId, JSON.toJSONString(rpcResult));
            throw new BizException("获取机构信息异常");
        }
        return (InstitutionDTO) rpcResult.getData();
    }

    /**
     * 根据机构名称获取机构信息
     * @return
     */
    public InstitutionDTO queryByOrgName(String orgName){
        RPCResult rpcResult = institutionRPCInterface.queryByOrgName(orgName);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("InstitutionService.queryByOrgName failed,orgName is[{}],rpcResult is[{}]",orgName, JSON.toJSONString(rpcResult));
            throw new BizException("获取机构信息异常");
        }
        return (InstitutionDTO) rpcResult.getData();
    }

    /**
     * 行政区划变更时，更新机构的行政区划数据
     * @param oldData
     * @param newData
     */
    public void administrativeDivisionChange(AdministrativeDivisionRequest oldData,AdministrativeDivisionRequest newData){
        RPCResult rpcResult = institutionRPCInterface.administrativeDivisionChange(oldData,newData);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("InstitutionService.administrativeDivisionChange failed,oldData is[{}],newData is[{}],rpcResult is[{}]",JSON.toJSONString(oldData),JSON.toJSONString(newData),JSON.toJSONString(rpcResult));
            throw new BizException("获取机构信息异常");
        }
    }

    /**
     * 获取所有的入驻机构
     * @return
     */
    public List<InstitutionDTO> getAll(){
        RPCResult rpcResult = institutionRPCInterface.getAll();
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("InstitutionService.getAll failed,rpcResult is[{}]", JSON.toJSONString(rpcResult));
            throw new BizException("获取机构信息异常");
        }
        return (List<InstitutionDTO>) rpcResult.getData();
    }

    /**
     * 根据机构id批量查询机构信息
     * @return
     */
    public List<InstitutionDTO> getByOrgIds(List<Long> orgIds){
        RPCResult rpcResult = institutionRPCInterface.queryByOrgIds(orgIds);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("InstitutionService.getByOrgIds failed,rpcResult is[{}]", JSON.toJSONString(rpcResult));
            throw new BizException("获取机构信息异常");
        }
        return (List<InstitutionDTO>) rpcResult.getData();
    }
}
