package com.project.greekpoll.greekpoll.dto;

public class UserRegistrationDto {

    private String gender;
    private String fullname;
    private String prefecture;
    private String municipality;
    private String password;
    private String username;


    public UserRegistrationDto(){

    }



    public UserRegistrationDto(String gender, String fullname, String prefecture, String municipality, String password, String username) {
        this.gender = gender;
        this.fullname = fullname;
        this.prefecture = prefecture;
        this.municipality = municipality;
        this.password = password;
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
