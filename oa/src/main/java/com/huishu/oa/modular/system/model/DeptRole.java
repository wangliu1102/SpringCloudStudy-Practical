package com.huishu.oa.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色和部门关联表
 * </p>
 *
 * @author sf
 * @since 2019-05-11
 */
@TableName("sys_role_dept")
@Data
public class DeptRole {


    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 角色id
     */
    private Integer roleId;


}
