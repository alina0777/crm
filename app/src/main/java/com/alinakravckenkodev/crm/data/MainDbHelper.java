package com.alinakravckenkodev.crm.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MainDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "main01.db";
    private static final int DATABASE_VERSION = 1;

    public static MainDbHelper mDbHelper;

    public MainDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_DOCS_TABLE = "CREATE TABLE " + Main.CustomerForms.TABLE_NAME +
                " ("+ Main.CustomerForms._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Main.CustomerForms.full_name + " TEXT NOT NULL, "
                + Main.CustomerForms.date_create + " TEXT NOT NULL, "
                + Main.CustomerForms.date_finish + " TEXT NOT NULL, "
                + Main.CustomerForms.geo_create + " TEXT NOT NULL, "
                + Main.CustomerForms.geo_finish + " TEXT NOT NULL, "
                + Main.CustomerForms.tsa_name + " TEXT NOT NULL, "
                + Main.CustomerForms.tsa_address + " TEXT NOT NULL, "
                + Main.CustomerForms.photo_base64 + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_DOCS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public static void addFormInBase(SQLiteDatabase db,
                                        String full_name,
                                        String date_create,
                                        String date_finish,
                                        String geo_create,
                                        String geo_finish,
                                        String tsa_name,
                                        String tsa_address,
                                        String photo_base64) {

        long unixTime = System.currentTimeMillis() / 1000L;

        ContentValues values = new ContentValues();
        values.put(Main.CustomerForms.full_name, full_name);
        values.put(Main.CustomerForms.date_create, date_create);
        values.put(Main.CustomerForms.date_finish, date_finish);
        values.put(Main.CustomerForms.geo_create, geo_create);
        values.put(Main.CustomerForms.geo_finish, geo_finish);
        values.put(Main.CustomerForms.tsa_name, tsa_name);
        values.put(Main.CustomerForms.tsa_address, tsa_address);
        values.put(Main.CustomerForms.photo_base64, photo_base64);


        long newRowId = db.insert(Main.CustomerForms.TABLE_NAME, null, values);

    }

//    public static Boolean changeDataAirplane(SQLiteDatabase db,
//                                             String _ID,
//                                             String new_name,
//                                             String new_year,
//                                             String new_message,
//                                             String bitmap_string_image){
//        try {
//            System.out.println("changeDataAirplane____start");
//
//
//            ContentValues values = new ContentValues();
//            values.put(Main.AirplanesBase.name, String.valueOf(new_name));
//            values.put(Main.AirplanesBase.year, String.valueOf(new_year));
//            values.put(Main.AirplanesBase.message, String.valueOf(new_message));
//            values.put(Main.AirplanesBase.bitmap_string_image, String.valueOf(bitmap_string_image));
//
//            db.update(Main.AirplanesBase.TABLE_NAME, values,
//                    "_ID=?",
//                    new String[]{
//                            _ID
//                    });
//
//            return true;
//
//        }   catch (Error error){
//            return false;
//        }
//    }


    public static SQLiteDatabase getDatabase(Context context) {

            mDbHelper = new MainDbHelper(context);
            SQLiteDatabase  vDb = mDbHelper.getWritableDatabase();

        return vDb;
    }

}