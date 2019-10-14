package com.huishu.oa.modular.office.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.huishu.oa.modular.office.dao.WeeklyMapper;
import com.huishu.oa.modular.office.model.Weekly;
import com.huishu.oa.modular.office.service.IWeeklyService;

@Service
public class WeeklyServiceImpl extends ServiceImpl<WeeklyMapper, Weekly> implements IWeeklyService {


    @Resource
    private WeeklyMapper weeklyMapper;


    @Override
    public void addWeekly(Weekly weekly) {
        weeklyMapper.insert(weekly);

    }

    @Override
    public List<Weekly> weeklyList(Page<Weekly> page, String condition,String startTime,String endTime) {
        return this.weeklyMapper.weeklyList(page,condition,startTime,endTime);
    }


}
