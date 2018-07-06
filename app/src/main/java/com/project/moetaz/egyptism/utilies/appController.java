package com.project.moetaz.egyptism.utilies;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;


public class appController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(getApplicationContext());
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        Picasso.Builder builder = new Picasso.Builder(this);
//        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
//        Picasso built = builder.build();
//        built.setLoggingEnabled(true);
//        Picasso.setSingletonInstance(built);
    }
}
