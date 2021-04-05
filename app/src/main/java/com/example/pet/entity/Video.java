package com.example.pet.entity;

public class Video {
    String id;
    String name;
    int age;
    String url,video;

    public  Video(){};

    public Video(String id, String name, int age, String url, String video) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.url = url;
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
