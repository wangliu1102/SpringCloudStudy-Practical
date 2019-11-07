package com.wl.springcloud.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wl.springcloud.entity.Role;

import java.util.List;

/**
 * 〈角色Dao〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查找角色列表
     *
     * @param memberId 用户id
     * @return 角色列表
     */
    List<Role> findByMemberId(Integer memberId);
}
