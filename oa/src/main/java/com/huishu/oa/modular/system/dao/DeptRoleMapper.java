package com.huishu.oa.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.huishu.oa.modular.system.model.DeptRole;

import java.util.List;

/**
 * @Author sf
 * @Date 2019/7/18 16:57
 * @Description 角色部门关联类
 **/
public interface DeptRoleMapper extends BaseMapper<DeptRole> {

    void delDeptRoleByRoleId(Integer roleId);

    /**
     * 批量插入数据
     */
    int insertBatch(List<DeptRole> deptRoles);
}
