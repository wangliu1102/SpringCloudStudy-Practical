package com.huishu.oa.modular.office.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.core.common.annotion.DataScope;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.modular.office.dao.LeaveMapper;
import com.huishu.oa.modular.office.model.Leave;
import com.huishu.oa.modular.office.service.ILeaveService;

/**
 * <p>
 * 请假表 服务实现类
 * </p>
 *
 * @author tb
 * @since 2019-06-2
 */
@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements ILeaveService {

    @Resource
    private LeaveMapper leaveMapper;

    @Override
    public void addLeave(Leave leave) {
    	   leave.setId(leave.getId());
           leave.setTitle(leave.getTitle());
           leave.setStartTime(leave.getStartTime());
           leave.setEndTime(leave.getEndTime());
           leave.setContent(leave.getContent());
           leave.setCreateTime(new Date());
           leave.setCreateBy(ShiroKit.getUser().getId().toString());
           leave.setDays(leave.getDays());
           leaveMapper.insert(leave);
    }

    @Override
    @DataScope
    public List<Map<String, Object>> leaveList(Leave leave,Page<Leave> page) {
        return this.leaveMapper.leaveList(leave, page);
	}

    @Override
    public Leave selectById(Integer id) {
        return leaveMapper.selectById(id);
	}

   
}
