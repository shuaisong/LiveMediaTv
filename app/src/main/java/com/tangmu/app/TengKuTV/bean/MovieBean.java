package com.tangmu.app.TengKuTV.bean;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class MovieBean {

    /**
     * vm_id : 13
     * vt_id_one : 1
     * vt_id_two : 4
     * vm_title : 甄嬛传2
     * vm_title_z : 甄嬛传藏文2
     * vm_des : 雍正元年，结束了血腥的夺位之争，新的君主（陈建斌 饰）继位，国泰民安，政治清明，但在一片祥和的表象之下，一股暗流蠢蠢欲动，尤其后宫，华妃（蒋欣 饰）与皇后（蔡少芬 饰）分庭抗礼，各方势力裹挟其中，凶险异常。十七岁的甄嬛（孙俪饰）与好姐妹眉庄（斓曦饰）、陵容（陶昕然饰）参加选秀，她本抱着来充个数的念头，可皇帝（陈建斌饰）偏相中了她的智慧、气节与端庄，最后三人一同入选。但因华妃（蒋欣饰）嚣张，步步紧逼，眉庄被冤，陵容变心，天真的甄嬛慢慢变成了后宫精明的女子。皇帝发现年羹尧（孙宁饰）的野心，令甄父剪除年氏一族，甄嬛终于斗倒了华妃。但由于甄嬛与先故纯元皇后的神似，皇后设计以纯元皇后的礼服陷害甄嬛，父亲（沈保平饰）也被文字狱牵连和奸人陷害而遭牢狱之灾，生下女儿后，心灰意冷的甄嬛选择出宫修行。在宫外幸得十七爷允礼（李东学饰）悉心照顾，二人相亲相爱，只等有机会远走高飞。后因误传十七爷死讯，甄嬛为保全腹中骨肉，设计与皇帝相遇，狠心断绝对十七爷的爱恋，重回宫中，再度与皇后相斗。后因生下双生子，同时甄父的冤案得以平反，重新被皇帝重用，甄氏一族再度崛起。甄嬛多次躲过皇后的陷害，最终扳倒皇后。可造化弄人，由于皇帝的疑心，最终却只能看着心上人允礼死在自己怀中，而与叶澜依（热依扎饰）合谋弑君。皇帝驾崩后，甄嬛养子弘历登基，甄嬛被尊为圣母皇太后，即便享尽荣华，但眼见一生姐妹沈眉庄血崩而亡，一生爱人允礼为保其周全而无憾自尽，不过是一代封建王朝的悲情故梦罢了
     * vm_des_z : 雍正元年，结束了血腥的夺位之争，新的君主（陈建斌 饰）继位，国泰民安，政治清明，但在一片祥和的表象之下，一股暗流蠢蠢欲动，尤其后宫，华妃（蒋欣 饰）与皇后（蔡少芬 饰）分庭抗礼，各方势力裹挟其中，凶险异常。十七岁的甄嬛（孙俪饰）与好姐妹眉庄（斓曦饰）、陵容（陶昕然饰）参加选秀，她本抱着来充个数的念头，可皇帝（陈建斌饰）偏相中了她的智慧、气节与端庄，最后三人一同入选。但因华妃（蒋欣饰）嚣张，步步紧逼，眉庄被冤，陵容变心，天真的甄嬛慢慢变成了后宫精明的女子。皇帝发现年羹尧（孙宁饰）的野心，令甄父剪除年氏一族，甄嬛终于斗倒了华妃。但由于甄嬛与先故纯元皇后的神似，皇后设计以纯元皇后的礼服陷害甄嬛，父亲（沈保平饰）也被文字狱牵连和奸人陷害而遭牢狱之灾，生下女儿后，心灰意冷的甄嬛选择出宫修行。在宫外幸得十七爷允礼（李东学饰）悉心照顾，二人相亲相爱，只等有机会远走高飞。后因误传十七爷死讯，甄嬛为保全腹中骨肉，设计与皇帝相遇，狠心断绝对十七爷的爱恋，重回宫中，再度与皇后相斗。后因生下双生子，同时甄父的冤案得以平反，重新被皇帝重用，甄氏一族再度崛起。甄嬛多次躲过皇后的陷害，最终扳倒皇后。可造化弄人，由于皇帝的疑心，最终却只能看着心上人允礼死在自己怀中，而与叶澜依（热依扎饰）合谋弑君。皇帝驾崩后，甄嬛养子弘历登基，甄嬛被尊为圣母皇太后，即便享尽荣华，但眼见一生姐妹沈眉庄血崩而亡，一生爱人允礼为保其周全而无憾自尽，不过是一代封建王朝的悲情故梦罢了
     * vm_img : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1061196248,3011418675&fm=11&gp=0.jpg
     * vm_update_status : 1
     */

    private int vm_id;
    private int vt_id_one;
    private int vt_id_two;
    private String vm_title;
    private String vm_title_z;
    private String vm_des;
    private String vm_des_z;
    private String vm_img;
    private int vm_update_status;//1:更新中2：已完成

    public int getVm_id() {
        return vm_id;
    }

    public void setVm_id(int vm_id) {
        this.vm_id = vm_id;
    }

    public int getVt_id_one() {
        return vt_id_one;
    }

    public void setVt_id_one(int vt_id_one) {
        this.vt_id_one = vt_id_one;
    }

    public int getVt_id_two() {
        return vt_id_two;
    }

    public void setVt_id_two(int vt_id_two) {
        this.vt_id_two = vt_id_two;
    }

    public String getVm_title() {
        return vm_title;
    }

    public void setVm_title(String vm_title) {
        this.vm_title = vm_title;
    }

    public String getVm_title_z() {
        return vm_title_z;
    }

    public void setVm_title_z(String vm_title_z) {
        this.vm_title_z = vm_title_z;
    }

    public String getVm_des() {
        return vm_des;
    }

    public void setVm_des(String vm_des) {
        this.vm_des = vm_des;
    }

    public String getVm_des_z() {
        return vm_des_z;
    }

    public void setVm_des_z(String vm_des_z) {
        this.vm_des_z = vm_des_z;
    }

    public String getVm_img() {
        return vm_img;
    }

    public void setVm_img(String vm_img) {
        this.vm_img = vm_img;
    }

    public int getVm_update_status() {
        return vm_update_status;
    }

    public void setVm_update_status(int vm_update_status) {
        this.vm_update_status = vm_update_status;
    }
}
