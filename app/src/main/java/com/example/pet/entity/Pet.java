package com.example.pet.entity;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Pet implements Serializable {
    String name,story;
    String id;
    int age,sex; //男生0，女生1
    int vaccine,sterillization,expelling;  //0代表未做，1代表做了
    //  vaccine疫苗，sterillization绝育，expelling驱虫
    String url1,url2,url3,url4;
    String video;
    String phone;
    String condition;
    String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getUrl4() {
        return url4;
    }

    public void setUrl4(String url4) {
        this.url4 = url4;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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
