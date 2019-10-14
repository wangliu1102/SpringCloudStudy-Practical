package com.huishu.oa.modular.system.service;

import com.huishu.oa.core.common.node.ZTreeNode;
import com.huishu.oa.modular.system.model.Dept;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门服务
 *
 * @author zx
 * @Date 2019-04-27 17:00
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 删除部门
     */
    void deleteDept(Integer deptId);

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();
    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> deptTreeListByRoleId(List<Long> deptIds);

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(String condition);

    /**
     * 根据条件查询部门
     *
     * @return
     * @Date 2019年2月12日 下午9:14:34
     */
    List<Long> getDeptIdsByRoleId(@Param("roleId") Integer roleId);
}
