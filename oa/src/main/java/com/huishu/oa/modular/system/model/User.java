package com.huishu.oa.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huishu.oa.core.common.annotation.Excel;
import com.huishu.oa.modular.BaseModel;
import lombok.Data;
import java.util.Date;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author zx
 * @since 2019-05-11
 */

@TableName("sys_user")
@Data
public class User extends BaseModel<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 账号
     */
    @Excel(name = "账号")
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * md5密码盐
     */
    private String salt;

    /**
     * 名字
     */
    private String name;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     *
     */
    @Excel(name = "性别", readConverterExp = "1=男,2=女")
    private Integer sex;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
    @Excel(name = "状态", readConverterExp = "1=启用,2=冻结,3=删除")
    private Integer status;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String beginTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}
