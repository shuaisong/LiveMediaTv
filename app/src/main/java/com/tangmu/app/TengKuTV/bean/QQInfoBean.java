package com.tangmu.app.TengKuTV.bean;

public class QQInfoBean {
    private String nickName;
    private String headImg;
    private String openId;

    public QQInfoBean(String nickName, String headImg, String openId) {
        this.nickName = nickName;
        this.headImg = headImg;
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public String getOpenId() {
        return openId;
    }
}
