package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2021/10/16.
 * auther:lenovo
 * Date：2021/10/16
 */

public class AuthenticationBean implements Serializable {
    private int status;
    private String msg;
    private ResultBean result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }




}
