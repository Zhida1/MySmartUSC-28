package com.example.zhidachen.mysmartusc_28;

public class Keyword {
    private String keyword;
    private String checkArea;

    public Keyword() {

    }

    public Keyword(String keyword, String checkArea){
        this.keyword = keyword;
        this.checkArea = checkArea;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getCheckArea() {
        return checkArea;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setCheckArea(String checkArea) {
        this.checkArea = checkArea;
    }
}
