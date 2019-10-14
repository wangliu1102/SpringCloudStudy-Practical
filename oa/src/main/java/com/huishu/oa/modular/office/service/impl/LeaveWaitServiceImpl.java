package com.huishu.oa.modular.office.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.core.common.annotion.DataScope;
import com.huishu.oa.modular.office.dao.LeaveWaitMapper;
import com.huishu.oa.modular.office.model.Leave;
import com.huishu.oa.modular.office.service.ILeaveWaitService;

/**
 * <p>
 * 请假待审批表服务实现类
 * </p>
 *
 * @author caowy
 * @since 2019-09-25
 */
@Service
public class LeaveWaitServiceImpl extends ServiceImpl<LeaveWaitMapper, Leave> implements ILeaveWaitService {

	
	
    @Resource
    private LeaveWaitMapper leaveWaitMapper;

 
    /**
     * 获取请假列表
     */
    @Override
    @DataScope
    public List<Map<String, Object>> leaveWaitList(Leave leave,Page<Leave> page) {
        return this.leaveWaitMapper.leaveList(leave,page);
    }

    /**
     * 查询请假信息
     */
    @Override
    public Leave selectById(Integer id) {
        return leaveWaitMapper.selectById(id);
    }

   /**
    * 修改请假状态
    */
	@Override
	public int setStatus(Integer leaveId, int status) {
		return this.baseMapper.setStatus(leaveId, status);
	}


}
