package com.example.pet.entity;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;

public class Pet {
    Bitmap image;
    String name,story;
    String id;
    int age,sex; //男生0，女生1
    int vaccine,sterillization,expelling;  //0代表未做，1代表做了
    //  vaccine疫苗，sterillization绝育，expelling驱虫

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public int getVaccine() {
        return vaccine;
    }

    public void setVaccine(int vaccine) {
        this.vaccine = vaccine;
    }

    public int getSterillization() {
        return sterillization;
    }

    public void setSterillization(int sterillization) {
        this.sterillization = sterillization;
    }

    public int getExpelling() {
        return expelling;
    }

    public void setExpelling(int expelling) {
        this.expelling = expelling;
    }
}
