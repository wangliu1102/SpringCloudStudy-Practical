package com.wl.springcloud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @description  〈角色实体〉
 * @author 王柳
 * @date 2019/11/7 9:43
 */
@Data
@TableName("es_role")
public class Role extends Model<Role> {

    @TableId
    private int id;
    private String roleName;
    private short valid;
    private Date createTime;
    private Date updateTime;
    @TableField(exist = false)
    private Set<Permission> permissions;
}
