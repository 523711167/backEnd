package org.pindaodao.pojo;

public enum S {


    S_1000(1000, "成功"),
    S_5000(5000,"系统异常");


    private Integer code;
    private String msg;

    S(Integer code , String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
