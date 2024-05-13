package com.svdprog.testmatica.student;

import java.io.Serializable;

// A class designed to store data about a thematic area (subject)...

public class SchoolSubject implements Serializable {
    private static final long serialVersionUID = 1L;
    public int[] number_class = new int[20];
    public boolean[] avail_tests = new boolean[20];
    public boolean[] any_var = new boolean[20];
    public int type_tests;
    // The standard constructor of the class, it is not necessary to fill it in:
    public SchoolSubject(int type_tests){
        this.type_tests = type_tests;
    }
    public void settings_subject(int n, int number_class, boolean avail_tests, boolean any_var){
        this.number_class[n] = number_class;
        this.avail_tests[n] = avail_tests;
        this.any_var[n] = any_var;
    }
    // If there are no tests:
    public void settings_subject(int n, int number_class, boolean avail_tests){
        this.number_class[n] = number_class;
        this.avail_tests[n] = avail_tests;
    }
    // Getting information about the subject:
    public int getNumber_class(int n){
        return number_class[n];
    }
    public boolean getAvail_tests(int n){
        return avail_tests[n];
    }
    public boolean getAny_var(int n){
        return any_var[n];
    }
    public int getType_tests(){
        return type_tests;
    }
    // Getting the name of the school subject (currently only available are present, the rest of the subjects need to be supplemented later):
    public String getName_subject(){
        if (this.type_tests == 1){
            return "Математика";
        }
        if (this.type_tests == 2){
            return "Русский язык";
        }
        if (this.type_tests == 3){
            return "Информатика";
        }
        if (this.type_tests == 4){
            return "Биология";
        }
        if (this.type_tests == 5){
            return "Обществознание";
        }
        if (this.type_tests == 6){
            return "Окружающий мир";
        }
        return "Произошла ошибка";
    }
}
