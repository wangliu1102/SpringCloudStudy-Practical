package com.huishu.oa.multi.test;

import com.huishu.oa.base.BaseJunit;
import com.huishu.oa.multi.service.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 业务测试
 *
 * @author zx
 * @Date 2019-06-23 23:12
 */
public class BizTest extends BaseJunit {

    @Autowired
    private TestService testService;

    @Test
    public void test() {
        testService.testOa();

        testService.testBiz();
    }
}
