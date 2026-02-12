package cn.idicc.taotie.service.external;

import cn.idicc.identity.common.RPCResult;
import cn.idicc.identity.common.RPCStatus;
import cn.idicc.identity.interfaces.rpc.UserRPCInterface;
import cn.idicc.identity.response.UserDTO;
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
public class UserService {

    @DubboReference(interfaceClass = UserRPCInterface.class, check = false,version = "1.0.0")
    UserRPCInterface userRPCInterface;

    /**
     * 根据用户ID获取用户信息
     * @return
     */
    public UserDTO queryByUserId(Long userId){
        RPCResult rpcResult = userRPCInterface.queryByUserId(userId);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("UserService.queryByUserId failed,userId is[{}],rpcResult is[{}]",userId, JSON.toJSONString(rpcResult));
            throw new BizException("获取用户信息异常");
        }
        return (UserDTO) rpcResult.getData();
    }

    /**
     * 获取机构下所有账号信息
     * @param orgCode
     * @return
     */
    public List<UserDTO> listByOrgCode(String orgCode){
        RPCResult rpcResult = userRPCInterface.listByOrgCode(orgCode);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("UserService.listByOrgCode failed,orgCode is[{}],rpcResult is[{}]",orgCode, JSON.toJSONString(rpcResult));
            throw new BizException("获取机构信息异常");
        }
        return (List<UserDTO>) rpcResult.getData();
    }

    /**
     * 根据机构code获取机构主账号信息
     * @param orgCode
     * @return
     */
    public UserDTO queryMainUserByOrgCode(String orgCode){
        RPCResult rpcResult = userRPCInterface.queryMainUserByOrgCode(orgCode);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("UserService.queryMainUserByOrgCode failed,orgCode is[{}],rpcResult is[{}]",orgCode, JSON.toJSONString(rpcResult));
            throw new BizException("获取机构主账号信息异常");
        }
        return (UserDTO) rpcResult.getData();
    }

    /**
     * 查询部门下所有子部门关联的用户ID集合数据
     * @param deptId
     * @return
     */
    public List<Long> listByChildDeptId(Long deptId){
        RPCResult rpcResult = userRPCInterface.listByChildDeptId(deptId);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("UserService.listByChildDeptId failed,deptId is[{}],rpcResult is[{}]",deptId, JSON.toJSONString(rpcResult));
            throw new BizException("查询部门下所有子部门关联用户信息异常");
        }
        return (List<Long>) rpcResult.getData();
    }

    /**
     * 获取登陆用户所属部门下所有子部门关联的用户
     * @param userId
     * @return
     */
    public List<UserDTO> listAllChildDeptUsers(Long userId,String orgCode){
        RPCResult rpcResult = userRPCInterface.listAllChildDeptUsers(userId,orgCode);
        if(rpcResult.getStatus() != RPCStatus.SUCCESS.status()){
            log.error("UserService.listAllChildDeptUsers failed,userId is[{}],rpcResult is[{}]",userId, JSON.toJSONString(rpcResult));
            throw new BizException("查询用户下所有子部门关联用户信息异常");
        }
        return (List<UserDTO>) rpcResult.getData();
    }

}
