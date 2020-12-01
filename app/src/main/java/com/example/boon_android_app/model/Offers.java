package com.example.boon_android_app.model;

import android.widget.Button;

public class Offers {
    private String heading , desc;
    private Button donate, learn;
    private Offers(){

    }
    private Offers(String heading, String desc)
    {
        this.heading=heading;
        this.desc=desc;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
