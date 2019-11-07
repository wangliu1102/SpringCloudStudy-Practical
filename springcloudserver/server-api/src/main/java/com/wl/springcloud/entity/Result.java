package com.wl.springcloud.entity;

import lombok.Data;

/**
 * @description  〈响应实体〉
 * @author 王柳
 * @date 2019/11/7 9:43
 */
@Data
public class Result {

    private int code;
    private String message;
    private Object data;

}
