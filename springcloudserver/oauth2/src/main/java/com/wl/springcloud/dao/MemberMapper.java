package com.wl.springcloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wl.springcloud.entity.Member;

/**
 * @author 王柳
 * @date 2019/11/7 10:04
 */
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 根据会员名查找会员
     * @param memberName 会员名
     * @return 会员
     */
    Member findByMemberName(String memberName);
}
