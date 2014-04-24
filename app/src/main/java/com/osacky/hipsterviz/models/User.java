package com.osacky.hipsterviz.models;

public class User {
    String name;
    String realName;
    String country;
    String gender;
    String age;
    String playCount;

    public int getPlayCount() {
        return Integer.parseInt(playCount);
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

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return Integer.parseInt(age);
    }
}
