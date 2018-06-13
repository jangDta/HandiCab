package com.hello.handicab1;

public class UserInformation {

    private String userName;
    private String userPhone;
    private String userParentPhone;
    private String userHandicap;
    private String userNeed;
    private String userFavorite;

    public UserInformation(){

    }

    public UserInformation(String userName, String userPhone, String userParentPhone, String userHandicap, String userNeed, String userFavorite) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userParentPhone = userParentPhone;
        this.userHandicap = userHandicap;
        this.userNeed = userNeed;
        this.userFavorite = userFavorite;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserParentPhone() {
        return userParentPhone;
    }

    public void setUserParentPhone(String userParentPhone) {
        this.userParentPhone = userParentPhone;
    }

    public String getUserHandicap() {
        return userHandicap;
    }

    public void setUserHandicap(String userHandicap) {
        this.userHandicap = userHandicap;
    }

    public String getUserNeed() {
        return userNeed;
    }

    public void setUserNeed(String userNeed) {
        this.userNeed = userNeed;
    }

    public String getUserFavorite() {
        return userFavorite;
    }

    public void setUserFavorite(String userFavorite) {
        this.userFavorite = userFavorite;
    }
}