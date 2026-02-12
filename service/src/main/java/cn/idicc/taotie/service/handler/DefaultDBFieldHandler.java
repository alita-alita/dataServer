package cn.idicc.taotie.service.handler;

import cn.idicc.common.model.BaseDO;
import cn.idicc.common.model.InputIdBaseDO;
import cn.idicc.identity.interfaces.config.login.UserContext;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类
 * <p>
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author wd
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    public static final String DEFAULT_USER = "system";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

            LocalDateTime now = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseDO.getGmtCreate())) {
                baseDO.setGmtCreate(now);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseDO.getGmtModify())) {
                baseDO.setGmtModify(now);
            }

            String userName = UserContext.getUserName();
            if (Objects.isNull(baseDO.getCreateBy())) {
                if (StringUtils.isNotBlank(userName)) {
                    baseDO.setCreateBy(userName);

                } else {
                    baseDO.setCreateBy(DEFAULT_USER);
                }
            }

            if (Objects.isNull(baseDO.getUpdateBy())) {
                if (StringUtils.isNotBlank(userName)) {
                    baseDO.setUpdateBy(userName);
                } else {
                    baseDO.setUpdateBy(DEFAULT_USER);
                }
            }
        } else if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof InputIdBaseDO) {
            InputIdBaseDO baseDO = (InputIdBaseDO) metaObject.getOriginalObject();

            LocalDateTime now = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseDO.getGmtCreate())) {
                baseDO.setGmtCreate(now);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseDO.getGmtModify())) {
                baseDO.setGmtModify(now);
            }

            String userName = UserContext.getUserName();
            if (Objects.isNull(baseDO.getCreateBy())) {
                if (StringUtils.isNotBlank(userName)) {
                    baseDO.setCreateBy(userName);

                } else {
                    baseDO.setCreateBy(DEFAULT_USER);
                }
            }

            if (Objects.isNull(baseDO.getUpdateBy())) {
                if (StringUtils.isNotBlank(userName)) {
                    baseDO.setUpdateBy(userName);
                } else {
                    baseDO.setUpdateBy(DEFAULT_USER);
                }
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

            LocalDateTime now = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            baseDO.setGmtModify(now);

            String userName = UserContext.getUserName();
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (StringUtils.isNotBlank(userName)) {
                baseDO.setUpdateBy(userName);
            } else {
                baseDO.setUpdateBy(DEFAULT_USER);
            }
        } else if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof InputIdBaseDO) {
            InputIdBaseDO baseDO = (InputIdBaseDO) metaObject.getOriginalObject();

            LocalDateTime now = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            baseDO.setGmtModify(now);

            String userName = UserContext.getUserName();
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (StringUtils.isNotBlank(userName)) {
                baseDO.setUpdateBy(userName);
            } else {
                baseDO.setUpdateBy(DEFAULT_USER);
            }
        }
    }
}
