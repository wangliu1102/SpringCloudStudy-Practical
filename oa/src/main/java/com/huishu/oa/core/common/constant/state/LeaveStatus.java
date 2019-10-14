package com.huishu.oa.core.common.constant.state;

/**
 * 请假的状态
 *
 * @author caowye
 * @Date 2019年9月25日 下午15：18
 */
public enum LeaveStatus {

    WAIT(1, "待批准"), AGREE(2, "批准"), UNAGREE(3, "驳回");

    int code;
    String message;

    LeaveStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer value) {
        if (value == null) {
            return "";
        } else {
            for (LeaveStatus ms : LeaveStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
