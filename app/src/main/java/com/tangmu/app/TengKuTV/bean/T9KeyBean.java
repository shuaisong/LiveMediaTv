package com.tangmu.app.TengKuTV.bean;

public class T9KeyBean {
    private int num1;


    private int num2 = 0;
    private String letter1 = "";

    private String letter2 = "";
    private String letter3 = "";
    private String letter4 = "";

    public T9KeyBean(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public T9KeyBean(int num1, String letter1, String letter2, String letter3) {
        this.num1 = num1;
        this.letter1 = letter1;
        this.letter2 = letter2;
        this.letter3 = letter3;
    }

    public T9KeyBean(int num1, String letter1, String letter2, String letter3, String letter4) {
        this.num1 = num1;
        this.letter1 = letter1;
        this.letter2 = letter2;
        this.letter3 = letter3;
        this.letter4 = letter4;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public String getLetter4() {
        return letter4;
    }

    public void setLetter4(String letter4) {
        this.letter4 = letter4;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public String getLetter1() {
        return letter1;
    }

    public void setLetter1(String letter1) {
        this.letter1 = letter1;
    }

    public String getLetter2() {
        return letter2;
    }

    public void setLetter2(String letter2) {
        this.letter2 = letter2;
    }

    public String getLetter3() {
        return letter3;
    }

    public void setLetter3(String letter3) {
        this.letter3 = letter3;
    }

}
