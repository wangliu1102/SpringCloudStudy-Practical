package com.huishu.oa.multi.service.impl;

import com.huishu.oa.core.common.constant.DatasourceEnum;
import com.huishu.oa.multi.entity.Test;
import com.huishu.oa.multi.mapper.TestMapper;
import com.huishu.oa.multi.service.TestService;
import cn.stylefeng.roses.core.mutidatasource.annotion.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zx
 * @since 2018-07-10
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    @DataSource(name = DatasourceEnum.DATA_SOURCE_BIZ)
    @Transactional
    public void testBiz() {
        Test test = new Test();
        test.setBbb("bizTest");
        testMapper.insert(test);
    }

    @Override
    @DataSource(name = DatasourceEnum.DATA_SOURCE_OA)
    @Transactional
    public void testOa() {
        Test test = new Test();
        test.setBbb("oaTest");
        testMapper.insert(test);
    }
}
