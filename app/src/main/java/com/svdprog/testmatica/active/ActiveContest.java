package com.svdprog.testmatica.active;

// This class is intended for the active test start block from the personal account:

public class ActiveContest {
    public int id, personal_id;
    public String title, comment, date;
    public int ed;
    public int numberclass;
    public int typetest;
    public int percent, status, mark, delete_test, for_five, for_four, for_three;
    // The standard constructor of the class, it is not necessary to fill it in:
    public ActiveContest(int personal_id, int id, String title, String comment, String date, int ed, int percent, int status, int mark, int delete_test, int for_five, int for_four, int for_three, int numberclass, int typetest){
        this.personal_id = personal_id;
        this.id = id;
        this.title = title;
        this.ed = ed;
        this.numberclass = numberclass;
        this.typetest = typetest;
        this.comment = comment;
        this.percent = percent;
        this.status = status;
        this.mark = mark;
        this.delete_test = delete_test;
        this.for_five = for_five;
        this.for_four = for_four;
        this.for_three = for_three;
        this.date = date;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public void setPersonal_id(int personal_id) {
        this.personal_id = personal_id;
    }
    public int getPersonal_id() {
        return personal_id;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setPercent(int percent) {
        this.percent = percent;
    }
    public int getPercent() {
        return percent;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getMark() {
        return mark;
    }
    public void setMark(int mark) {
        this.mark = mark;
    }
    public int getDelete_test() {
        return delete_test;
    }
    public void setDelete_test(int delete_test) {
        this.delete_test = delete_test;
    }
    public int getFor_five() {
        return for_five;
    }
    public void setFor_five(int for_five) {
        this.for_five = for_five;
    }
    public int getFor_four() {
        return for_four;
    }
    public void setFor_four(int for_four) {
        this.for_four = for_four;
    }
    public int getFor_three() {
        return for_three;
    }
    public void setFor_three(int for_three) {
        this.for_three = for_three;
    }
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public int getEd(){
        return ed;
    }
    public int getNumberclass(){
        return numberclass;
    }
    public int getTypetest(){
        return typetest;
    }

}
