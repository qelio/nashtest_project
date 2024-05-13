package com.svdprog.testmatica.admin;

// This class is designed to display the history of user authorization sessions...

public class PullManagement {
    public int id, id_user;
    public String date, ip, browser;
    // The standard constructor of the class, it is not necessary to fill it in:
    public PullManagement(int id, int id_user, String date, String ip, String browser){
        this.id = id;
        this.id_user = id_user;
        this.date = date;
        this.ip = ip;
        this.browser = browser;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public void setId(int id) {
        this.id = id;
    }
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public int getId() {
        return id;
    }
    public int getId_user() {
        return id_user;
    }
    public String getDate() {
        return date;
    }
    public String getIp() {
        return ip;
    }
    public String getBrowser() {
        return browser;
    }
}
