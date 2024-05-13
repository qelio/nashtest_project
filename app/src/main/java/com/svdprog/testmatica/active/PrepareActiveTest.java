package com.svdprog.testmatica.active;

// This class is intended for the active test from the teacher class...

public class PrepareActiveTest {
    public int id_test, id_user, for_five, for_four, for_three, delete_test, class_test, type_test;
    public String name_test, com_test, date_test;
    // The standard constructor of the class, it is not necessary to fill it in:
    public PrepareActiveTest(int id_test, String name_test, String com_test, int id_user, String date_test, int for_five, int for_four, int for_three, int delete_test, int class_test, int type_test){
        this.id_test = id_test;
        this.name_test = name_test;
        this.com_test = com_test;
        this.id_user = id_user;
        this.date_test = date_test;
        this.for_five = for_five;
        this.for_four = for_four;
        this.for_three = for_three;
        this.delete_test = delete_test;
        this.class_test = class_test;
        this.type_test = type_test;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public int getId_test() {
        return id_test;
    }
    public String getName_test() {
        return name_test;
    }
    public String getCom_test() {
        return com_test;
    }
    public int getId_user() {
        return id_user;
    }
    public String getDate_test() {
        return date_test;
    }
    public int getFor_three() {
        return for_three;
    }
    public int getFor_four() {
        return for_four;
    }
    public int getFor_five() {
        return for_five;
    }
    public int getDelete_test() {
        return delete_test;
    }
    public int getType_test() {
        return type_test;
    }
    public int getClass_test() {
        return class_test;
    }
    public void setId_test(int id_test) {
        this.id_test = id_test;
    }
    public void setName_test(String name_test) {
        this.name_test = name_test;
    }
    public void setCom_test(String com_test) {
        this.com_test = com_test;
    }
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    public void setDate_test(String date_test) {
        this.date_test = date_test;
    }
    public void setFor_three(int for_three) {
        this.for_three = for_three;
    }
    public void setFor_four(int for_four) {
        this.for_four = for_four;
    }
    public void setFor_five(int for_five) {
        this.for_five = for_five;
    }
    public void setDelete_test(int delete_test) {
        this.delete_test = delete_test;
    }
    public void setClass_test(int class_test) {
        this.class_test = class_test;
    }
    public void setType_test(int type_test) {
        this.type_test = type_test;
    }
}
