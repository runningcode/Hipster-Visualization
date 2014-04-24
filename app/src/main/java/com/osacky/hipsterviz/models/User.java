package com.osacky.hipsterviz.models;

public class User {
    String name;
    String realName;
    String country;
    char gender;

    public int getPlayCount() {
        return playCount;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public String getCountry() {
        return country;
    }

    public char getGender() {
        return gender;
    }

    int playCount;
}
