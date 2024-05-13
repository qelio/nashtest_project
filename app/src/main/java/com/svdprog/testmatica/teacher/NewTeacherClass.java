package com.svdprog.testmatica.teacher;

// This class is intended for the teacher to view his created classes...

public class NewTeacherClass {
    public int id;
    public String title;
    public int idteacher;
    public String NTcode;
    // The standard constructor of the class, it is not necessary to fill it in:
    public NewTeacherClass(int id, String title, int idteacher, String NTcode){
        this.id = id;
        this.title = title;
        this.idteacher = idteacher;
        this.NTcode = NTcode;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getIdteacher() {
        return idteacher;
    }
    public void setIdteacher(int idteacher) {
        this.idteacher = idteacher;
    }
    public String getNTcode() {
        return NTcode;
    }
    public void setNTcode(String NTcode) {
        this.NTcode = NTcode;
    }
}
