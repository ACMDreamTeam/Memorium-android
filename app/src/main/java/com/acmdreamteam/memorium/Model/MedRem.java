package com.acmdreamteam.memorium.Model;

public class MedRem {


    String name,med_id,med_type,time,food,frequency,number,weekday;


    public MedRem(String name, String med_id, String med_type, String time, String food, String frequency, String number, String weekday) {
        this.name = name;
        this.med_id = med_id;
        this.med_type = med_type;
        this.time = time;
        this.food = food;
        this.frequency = frequency;
        this.number = number;
        this.weekday = weekday;
    }

    public MedRem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMed_id() {
        return med_id;
    }

    public void setMed_id(String med_id) {
        this.med_id = med_id;
    }

    public String getMed_type() {
        return med_type;
    }

    public void setMed_type(String med_type) {
        this.med_type = med_type;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
