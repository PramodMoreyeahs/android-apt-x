package com.apt_x.app.db;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.apt_x.app.model.bean.OrmBeanList;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class used for Inserting,updating,deleting data in database.
 */
@SuppressWarnings("ALL")
public class CRUDHelper extends GetRecordDbHelper {
    public CRUDHelper(Context context) {
        super(context);
    }

    /**
     * Insert category list in @CategoryList Tabel
     */
    public synchronized void insertCategoryDataList(ArrayList<OrmBeanList> categoryLists) {
        if (categoryLists != null && categoryLists.size() > 0) {
            clearCategoryTable();
            for (int i = 0; i < categoryLists.size(); i++) {
                OrmBeanList categoryList = new OrmBeanList();
                categoryList.setName(categoryLists.get(i).getName());
                categoryList.setId(categoryLists.get(i).getId());
                categoryList.setCity(categoryLists.get(i).getCity());
                categoryList.setContactNo(categoryLists.get(i).getContactNo());
                categoryList.setState(categoryLists.get(i).getState());
                getRuntimeExceptionDao(OrmBeanList.class).create(categoryList);
            }
        }
    }
   
    public synchronized boolean checkIdExistInTable(String vendorId) {
        try {
            ArrayList<OrmBeanList> list = (ArrayList) getRuntimeExceptionDao(OrmBeanList.class)
                    .queryBuilder().where().
                            eq("id", vendorId).query();
            if (list.size() > 0 && list != null) {
                return true;
            }
        } catch (SQLException e) {
            Log.e("excpetion", e.getMessage());
        }
        return false;
    }

    /*
    *
    *
    * Delete Gallery Images from database.
    *
    * */
    public synchronized void deleteTabelData(String vendorId) {
        try {
            DeleteBuilder deleteBuilder = (DeleteBuilder)
                    getRuntimeExceptionDao(OrmBeanList.class).deleteBuilder();
            deleteBuilder.where().eq("id", vendorId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            Log.e("expetion", e.getMessage());
        }
    }


    /*
    *
    * Insertion of Vendor gallery images in database.
    *
    * */

    public synchronized void insertData(ArrayList<OrmBeanList> dataArrayList,
                                        String vendorId) {
        if (dataArrayList != null && dataArrayList.size() > 0) {

            boolean checkIdExistOrNot = checkIdExistInTable(vendorId);

            if (checkIdExistOrNot) {
                deleteTabelData(vendorId);
            }
            for (int i = 0; i < dataArrayList.size(); i++) {
                getRuntimeExceptionDao(OrmBeanList.class).create(dataArrayList.get(i));

            }
        }
    }

    //___________________________VendorReviews_____________________________

    /*
    *
    * Method used to check weather vendor id exist in the ReviewListItem
    *
    * */
  /*  public synchronized boolean checkVendorIdExistOrNotInReviewTable(String vendorId) {
        try {
            ArrayList<ReviewListItem> list = (ArrayList) getRuntimeExceptionDao(ReviewListItem.class)
                    .queryBuilder().where().
                            eq("vendor_id", vendorId).query();
            if (list.size() > 0 && list != null) {
                return true;
            }
        } catch (SQLException e) {
            Log.e("expetion", e.getMessage());
        }
        return false;
    }*/

    /*
   *
   *
   * Delete Vendor Reviews from database.
   *
   * */
   /* public synchronized void deleteVendorReviews(String vendorId) {
        try {
            DeleteBuilder deleteBuilder = (DeleteBuilder)
                    getRuntimeExceptionDao(ReviewListItem.class).deleteBuilder();
            deleteBuilder.where().eq("vendor_id", vendorId);
            deleteBuilder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /*
    *
    * Insertion of Vendor Reviews in database.
    *
    * */

   /* public synchronized void insertVendorReviews(ArrayList<ReviewListItem> reviewListItems, String vendorId) {
        if (reviewListItems != null && reviewListItems.size() > 0) {
            boolean checkVendorIdExistOrNot = checkVendorIdExistOrNotInReviewTable(vendorId);
            if (checkVendorIdExistOrNot) {
                deleteVendorReviews(vendorId);
            }
            for (int i = 0; i < reviewListItems.size(); i++) {
                if (i <= 50) {
                    getRuntimeExceptionDao(ReviewListItem.class).create(reviewListItems.get(i));
                } else {
                    break;
                }
            }
        }
    }*/




 

}