package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

public class LProgram implements Serializable {


    /**
     * dates1 : 09:22
     * dates2 : 02:32
     * content : 你咋那么美
     */

    private String dates1;
    private String dates2;
    private String content;

    public String getDates1() {
        return dates1;
    }

    public void setDates1(String dates1) {
        this.dates1 = dates1;
    }

    public String getDates2() {
        return dates2;
    }

    public void setDates2(String dates2) {
        this.dates2 = dates2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
