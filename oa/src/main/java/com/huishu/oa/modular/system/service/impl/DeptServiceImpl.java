package com.huishu.oa.modular.system.service.impl;

import com.huishu.oa.core.common.node.ZTreeNode;
import com.huishu.oa.modular.system.dao.DeptMapper;
import com.huishu.oa.modular.system.model.Dept;
import com.huishu.oa.modular.system.service.IDeptService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 部门服务
 *
 * @author zx
 * @Date 2019/10/15 下午11:39
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    @Transactional
    public void deleteDept(Integer deptId) {
        Dept dept = deptMapper.selectById(deptId);

        Wrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("pids", "%[" + dept.getId() + "]%");
        List<Dept> subDepts = deptMapper.selectList(wrapper);
        for (Dept temp : subDepts) {
            temp.deleteById();
        }

        dept.deleteById();
    }

    @Override
    public List<ZTreeNode> tree() {
        return this.baseMapper.tree();
    }

    @Override
    public List<ZTreeNode> deptTreeListByRoleId(List<Long> deptIds) {
        return deptMapper.deptTreeListByRoleId(deptIds);
    }

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }

    @Override
    public List<Long> getDeptIdsByRoleId(Integer roleId) {
        return deptMapper.getDeptIdsByRoleId(roleId);
    }
}
