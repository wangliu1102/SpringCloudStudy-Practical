package com.huishu.oa.modular.office.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.core.common.annotion.DataScope;
import com.huishu.oa.modular.office.dao.OvertimeMapper;
import com.huishu.oa.modular.office.model.Overtime;
import com.huishu.oa.modular.office.model.Weekly;
import com.huishu.oa.modular.office.service.IOvertimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 请假表 服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-06-2
 */
@Service
public class OvertimeServiceImpl extends ServiceImpl<OvertimeMapper, Overtime> implements IOvertimeService {

    @Resource
    private OvertimeMapper overtimeMapper;

    @Override
    public List<Overtime> overtimeList(Page<Overtime> page, String condition, String startTime, String endTime) {
        return this.overtimeMapper.overtimeList(page,condition,startTime,endTime);
    }


}
