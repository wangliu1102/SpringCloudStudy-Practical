package com.huishu.oa.modular.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.core.common.node.ZTreeNode;
import com.huishu.oa.core.util.ToolUtil;
import com.huishu.oa.modular.system.dao.DeptRoleMapper;
import com.huishu.oa.modular.system.dao.RelationMapper;
import com.huishu.oa.modular.system.dao.RoleMapper;
import com.huishu.oa.modular.system.model.Dept;
import com.huishu.oa.modular.system.model.DeptRole;
import com.huishu.oa.modular.system.model.Relation;
import com.huishu.oa.modular.system.model.Role;
import com.huishu.oa.modular.system.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色服务
 *
 * @author zx
 * @Date 2019/10/15 下午11:40
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RelationMapper relationMapper;
    @Resource
    private DeptRoleMapper deptRoleMapper;



    @Override
    @Transactional
    public void setAuthority(Integer roleId, String ids) {

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Convert.toLongArray(ids.split(","))) {
            Relation relation = new Relation();
            relation.setRoleId(roleId);
            relation.setMenuId(id);
            this.relationMapper.insert(relation);
        }
    }


    @Override
    @Transactional
    public void delRoleById(Integer roleId) {
        //删除角色
        this.roleMapper.deleteById(roleId);

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);
    }

    @Override
    public List<Map<String, Object>> selectRoles(String condition) {
        return this.baseMapper.selectRoles(condition);
    }

    @Override
    public int deleteRolesById(Integer roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    @Override
    public List<ZTreeNode> roleTreeList() {
        return this.baseMapper.roleTreeList();
    }

    @Override
    public void DateAuthority(Integer roleId, String depIds) {
        deptRoleMapper.delDeptRoleByRoleId(roleId);
        List<DeptRole> deptRoles = new ArrayList<>();
        //添加新的数据权限
        for (Long id : Convert.toLongArray(depIds.split(","))) {
            DeptRole deptRole = new DeptRole();
            deptRole.setRoleId(roleId);
            deptRole.setDeptId(id);
            deptRoles.add(deptRole);
        }
        if(ToolUtil.isNotEmpty(deptRoles)){
            deptRoleMapper.insertBatch(deptRoles);
        }
    }

    @Override
    public List<ZTreeNode> roleTreeListByRoleId(String[] roleId) {
        return this.baseMapper.roleTreeListByRoleId(roleId);
    }

}
