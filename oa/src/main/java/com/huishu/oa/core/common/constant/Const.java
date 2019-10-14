package com.huishu.oa.core.common.constant;

/**
 * 系统常量
 *
 * @author zx
 * @Date 2019年2月12日 下午9:42:53
 */
public interface Const {

    /**
     * 系统默认的管理员密码
     */
    String DEFAULT_PWD = "111111";

    /**
     * 管理员角色的名字
     */
    String ADMIN_NAME = "administrator";

    /**
     * 管理员id
     */
    Integer ADMIN_ID = 1;

    /**
     * 超级管理员角色id
     */
    Integer ADMIN_ROLE_ID = 1;

    /**
     * 接口文档的菜单名
     */
    String API_MENU_NAME = "接口文档";

    /**
     * 通用成功标识
     */
    String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    String FAIL = "1";

    /**
     * 签到成功标识
     */
    String SIGNSUCCESS = "打卡成功";
    /**
     * 重复打卡提示标识
     */
    String SIGNAGIN = "今日已打卡，不用重复打卡！";

    /**
     * 报销文件上传地址
     */
    String EXPENSE_FILE = "expense/";

    /**
     * 4个小时标识
     */
    double FOUR =4.0;

    /**
     * 零小时标识
     */
    long ZERO = 0;

    /**
     * 半天标识
     */
    double HALF_DAY =0.5;

    /**
     * 一天标识
     */
    double ONE_DAY =1.0;
}
