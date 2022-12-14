package com.apt_x.app.authsdk.verifyAucant;

import android.graphics.Bitmap;

/**
 * Created by tapasbehera on 5/16/18.
 */
public class ProcessedData {
    public static Bitmap frontImage = null;
    public static Bitmap backImage = null;
    public static Bitmap faceImage = null;
    public static Bitmap capturedFaceImage = null;
    public static Bitmap signImage = null;
    public static String formattedString="";
    public static String dateOfBirth = "";
    public static String instanceId = null;
    public static String dateOfExpiry = null;
    public static String documentNumber = null;
    public static boolean isHealthCard = false;
    public static String cardType = "ID1";
    public static String country = "unknown";
    public static String frontImageUri="";
    public static String backImageUri="";
    public static String faceImageUri="";
    public static void cleanup() {
        frontImage = null;
        backImage = null;
        faceImage = null;
        capturedFaceImage = null;
        signImage = null;
        formattedString="";
        dateOfExpiry = null;
        documentNumber = null;
        instanceId = null;
        isHealthCard = false;
        cardType = "ID1";
    }
}
