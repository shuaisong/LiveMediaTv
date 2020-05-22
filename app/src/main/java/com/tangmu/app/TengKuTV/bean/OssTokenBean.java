package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

public class OssTokenBean implements Serializable {

    /**
     * expiredTime : 1584962998
     * expiration : 2020-03-23T11:29:58Z
     * credentials : {"sessionToken":"WdfsJyQkLAgrifm6QlOn3RIBwyllKsV1faa6a4f7310a3e43f2e5dc46417e2f07Icxp5DNxNEFuhM4LwU5SzfjtBAX4wMpwNWtVt_oIfAvOg8TdXU3FnttXSjF_UKqvv7nevMuHlzZsb8cATmU6k7SsSAkmd2oS3X-p-ZvC-mQo2XEq2NrOxRjtxBkMrTVCmFTJ54071cpHBnJumVXZGfZj5cymdJLxa0xqF9cDDxuSnTvf47WLwEhI3CZ7KXW1dXMKrausTpakri6su4rgnKD9PVtTWGR3FhKgF97Rlc5mrB4a6zQu_d4ULPekF62dBpTfa7siZ-S3Z2I9X0eqWchWIGVBx2ZUJ9NFKgX75tm23fvgSRLchvUQjxSqjWk9B-2JfHGh3e8IcTi0Rzh1hFGRymx-XB783QsPZ4pao8YYK8gRMRdHXAQ-TrOQmNnL3yvpLxmrQD0dSBa8u4etL7x_FWYm1jW4jjMlm-7w19RgBVo7dltpX6sivyJy9rITJkt65Jd1jPt9ASWVz-aKLQeFxOL6FAzs12lvDOlphwU","tmpSecretId":"AKIDq3H7vs5um3W9louErK0SxDSWz2N__jakNvZLpnNY7My5tb1lqfQdu0RrEBzfsVMe","tmpSecretKey":"mP/qlCvTB1ZuTCmUq1TnRxc0cGZODhVyJE6lmC6grFA="}
     * requestId : cc8f6f68-1e82-4ea8-b5ec-3bd719ca9462
     * startTime : 1584961198
     */

    private int expiredTime;
    private String expiration;
    private CredentialsBean credentials;
    private String requestId;
    private int startTime;

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public CredentialsBean getCredentials() {
        return credentials;
    }

    public void setCredentials(CredentialsBean credentials) {
        this.credentials = credentials;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public static class CredentialsBean {
        /**
         * sessionToken : WdfsJyQkLAgrifm6QlOn3RIBwyllKsV1faa6a4f7310a3e43f2e5dc46417e2f07Icxp5DNxNEFuhM4LwU5SzfjtBAX4wMpwNWtVt_oIfAvOg8TdXU3FnttXSjF_UKqvv7nevMuHlzZsb8cATmU6k7SsSAkmd2oS3X-p-ZvC-mQo2XEq2NrOxRjtxBkMrTVCmFTJ54071cpHBnJumVXZGfZj5cymdJLxa0xqF9cDDxuSnTvf47WLwEhI3CZ7KXW1dXMKrausTpakri6su4rgnKD9PVtTWGR3FhKgF97Rlc5mrB4a6zQu_d4ULPekF62dBpTfa7siZ-S3Z2I9X0eqWchWIGVBx2ZUJ9NFKgX75tm23fvgSRLchvUQjxSqjWk9B-2JfHGh3e8IcTi0Rzh1hFGRymx-XB783QsPZ4pao8YYK8gRMRdHXAQ-TrOQmNnL3yvpLxmrQD0dSBa8u4etL7x_FWYm1jW4jjMlm-7w19RgBVo7dltpX6sivyJy9rITJkt65Jd1jPt9ASWVz-aKLQeFxOL6FAzs12lvDOlphwU
         * tmpSecretId : AKIDq3H7vs5um3W9louErK0SxDSWz2N__jakNvZLpnNY7My5tb1lqfQdu0RrEBzfsVMe
         * tmpSecretKey : mP/qlCvTB1ZuTCmUq1TnRxc0cGZODhVyJE6lmC6grFA=
         */

        private String sessionToken;
        private String tmpSecretId;
        private String tmpSecretKey;

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public String getTmpSecretId() {
            return tmpSecretId;
        }

        public void setTmpSecretId(String tmpSecretId) {
            this.tmpSecretId = tmpSecretId;
        }

        public String getTmpSecretKey() {
            return tmpSecretKey;
        }

        public void setTmpSecretKey(String tmpSecretKey) {
            this.tmpSecretKey = tmpSecretKey;
        }
    }
}
