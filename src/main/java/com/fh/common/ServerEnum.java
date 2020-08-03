package com.fh.common;

public enum ServerEnum {
    SUCCESS(200,"操作成功"),
    ERROR(1001,"操作失败"),
    LOGIN_ERROR(1002,"登陆失败"),
    REG_MEMBER_IS_NULL(1003,"信息不能为空"),
    REG_MEMBER_IS_NXIST(1004,"用户名已存在"),
    REG_MEMBER_IS_NPONE(1004,"手机号已存在"),
    PRUDUCT_NOT_EXIST(1005,"商品不存在"),
    PRUDUCT_IS_DUWN(1006,"商品已下架"),
    CART_IS_NULL(1007,"购物车中未有商品，请前往添加商品")
    ;
    private int code;
    private String msg;

    ServerEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ServerEnum() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
