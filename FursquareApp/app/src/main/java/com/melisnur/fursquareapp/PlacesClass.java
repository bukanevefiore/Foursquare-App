package com.melisnur.fursquareapp;

import android.graphics.Bitmap;

public class PlacesClass {

    private static  PlacesClass instance;

    private Bitmap image;
    private String name;
    private String type;
    private String atmosfer;

    private PlacesClass(){

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAtmosfer() {
        return atmosfer;
    }

    public void setAtmosfer(String atmosfer) {
        this.atmosfer = atmosfer;
    }

    public static PlacesClass getInstance() {
        if(instance == null){
            instance=new PlacesClass();
        }
        return instance;
    }
}
