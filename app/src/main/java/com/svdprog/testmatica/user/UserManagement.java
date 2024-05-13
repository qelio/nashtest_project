package com.svdprog.testmatica.user;

// This class is designed to display users in the admin control panel...

public class UserManagement {
    public int id, adminuser, dolg, imghref, levelxp, idclass, diary;
    public String login, pass, name, family, pol, email, city, date, ip, browser, checkclass;
    // The standard constructor of the class, it is not necessary to fill it in:
    public UserManagement(int id, String login, String pass, String name, String family, String pol, String email, String city, int adminuser, String date, String ip, String browser, int dolg, int imghref, int levelxp, String checkclass, int idclass, int diary){
        this.id = id;
        this.login = login;
        this.pass = pass;
        this.name = name;
        this.family = family;
        this.pol = pol;
        this.email = email;
        this.city = city;
        this.adminuser = adminuser;
        this.date = date;
        this.ip = ip;
        this.browser = browser;
        this.dolg = dolg;
        this.imghref = imghref;
        this.levelxp = levelxp;
        this.checkclass = checkclass;
        this.idclass = idclass;
        this.diary = diary;
    }
    // Setters and getters for this class, you can fill in the class and output information using them:
    public String getImg_link(){
        if (imghref == 1){
            return "https://testmatica.ru/images/monkey.png";
        }
        if (imghref == 2){
            return "https://testmatica.ru/images/cat.png";
        }
        if (imghref == 3){
            return "https://testmatica.ru/images/cherep.png";
        }
        if (imghref == 4){
            return "https://testmatica.ru/images/corova.png";
        }
        if (imghref == 5){
            return "https://testmatica.ru/images/dolphin.png";
        }
        return "https://testmatica.ru/images/girafee.png";
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAdminuser(int adminuser) {
        this.adminuser = adminuser;
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

    public void setDolg(int dolg) {
        this.dolg = dolg;
    }

    public void setImghref(int imghref) {
        this.imghref = imghref;
    }

    public void setLevelxp(int levelxp) {
        this.levelxp = levelxp;
    }

    public void setCheckclass(String checkclass) {
        this.checkclass = checkclass;
    }

    public void setIdclass(int idclass) {
        this.idclass = idclass;
    }

    public void setDiary(int diary) {
        this.diary = diary;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public String getPol() {
        return pol;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public int getAdminuser() {
        return adminuser;
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

    public int getDolg() {
        return dolg;
    }

    public int getImghref() {
        return imghref;
    }

    public int getLevelxp() {
        return levelxp;
    }

    public String getCheckclass() {
        return checkclass;
    }

    public int getIdclass() {
        return idclass;
    }

    public int getDiary() {
        return diary;
    }
}
