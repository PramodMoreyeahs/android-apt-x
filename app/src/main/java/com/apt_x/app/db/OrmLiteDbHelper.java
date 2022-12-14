package com.apt_x.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.apt_x.app.model.bean.OrmBeanList;

import java.sql.SQLException;

@SuppressWarnings("ALL")
public class OrmLiteDbHelper extends OrmLiteSqliteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";
    public SQLiteDatabase sqLiteDatabase;
    private Context mContext;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    public OrmLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            sqLiteDatabase = getWritableDatabase();
        } catch (Exception e) {
            Log.e("exeption", "" + e.getMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        sqLiteDatabase = database;
        createTable(connectionSource);
    }


    /*
    *
    *
    * Method used to create database tabels.
    *
    *
    * */
    public void createTable(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, OrmBeanList.class);
        } catch (SQLException e) {
            Log.e("exeption", "" + e.getMessage());
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, OrmBeanList.class, false);
            createTable(connectionSource);
        } catch (Exception e) {
            createTable(connectionSource);
            Log.e("EXCEPTION", "" + e.getMessage());
        }
    }


    /*
    *
    *
    * Clear Data from CategoryList table.
    *
    * */
    public void clearCategoryTable() {
        try {
            TableUtils.clearTable(connectionSource, OrmBeanList.class);
        } catch (SQLException e) {
            Log.e("EXCEPTION", "" + e.getMessage());
        }

    }







}