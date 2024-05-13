package com.svdprog.testmatica.contests;

import java.io.Serializable;

// A class for storing data about a regular thematic test...

public class DecisionTest implements Serializable {
    private static final long serialVersionUID = 1L;
    public int quanity = 0;
    public int seen = 0;
    public int current_answer = 1;
    public int levelxp = 0;
    public boolean admin_user = false, enter = false;
    String title_test = "-";
    String[] answer = new String[50];
    int[] id = new int[50];
    int[] fine = new int[50];
    String[] link = new String[50];
    String[] user = new String[50];
    // These blocks are intended for the case of using this class for an active test:
    public int percent, status, mark, delete_test, for_five, for_four, for_three, id_test, personal_id;
    boolean active_test = false;
    public String date_test = "";
    // The main constructor of the class:
    public DecisionTest(int n, String title_test){
        this.quanity = n;
        this.title_test = title_test;
    }
    // These blocks are intended for the case of using this class for an active test:
    public DecisionTest(int n, String title_test, boolean active_test){
        this.quanity = n;
        this.title_test = title_test;
        this.active_test = active_test;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public void setEnter(boolean enter) {
        this.enter = enter;
    }
    public boolean getEnter() {
        return enter;
    }
    public void setPersonal_id(int personal_id) {
        this.personal_id = personal_id;
    }
    public int getPersonal_id() {
        return personal_id;
    }
    public int getId_test() {
        return id_test;
    }
    public void setId_test(int id_test) {
        this.id_test = id_test;
    }
    public String getDate_test() {
        return date_test;
    }
    public void setDate_test(String date_test) {
        this.date_test = date_test;
    }
    public boolean isActive_test() {
        return active_test;
    }
    public void setActive_test(boolean active_test) {
        this.active_test = active_test;
    }
    public boolean isAdmin_user() {
        return admin_user;
    }
    public void setAdmin_user(boolean admin_user) {
        this.admin_user = admin_user;
    }
    public int getPercent() {
        return percent;
    }
    public void setPercent(int percent) {
        this.percent = percent;
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
    // Blocks for the usual test:
    public int getSeen() {
        return seen;
    }
    public int getLevelxp(){
        return levelxp;
    }
    public void setLevelxp(int levelxp) {
        this.levelxp = levelxp;
    }
    public void setSeen(int seen) {
        this.seen = seen;
    }
    public int getQuanity(){
        return quanity;
    }
    public String getTitle(){
        return title_test;
    }
    public void setId(int n, int id){
        this.id[n - 1] = id;
    }
    public void setFine(int n, int fine){
        this.fine[n - 1] = fine;
    }
    public int getId(int n){
        return id[n - 1];
    }
    public int getFine(int n){
        return fine[n - 1];
    }
    public void setAnswer(int n, String answer){
        this.answer[n - 1] = answer;
    }
    public void setLink(int n, String link){
        this.link[n - 1] = link;
    }
    public void setTitle(String title_test) { this.title_test = title_test; }
    // TYPES OF LINK:
    // 1 - <img alt=\"\" src=\"https://sun9-27.userapi.com/impf/0ZcBV5TrzLd0itiE3pAKb7KCPVKAdH90tYvdvQ/2UefMEl0K0w.jpg?size=290x123&amp;quality=96&amp;sign=5fd07caaddd743cc029a3bb9bf771a2a&amp;type=album\" />
    // 2 - <img alt=\"\" src=\"/upload/image/Test12/Var2/9.JPG\" style=\"height:225px; width:505px\" />
    // 3 - <img alt=\"\" src=\"http://testmatica.ru/upload/image/oge2020/Test36/15.JPG\" />
    public void setUser(int n, String user){
        this.user[n - 1] = user;
    }
    public String getAnswer(int n){
        return answer[n - 1];
    }
    public String getLink(int n){
        return link[n - 1];
    }
    public String getUser(int n){
        return user[n - 1];
    }
    public void setCurrent(int n){
        this.current_answer = n;
    }
    public int getCurrent(){
        return current_answer;
    }
}
