package com.example.pet.entity;

import android.media.Image;

public class PetDetail {
    Image image;
    String name,story;
    int id;
    int age,sex; //男生0，女生1
    int vaccine,sterillization,expelling;  //0代表未做，1代表做了
    int flagCollection,flagCash;//0为否，1为是
    String condition;
    String cash,cashTime,cashReturn;

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getStory() {
        return story;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getSex() {
        return sex;
    }

    public int getVaccine() {
        return vaccine;
    }

    public int getSterillization() {
        return sterillization;
    }

    public int getExpelling() {
        return expelling;
    }

    public int getFlagCollection() {
        return flagCollection;
    }

    public void setFlagCollection(int flagCollection) {
        this.flagCollection = flagCollection;
    }

    public int getFlagCash() {
        return flagCash;
    }

    public String getCondition() {
        return condition;
    }

    public String getCash() {
        return cash;
    }

    public String getCashTime() {
        return cashTime;
    }

    public String getCashReturn() {
        return cashReturn;
    }
}
