package com.tangmu.app.TengKuTV.bean;

/**
 * Created by lenovo on 2021/10/16.
 * auther:lenovo
 * Date：2021/10/16
 */

public class ResultBean {
    private AttributesBeanX attributes;
    private HeaderBean header;
    private BodyBean body;

    public AttributesBeanX getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesBeanX attributes) {
        this.attributes = attributes;
    }

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }
}
