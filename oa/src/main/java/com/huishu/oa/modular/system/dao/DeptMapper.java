package com.huishu.oa.modular.system.dao;

import com.huishu.oa.core.common.node.ZTreeNode;
import com.huishu.oa.modular.system.model.Dept;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author zx
 * @since 2019-05-11
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);


    /**
     * 获取所有部门列表
     */
    List<ZTreeNode> deptTreeListByRoleId(List<Long> deptIds);
    /**
     * 根据条件查询部门
     *
     * @return
     * @Date 2019年2月12日 下午9:14:34
     */
    List<Long> getDeptIdsByRoleId(@Param("roleId") Integer roleId);

}