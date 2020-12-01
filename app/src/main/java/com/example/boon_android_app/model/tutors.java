package com.example.boon_android_app.model;

public class tutors {
    private String Name,rating,class1,Tid,image,sub,pid;
    private tutors()
    {

    }
    private tutors(String Name,String rating,String class1,String sub,String price,String Tid,String image,String pid)
    {
        this.Name=Name;
        this.class1=class1;
        this.rating=rating;
        this.sub=sub;
        this.rating=rating;
        this.Tid=Tid;
        this.image=image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getTid() {
        return Tid;
    }

    public void setTid(String tid) {
        Tid = tid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}

