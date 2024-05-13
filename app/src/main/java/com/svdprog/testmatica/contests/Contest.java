package com.svdprog.testmatica.contests;

// This class is designed to store information about the current contest...

public class Contest {
    public int id;
    public String title;
    public String var1;
    public String var2;
    public int ed;
    public int levelxp;
    public int numberclass;
    public int typetest;
    // The standard constructor of the class, it is not necessary to fill it in:
    public Contest(int id, String title, String var1, String var2, int ed, int levelxp, int numberclass, int typetest){
        this.id = id;
        this.title = title;
        this.var1 = var1;
        this.var2 = var2;
        this.ed = ed;
        this.levelxp = levelxp;
        this.numberclass = numberclass;
        this.typetest = typetest;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getVar1(){
        return var1;
    }
    public String getVar2(){
        return var2;
    }
    public int getEd(){
        return ed;
    }
    public int getLevelxp(){
        return levelxp;
    }
    public int Numberclass(){
        return numberclass;
    }
    public int getTypetest(){
        return typetest;
    }
    public int getStart1(){
        char[] temp = this.var1.toCharArray();
        int start = 0, finish = 0;
        // Conversion of the link type to a typed view, readable by the handler on the server:
        for(int i = 0; i < temp.length; i++){
            if (temp[i] == 'r' && temp[i + 1] == 't'){
                start = i + 3;
            }
            if (temp[i] == '&'){
                finish = i - 1;
            }
        }
        String result = "";
        for (int i = start; i <= finish; i++){
            result += temp[i];
        }
        return Integer.parseInt(result);
    }
    public int getFinish1(){
        char[] temp = this.var1.toCharArray();
        int start = 0, finish = 0;
        // Conversion of the link type to a typed view, readable by the handler on the server:
        for(int i = 0; i < temp.length; i++){
            if (temp[i] == 's' && temp[i + 1] == 'h'){
                start = i + 3;
            }
            if (temp[i] == '\'' && temp[i + 1] == '>'){
                finish = i - 1;
            }
        }
        String result = "";
        for (int i = start; i <= finish; i++){
            result += temp[i];
        }
        return Integer.parseInt(result);
    }
    public int getStart2(){
        char[] temp = this.var2.toCharArray();
        int start = 0, finish = 0;
        // Conversion of the link type to a typed view, readable by the handler on the server:
        for(int i = 0; i < temp.length; i++){
            if (temp[i] == 'r' && temp[i + 1] == 't'){
                start = i + 3;
            }
            if (temp[i] == '&'){
                finish = i - 1;
            }
        }
        String result = "";
        for (int i = start; i <= finish; i++){
            result += temp[i];
        }
        return Integer.parseInt(result);
    }
    public int getFinish2(){
        char[] temp = this.var2.toCharArray();
        int start = 0, finish = 0;
        // Conversion of the link type to a typed view, readable by the handler on the server:
        for(int i = 0; i < temp.length; i++){
            if (temp[i] == 's' && temp[i + 1] == 'h'){
                start = i + 3;
            }
            if (temp[i] == '\'' && temp[i + 1] == '>'){
                finish = i - 1;
            }
        }
        String result = "";
        for (int i = start; i <= finish; i++){
            result += temp[i];
        }
        return Integer.parseInt(result);
    }
}
