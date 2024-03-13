package com.alinakravckenkodev.crm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Services {
    public static void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) MainActivity.context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String convertTimestapmToDate(long timestamp) {
        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        String dateStr = df.format(new Date (timestamp));
        return dateStr;
    }

    public static float getLatitude(String str) {

        return Float.valueOf(str.split(",")[0]);
    }

    public static float getLongitude(String str) {

        return Float.valueOf(str.split(",")[1]);
    }

    public static Bitmap getBitmapFromByteArray(byte[] byteArray) {
        System.out.println("getBitmapFromByteArray...");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return bmp;

    }


}
