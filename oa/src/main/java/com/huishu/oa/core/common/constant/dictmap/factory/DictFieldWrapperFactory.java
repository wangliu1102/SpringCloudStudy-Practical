package com.huishu.oa.core.common.constant.dictmap.factory;

import com.huishu.oa.core.common.constant.factory.ConstantFactory;
import com.huishu.oa.core.common.constant.factory.IConstantFactory;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

import java.lang.reflect.Method;

/**
 * 字典字段的包装器(从ConstantFactory中获取包装值)
 *
 * @author zx
 * @Date 2019-05-06 15:12
 */
public class DictFieldWrapperFactory {

    public static Object createFieldWrapper(Object parameter, String methodName) {
        IConstantFactory constantFactory = ConstantFactory.me();
        try {
            Method method = IConstantFactory.class.getMethod(methodName, parameter.getClass());
            return method.invoke(constantFactory, parameter);
        } catch (Exception e) {
            try {
                Method method = IConstantFactory.class.getMethod(methodName, Integer.class);
                return method.invoke(constantFactory, Integer.parseInt(parameter.toString()));
            } catch (Exception e1) {
                throw new ServiceException(BizExceptionEnum.ERROR_WRAPPER_FIELD);
            }
        }
    }

}
