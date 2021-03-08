package com.example.pet.entity;

import android.media.Image;

import java.util.ArrayList;

public class Pet {
    ArrayList<Image> images;
    String name,story;
    int age,sex; //男生0，女生1
    int vaccine,sterillization,expelling;  //0代表未做，1代表做了
    //  vaccine疫苗，sterillization绝育，expelling驱虫

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public Image getImage(int index) {
        return images.get(index);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
