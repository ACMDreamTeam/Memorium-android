package com.acmdreamteam.memorium.Model;

public class People {


    String name,phone,notes,imageURL;


    public People(String name, String phone, String notes, String imageURL) {
        this.name = name;
        this.phone = phone;
        this.notes = notes;
        this.imageURL = imageURL;
    }

    public People() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
