package com.svdprog.testmatica.contests;

// This class is designed to store the data of the passed test obtained from the site database...

public class ResultContest {
    public int id = 0;
    public int user_id = 0;
    public String title = "";
    public String date = "";
    public String kerm = "";
    // The standard constructor of the class, it is not necessary to fill it in:
    public ResultContest(int id, int user_id, String title, String date, String kerm){
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.date = date;
        this.kerm = kerm;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public int getId(){
        return id;
    }
    public int getUser_id(){
        return user_id;
    }
    public String getTitle(){
        return title;
    }
    public String getDate(){
        return date;
    }
    public String getKerm(){
        return kerm;
    }
}
