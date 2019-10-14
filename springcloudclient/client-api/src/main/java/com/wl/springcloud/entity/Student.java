package com.wl.springcloud.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 王柳
 * Date 2019/10/2 15:28
 * version:1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("student")
public class Student extends Model<Student> {

    Integer id;

    String userName;

    Integer age;

}
