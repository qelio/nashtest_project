package com.svdprog.testmatica.contests;

// This class is designed to store information from this function list_assigned_tests.php...

public class AssignedTest {
    public int id, id_test, id_teacher, id_user, percent, status, mark, delete_test;
    public String name, family, name_class;
    // The standard constructor of the class, it is not necessary to fill it in:
    public AssignedTest(int id, int id_test, int id_teacher, int id_user, String name, String family, String name_class, int percent, int status, int mark, int delete_test){
        this.id = id;
        this.id_test = id_test;
        this.id_teacher = id_teacher;
        this.id_user = id_user;
        this.name = name;
        this.family = family;
        this.name_class = name_class;
        this.percent = percent;
        this.status = status;
        this.mark = mark;
        this.delete_test = delete_test;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public int getId() {
        return id;
    }
    public int getId_test() {
        return id_test;
    }
    public int getId_user() {
        return id_user;
    }
    public int getId_teacher() {
        return id_teacher;
    }
    public String getName() {
        return name;
    }
    public String getFamily() {
        return family;
    }
    public String getName_class() {
        return name_class;
    }
    public int getPercent() {
        return percent;
    }
    public int getStatus() {
        return status;
    }
    public int getMark() {
        return mark;
    }
    public int getDelete_test() {
        return delete_test;
    }
}
