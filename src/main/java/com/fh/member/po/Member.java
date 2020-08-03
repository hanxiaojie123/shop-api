package com.fh.member.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_member")
public class Member implements Serializable {
    public Integer id;
    public String memberName;
    public String password;//    【密码】
    public String phone ;//      【电话】
    @TableField(exist = false)
    public String code;
}
