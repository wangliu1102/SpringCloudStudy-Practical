package com.huishu.oa.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;
import com.huishu.oa.core.common.constant.dictmap.factory.DictFieldWrapperFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 对比两个对象的变化的工具类
 *
 * @author zx
 * @Date 2019/3/31 10:36
 */
public class Contrast {

    //记录每个修改字段的分隔符
    public static final String separator = ";;;";

    /**
     * 比较两个对象,并返回不一致的信息
     *
     * @author zx
     * @Date 2019/5/9 19:34
     */
    public static String contrastObj(Object pojo1, Object pojo2) {
        String str = "";
        try {
            Class clazz = pojo1.getClass();
            Field[] fields = pojo1.getClass().getDeclaredFields();
            int i = 1;
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(pojo1);
                Object o2 = getMethod.invoke(pojo2);
                if (o1 == null || o2 == null) {
                    continue;
                }
                if (o1 instanceof Date) {
                    o1 = DateUtil.formatDate((Date) o1);
                }
                if (!o1.toString().equals(o2.toString())) {
                    if (i != 1) {
                        str += separator;
                    }
                    str += "字段名称" + field.getName() + ",旧值:" + o1 + ",新值:" + o2;
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 比较两个对象pojo1和pojo2,并输出不一致信息
     *
     * @author zx
     * @Date 2019/5/9 19:34
     */
    public static String contrastObj(Class dictClass, String key, Object pojo1, Map<String, String> pojo2) throws IllegalAccessException, InstantiationException {
        AbstractDictMap dictMap = (AbstractDictMap) dictClass.newInstance();
        String str = parseMutiKey(dictMap, key, pojo2) + separator;
        try {
            Class clazz = pojo1.getClass();
            Field[] fields = pojo1.getClass().getDeclaredFields();
            int i = 1;
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(pojo1);
                Object o2 = pojo2.get(StrUtil.lowerFirst(getMethod.getName().substring(3)));
                if (o1 == null || o2 == null) {
                    continue;
                }
                if (o1 instanceof Date) {
                    o1 = DateUtil.formatDate((Date) o1);
                } else if (o1 instanceof Integer) {
                    o2 = Integer.parseInt(o2.toString());
                }
                if (!o1.toString().equals(o2.toString())) {
                    if (i != 1) {
                        str += separator;
                    }
                    String fieldName = dictMap.get(field.getName());
                    String fieldWrapperMethodName = dictMap.getFieldWrapperMethodName(field.getName());
                    if (fieldWrapperMethodName != null) {
                        Object o1Wrapper = DictFieldWrapperFactory.createFieldWrapper(o1, fieldWrapperMethodName);
                        Object o2Wrapper = DictFieldWrapperFactory.createFieldWrapper(o2, fieldWrapperMethodName);
                        str += "字段名称:" + fieldName + ",旧值:" + o1Wrapper + ",新值:" + o2Wrapper;
                    } else {
                        str += "字段名称:" + fieldName + ",旧值:" + o1 + ",新值:" + o2;
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 比较两个对象pojo1和pojo2,并输出不一致信息
     *
     * @author zx
     * @Date 2019/5/9 19:34
     */
    public static String contrastObjByName(Class dictClass, String key, Object pojo1, Map<String, String> pojo2) throws IllegalAccessException, InstantiationException {
        AbstractDictMap dictMap = (AbstractDictMap) dictClass.newInstance();
        String str = parseMutiKey(dictMap, key, pojo2) + separator;
        try {
            Class clazz = pojo1.getClass();
            Field[] fields = pojo1.getClass().getDeclaredFields();
            int i = 1;
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                String prefix = "get";
                int prefixLength = 3;
                if (field.getType().getName().equals("java.lang.Boolean")) {
                    prefix = "is";
                    prefixLength = 2;
                }
                Method getMethod = null;
                try {
                    getMethod = clazz.getDeclaredMethod(prefix + StrUtil.upperFirst(field.getName()));
                } catch (java.lang.NoSuchMethodException e) {
                    System.err.println("this className:" + clazz.getName() + " is not methodName: " + e.getMessage());
                    continue;
                }
                Object o1 = getMethod.invoke(pojo1);
                Object o2 = pojo2.get(StrUtil.lowerFirst(getMethod.getName().substring(prefixLength)));
                if (o1 == null || o2 == null) {
                    continue;
                }
                if (o1 instanceof Date) {
                    o1 = DateUtil.formatDate((Date) o1);
                } else if (o1 instanceof Integer) {
                    o2 = Integer.parseInt(o2.toString());
                }
                if (!o1.toString().equals(o2.toString())) {
                    if (i != 1) {
                        str += separator;
                    }
                    String fieldName = dictMap.get(field.getName());
                    String fieldWrapperMethodName = dictMap.getFieldWrapperMethodName(field.getName());
                    if (fieldWrapperMethodName != null) {
                        Object o1Wrapper = DictFieldWrapperFactory.createFieldWrapper(o1, fieldWrapperMethodName);
                        Object o2Wrapper = DictFieldWrapperFactory.createFieldWrapper(o2, fieldWrapperMethodName);
                        str += "字段名称:" + fieldName + ",旧值:" + o1Wrapper + ",新值:" + o2Wrapper;
                    } else {
                        str += "字段名称:" + fieldName + ",旧值:" + o1 + ",新值:" + o2;
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 解析多个key(逗号隔开的)
     *
     * @author zx
     * @Date 2019/5/16 22:19
     */
    public static String parseMutiKey(AbstractDictMap dictMap, String key, Map<String, String> requests) {
        StringBuilder sb = new StringBuilder();
        if (key.contains(",")) {
            String[] keys = key.split(",");
            for (String item : keys) {
                String fieldWrapperMethodName = dictMap.getFieldWrapperMethodName(item);
                String value = requests.get(item);
                if (fieldWrapperMethodName != null) {
                    Object valueWrapper = DictFieldWrapperFactory.createFieldWrapper(value, fieldWrapperMethodName);
                    sb.append(dictMap.get(item) + "=" + valueWrapper + ",");
                } else {
                    sb.append(dictMap.get(item) + "=" + value + ",");
                }
            }
            return StrUtil.removeSuffix(sb.toString(), ",");
        } else {
            String fieldWrapperMethodName = dictMap.getFieldWrapperMethodName(key);
            String value = requests.get(key);
            if (fieldWrapperMethodName != null) {
                Object valueWrapper = DictFieldWrapperFactory.createFieldWrapper(value, fieldWrapperMethodName);
                sb.append(dictMap.get(key) + "=" + valueWrapper);
            } else {
                sb.append(dictMap.get(key) + "=" + value);
            }
            return sb.toString();
        }
    }

}