package org.example.clipflow.utils;

import lombok.Data;

@Data
public class R<T> {
    private T type;
    private boolean state;
    private int code;
    private String message;
    private Object data;
    private long count;

    public static R success() {
        R r = new R();
        r.setState(true);
        r.setCode(0);
        r.setMessage("成功");
        r.setData(null);
        return r;
    }

    public static R error() {
        R r = new R();
        r.setState(false);
        r.setCode(1);
        r.setMessage("失败");
        r.setData(null);
        return r;
    }

    public R setMessage(String message) {
        this.message = message;
        return this;
    }

    public R setData(Object data) {
        this.data = data;
        return this;
    }
}
