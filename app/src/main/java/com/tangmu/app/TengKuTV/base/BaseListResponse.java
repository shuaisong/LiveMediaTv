package com.tangmu.app.TengKuTV.base;

import java.io.Serializable;
import java.util.List;

public class BaseListResponse<T> implements Serializable {
    private String msg;
    private int status;
    private List<T> result;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
