package com.tangmu.app.TengKuTV.bean;

public class LiveDetailBean {

    /**
     * l_program : [{"dates1":"09:22","dates2":"02:32","content":"你咋那么美"},{"dates1":"14:22","dates2":"15:33","content":"就是那么的美美美"}]
     * l_des : 欢迎您的到来
     * l_des_z : 欢迎您的到来
     * l_status : 1
     * l_program_type : 2
     * is_collec_status : 0
     */

    private String l_des;
    private String l_des_z;
    private int l_status;
    private int l_program_type;
    private int is_collec_status;
    /**
     * l_program : images/2020/3/de6b6b9e620c11bf1d332454ca706185.jpg
     * l_img : images/2020/3/fbd2f64635178cd291c341e09c7029a9.jpg
     * l_title : 测试
     * l_title_z : 测试
     */

    private String l_img;
    private String l_title;
    private String l_title_z;
    /**
     * l_program : images/2020/3/de6b6b9e620c11bf1d332454ca706185.jpg
     * uc_id : 0
     */

    private int uc_id;

    public Object getL_program() {
        return l_program;
    }

    public void setL_program(Object l_program) {
        this.l_program = l_program;
    }

    private Object l_program;

    public String getL_des() {
        return l_des;
    }

    public void setL_des(String l_des) {
        this.l_des = l_des;
    }

    public String getL_des_z() {
        return l_des_z;
    }

    public void setL_des_z(String l_des_z) {
        this.l_des_z = l_des_z;
    }

    public int getL_status() {
        return l_status;
    }

    public void setL_status(int l_status) {
        this.l_status = l_status;
    }

    public int getL_program_type() {
        return l_program_type;
    }

    public void setL_program_type(int l_program_type) {
        this.l_program_type = l_program_type;
    }

    public int getIs_collec_status() {
        return is_collec_status;
    }

    public void setIs_collec_status(int is_collec_status) {
        this.is_collec_status = is_collec_status;
    }


    public String getL_img() {
        return l_img;
    }

    public void setL_img(String l_img) {
        this.l_img = l_img;
    }

    public String getL_title() {
        return l_title;
    }

    public void setL_title(String l_title) {
        this.l_title = l_title;
    }

    public String getL_title_z() {
        return l_title_z;
    }

    public void setL_title_z(String l_title_z) {
        this.l_title_z = l_title_z;
    }

    public int getUc_id() {
        return uc_id;
    }

    public void setUc_id(int uc_id) {
        this.uc_id = uc_id;
    }
}
