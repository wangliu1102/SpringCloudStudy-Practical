package com.huishu.oa.core.common.annotation;

import com.huishu.oa.core.common.constant.dictmap.base.AbstractDictMap;
import com.huishu.oa.core.common.constant.dictmap.base.SystemDict;

import java.lang.annotation.*;

/**
 * 标记需要做业务日志的方法
 *
 * @author zx
 * @Date 2019-03-31 12:46
 */

/**
 * 作用目标
 * ElementType.CONSTRUCTOR          构造方法声明
 * ElementType.FIELD                字段声明
 * ElementType.LOCAL_VARIABLE       局部变量申明
 * ElementType.METHOD               方法声明
 * ElementType.PACKAGE              包声明
 * ElementType.PARAMETER            参数声明
 * ElementType.TYPE                 类接口
 *
 * 保留
 * RetentionPolicy.SOURCE        只在源码显示，编译时会丢弃
 * RetentionPolicy.CLASS         编译时会记录到class中，运行时忽
 * RetentionPolicy.RUNTIME       运行时存在，可以通过发射读取
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BusinessLog {

    /**
     * 业务的名称,例如:"修改菜单"
     */
    String value() default "";

    /**
     * 被修改的实体的唯一标识,例如:菜单实体的唯一标识为"id"
     */
    String key() default "id";

    /**
     * 字典(用于查找key的中文名称和字段的中文名称)
     */
    Class<? extends AbstractDictMap> dict() default SystemDict.class;
}
