package com.acmdreamteam.memorium.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String name,age,gender,married,sibling;


    public User(String name, String age, String gender, String married) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.married = married;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }


}
