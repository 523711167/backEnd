package org.pindaodao.pojo;

import lombok.Data;

import static org.pindaodao.pojo.S.S_1000;
import static org.pindaodao.pojo.S.S_5000;

@Data
public class SS<T> {

    private static final long serialVersionUID = 4580737268023862568L;

    private Integer code;

    private String msg;

    private T data;

    public SS() {

    }

    public SS(Integer code) {
        this.code = code;
    }

    //是否成功（自定义结果码为1000为成功）
    public boolean isSuccess() {
        return this.code == 1000;
    }

    //成功时引用
    public static <T> SS<T> success() {
        return success(S_1000);
    }

    public static <T> SS<T> success(T data) {
        return success(S_1000, data);
    }

    public static <T> SS<T> success(S re) {
        return success(re, null);
    }

    public static <T> SS<T> success(S re, T data) {
        Integer code = re.getCode();
        String msg = re.getMsg();
        return success(code, msg, data);
    }

    public static <T> SS<T> success(Integer code, String msg, T data) {
        SS<T> result = new SS<>(1000);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    //失败时引用
    public static <T> SS<T> fail() {
        return fail(S_5000);
    }

    public static <T> SS<T> fail(S re) {
        return fail(re, null);
    }

    public static <T> SS<T> fail(String msg) {
        return fail(S_5000.getCode(), msg, null);
    }

    public static <T> SS<T> fail(T data) {
        return fail(S_5000, data);
    }

    public static <T> SS<T> fail(S re, T data) {
        Integer code = re.getCode();
        String msg = re.getMsg();
        return fail(code, msg, data);
    }

    public static <T> SS<T> fail(Integer code, String msg, T data) {
        SS<T> result = new SS<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
