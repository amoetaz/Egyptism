package com.project.moetaz.egyptism.models;


import java.io.Serializable;

public class Region implements Serializable {
    protected String image;
    protected String name;

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }
}
