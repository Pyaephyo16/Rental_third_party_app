package com.example.rentalforu.Models;

public class UserModel {

    //int id;
    String name;
    String email;
    String password;
    String profile;

    public UserModel(){}
    public UserModel( String name, String email, String password, String profile) {
       //this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    //public int getId() {
    //    return id;
   //}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfile() {
        return profile;
    }
}
