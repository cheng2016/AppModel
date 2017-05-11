package com.uniaip.android.base;

/**
 * 作者: ysc
 * 时间: 2017/4/20
 */

public class BaseModel {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOK() {
        if (code.equals("200"))
            return true;
        else
            return false;
    }
}
