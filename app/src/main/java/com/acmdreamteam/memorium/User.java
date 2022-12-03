package com.acmdreamteam.memorium;

public class User {

    private String name,age,gender,married,sibling;


    public User(String name, String age, String gender, String married, String sibling) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.married = married;
        this.sibling = sibling;
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

    public String getSibling() {
        return sibling;
    }

    public void setSibling(String sibling) {
        this.sibling = sibling;
    }
}
