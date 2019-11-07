package com.wl.springcloud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 〈会员实体〉
 *
 * @author 王柳
 * @date 2019/11/7 9:43
 */
@Data
@TableName("es_member")
public class Member extends Model<Member> {

    @TableId
    private int id;
    private String memberName;
    private String password;
    private String mobile;
    private String email;
    private short sex;
    private Date birthday;
    private Date createTime;
    @TableField(exist = false)
    private Set<Role> roles;

}
