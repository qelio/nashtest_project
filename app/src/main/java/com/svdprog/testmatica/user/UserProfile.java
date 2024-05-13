package com.svdprog.testmatica.user;

// This class is intended for storing user information...

public class UserProfile {
    public int id = 0;
    public String login = "";
    // The password is missing for the security of user data:
    public String name = "";
    public String family = "";
    public String pol = "";
    public String email = "";
    public String city = "";
    // These are user rights, indicated according to the marking of the site database:
    public int adminuser = 0;
    // This field contains the date of the last login to the account:
    public String date = "";
    // This field contains the IP address of the device from which the account was last logged in:
    public String ip = "";
    // This field contains the configuration of the browser and the device from which the account was last logged in:
    public String browser = "";
    // The functionality of the following fields is temporarily not implemented...
    public int dolg = 0;
    public int imghref = 0;
    public int levelxp = 0;
    public String checkclass = "";
    public int idclass = 0;
    public int diary = 0;
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