package com.apt_x.app.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 🔥 MVP-I Architecture and Android Jetpack 🔥
 * 🍴 Focused on Clean Architecture
 * Created by 🔱 Pratik Kataria 🔱 on 15-07-2021.
 */
public class DateParser {

    public static String getDateFormat(String serverDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (serverDate == null) return "";

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());//These format come to server
        originalFormat.setTimeZone(TimeZone.getDefault());
        DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());  //change to new format here  //dd-MM-yyyy HH:mm:ss
        String formattedDate = "";
        try {
            Date date = originalFormat.parse(serverDate);
            formattedDate = targetFormat.format(date);  //result
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getDateFormatV2(String serverDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (serverDate == null) return "";

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//These format come to server
        originalFormat.setTimeZone(TimeZone.getDefault());
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());  //change to new format here  //dd-MM-yyyy HH:mm:ss
        String formattedDate = "";
        try {
            Date date = originalFormat.parse(serverDate);
            formattedDate = targetFormat.format(date);  //result
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getDateFormatV4(String serverDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (serverDate == null) return "";

        DateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());//These format come to server
        originalFormat.setTimeZone(TimeZone.getDefault());
        DateFormat targetFormat = new SimpleDateFormat("dd mm yyyy", Locale.getDefault());  //change to new format here  //dd-MM-yyyy HH:mm:ss
        String formattedDate = "";
        try {
            Date date = originalFormat.parse(serverDate);
            formattedDate = targetFormat.format(date);  //result
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String simpleDate(String serverDate) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        String reformattedStr = "";
        try {

            reformattedStr = myFormat.format(fromUser.parse(serverDate));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    public static String simpleDatehistory(String serverDate) {
        String month;
        String monthname = "";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat myFormat = new SimpleDateFormat("dd yy h:mmaa");

        String reformattedStr = "";
        try {

            month = String.valueOf(fromUser.parse(serverDate));
            monthname = month.substring(4, 7)+" ";
            System.out.println("formatted date 111" + monthname);

            reformattedStr = myFormat.format(fromUser.parse(serverDate));
            System.out.println("formatted date 222" + reformattedStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthname + reformattedStr;
    }


    public static String simpleTime(String serverDate) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm aa");
        String reformattedStr = "";
        try {

            reformattedStr = myFormat.format(fromUser.parse(serverDate));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    public static String simpleOnlyDate(String serverDate) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String reformattedStr = "";
        try {

            reformattedStr = myFormat.format(fromUser.parse(serverDate));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }


}
