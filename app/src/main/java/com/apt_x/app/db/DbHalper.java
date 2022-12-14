package com.apt_x.app.db;

import android.annotation.SuppressLint;
import android.content.Context;

import com.apt_x.app.application.MyApp;


@SuppressWarnings("ALL")
public class DbHalper extends CRUDHelper {
    @SuppressLint("StaticFieldLeak")
    public static DbHalper dbHalper;

    public static DbHalper getInstance(Context context) {
        if (dbHalper == null) {
            if(context==null){
                context= MyApp.getInstance().getApplicationContext();
            }
            dbHalper = new DbHalper(context);
        }
        return dbHalper;
    }

    public DbHalper(Context context) {
        super(context);
    }


}