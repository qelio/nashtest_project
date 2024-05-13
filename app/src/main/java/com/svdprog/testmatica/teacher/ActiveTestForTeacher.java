package com.svdprog.testmatica.teacher;

// This class is designed to edit the questions of the active test from the teacher's room...

public class ActiveTestForTeacher {
    public int active_gen, id, levelxp, class_test, type_test, fine, seen, add = 0;
    public String title, cond, any;
    // The standard constructor of the class, it is not necessary to fill it in:
    public ActiveTestForTeacher(int active_gen, int id, String title, String cond, String any, int levelxp, int class_test, int type_test, int fine, int seen){
        this.active_gen = active_gen;
        this.id = id;
        this.title = title;
        this.cond = cond;
        this.any = any;
        this.levelxp = levelxp;
        this.class_test = class_test;
        this.type_test = type_test;
        this.fine = fine;
        this.seen = seen;
    }
    // The no standard constructor of the class, it is not necessary to fill it in:
    public ActiveTestForTeacher(int active_gen, int id, String title, String cond, String any, int levelxp, int class_test, int type_test, int fine, int seen, int add){
        this.active_gen = active_gen;
        this.add = add;
        this.id = id;
        this.title = title;
        this.cond = cond;
        this.any = any;
        this.levelxp = levelxp;
        this.class_test = class_test;
        this.type_test = type_test;
        this.fine = fine;
        this.seen = seen;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public int getAdd() {
        return add;
    }
    public void setAdd(int add) {
        this.add = add;
    }
    public void setActive_gen(int active_gen) {
        this.active_gen = active_gen;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCond(String cond) {
        this.cond = cond;
    }
    public void setAny(String any) {
        this.any = any;
    }
    public void setLevelxp(int levelxp) {
        this.levelxp = levelxp;
    }
    public void setClass_test(int class_test) {
        this.class_test = class_test;
    }
    public void setType_test(int type_test) {
        this.type_test = type_test;
    }
    public void setFine(int fine) {
        this.fine = fine;
    }
    public void setSeen(int seen) {
        this.seen = seen;
    }
    public String getAny() {
        return any;
    }
    public int getLevelxp() {
        return levelxp;
    }
    public int getClass_test() {
        return class_test;
    }
    public int getType_test() {
        return type_test;
    }
    public int getFine() {
        return fine;
    }
    public int getSeen() {
        return seen;
    }
    public String getTitle() {
        return title;
    }
    public int getActive_gen() {
        return active_gen;
    }
    public int getId() {
        return id;
    }
    public String getCond() {
        // Converting a link from a database to a standard-looking link that the function understands correctly:
        String result = "";
        char[] link_char = cond.toCharArray();
        int link_type = 2;
        for (int j = 0; j < link_char.length; j++){
            if ((link_char[j] == 'a') && (link_char[j + 1] == 'p') && (link_char[j + 2] == 'i')){
                link_type = 1;
                break;
            }
            if ((link_char[j] == 't') && (link_char[j + 1] == 'e') && (link_char[j + 2] == 's') && (link_char[j + 3] == 't') && (link_char[j + 4] == 'm')){
                link_type = 3;
                break;
            }
        }
        if (link_type == 1){
            int start = 20;
            int finish = link_char.length - 5;
            for (int j = start; j <= finish; j++){
                result += link_char[j];
            }
        }
        if (link_type == 2){
            int start = 17;
            int finish = link_char.length - 5;
            result += "https://testmatica.ru";
            for (int j = start; j <= finish; j++){
                result += link_char[j];
            }
        }
        if (link_type == 3){
            int start = 37;
            int finish = link_char.length - 5;
            result += "https://testmatica.ru";
            for (int j = start; j <= finish; j++){
                result += link_char[j];
            }
        }
        return result;
    }
}
