package com.acmdreamteam.memorium.Model;

public class Journal {

    String things,date,type,priority;

    public Journal(String things, String date, String type, String priority) {
        this.things = things;
        this.date = date;
        this.type = type;
        this.priority = priority;
    }

    public Journal() {
    }

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
