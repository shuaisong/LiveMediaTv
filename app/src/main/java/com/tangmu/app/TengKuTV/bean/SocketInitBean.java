package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

public class SocketInitBean implements Serializable {

    /**
     * type : init
     * client_id : 7f0000010bd7000000d3
     */

    private String type;
    private String client_id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
}
