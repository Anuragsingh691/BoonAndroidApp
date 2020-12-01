package com.example.boon_android_app.Users;

public class Users {

    private String name,phone, image,email,rating;
    private Boolean hindi, english;
    private float Userclass;
    public Users()
    {

    }
    public Users(String name, String phone, String image, String email,Boolean hindi, Boolean english, float userclass,String rating)
    {
        name=this.name;
        phone=this.phone;
        image=this.image;
        email=this.email;
        hindi=this.hindi;
        english=this.english;
        userclass=this.Userclass;
        this.rating=rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHindi() {
        return hindi;
    }

    public void setHindi(Boolean hindi) {
        this.hindi = hindi;
    }

    public Boolean getEnglish() {
        return english;
    }

    public void setEnglish(Boolean english) {
        this.english = english;
    }

    public float getUserclass() {
        return Userclass;
    }

    public void setUserclass(float userclass) {
        Userclass = userclass;
    }
}
