package com.apt_x.app.db;

import android.content.Context;


import com.apt_x.app.model.bean.OrmBeanList;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class GetRecordDbHelper extends OrmLiteDbHelper {
    Context mContext;
    /*
    *
    * Default Constructor.
    *
    * */
    public GetRecordDbHelper(Context context) {
        super(context);
        mContext = context;
    }


    /*
    *
    *
    * Get Category List from Tabel.
    *
    * */
    public synchronized ArrayList<OrmBeanList> getCategoryListData() {
        ArrayList<OrmBeanList> getCategoryList = null;
        try {
            getCategoryList = (ArrayList<OrmBeanList>) getRuntimeExceptionDao(OrmBeanList.class)
                    .queryBuilder().query();
            return getCategoryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getCategoryList;
    }



    public synchronized ArrayList<OrmBeanList> getDataList(Context context, String vendorId) {
        ArrayList<OrmBeanList> getGalleryList = null;
        try {
            getGalleryList = (ArrayList<OrmBeanList>) getRuntimeExceptionDao(OrmBeanList.class)
                    .queryBuilder().where().eq("id", vendorId).query();
            return getGalleryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getGalleryList;
    }

  }