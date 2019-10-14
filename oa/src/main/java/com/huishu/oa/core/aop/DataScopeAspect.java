package com.huishu.oa.core.aop;


import com.huishu.oa.core.common.annotion.DataScope;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.core.shiro.ShiroUser;
import com.huishu.oa.core.util.StringUtils;
import com.huishu.oa.modular.BaseModel;
import com.huishu.oa.modular.system.model.Dept;
import com.huishu.oa.modular.system.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据过滤处理
 *
 * @author sf
 */
@Aspect
@Component
@Slf4j
public class DataScopeAspect {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.huishu.oa.core.common.annotion.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 获取当前的用户
        ShiroUser currentUser = ShiroKit.getUser();
        if (currentUser != null) {
            // 如果是超级管理员，则不过滤数据
            if (!ShiroKit.isAdmin()) {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param deptAlias 别名
     * @param userAlias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, ShiroUser user, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();

        for (Integer roleId : user.getRoleList()) {
            Role role = new Role().selectById(roleId);
            String dataScope = role.getDataScope();
            //全部数据权限
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
                //自定数据权限
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                sqlString.append(StringUtils.format(
                        " OR {}.id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getId()));
                //部门数据权限
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.id = {} ", deptAlias, user.getDeptId()));
                //部门及以下数据权限
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                //todo 将部门信息保存到shoirUser
                String pids = new Dept().selectById(user.getDeptId())==null?"":new Dept().selectById(user.getDeptId()).getPids();
                String deptChild = pids+"["+user.getDeptId()+"],";
                sqlString.append(StringUtils.format(
                        " OR {}.id IN ( SELECT id FROM sys_dept WHERE id = {} or pids LIKE '%{}%' )",
                        deptAlias, user.getDeptId(), deptChild));
                // 仅本人数据权限
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.id = {} ", userAlias, user.getUserId()));
                } else {
                    log.info("仅本人数据权限没有获取到本人id");
                }
            }
        }
        if (StringUtils.isNotBlank(sqlString.toString())) {
            BaseModel baseEntity = (BaseModel) joinPoint.getArgs()[0];
            System.out.println(baseEntity);
            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
