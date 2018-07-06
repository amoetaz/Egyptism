package com.project.moetaz.egyptism.models;


import java.io.Serializable;

public class Site extends Region implements Serializable {
    private String  desc;
    private String latLong;

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getLatLong() {

        return latLong;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
