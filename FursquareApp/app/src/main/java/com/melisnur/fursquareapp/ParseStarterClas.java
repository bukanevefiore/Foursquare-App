package com.melisnur.fursquareapp;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClas extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("vAUD72kCDZqFmaimFx2Vaaqoyz1Qgpfyd0LBtJox")
                .clientKey("hXjkSkWnx6FlvqQPUP5dna1bBCoIJwlTAl5WMgXt")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
