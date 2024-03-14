package com.alinakravckenkodev.crm.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;

import com.alinakravckenkodev.crm.MainActivity;
import com.alinakravckenkodev.crm.objects.Form;

import java.util.ArrayList;
import java.util.Date;


public class MainDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "main01.db";
    private static final int DATABASE_VERSION = 78;
    public static MainDbHelper mDbHelper;

    public MainDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {

        String SQL_CREATE_DOCS_TABLE = "CREATE TABLE " + Main.CustomerForms.TABLE_NAME +
                " ("+ Main.CustomerForms._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Main.CustomerForms.comment + " TEXT, "
                + Main.CustomerForms.date_create + " INTEGER NOT NULL, "
                + Main.CustomerForms.date_finish + " INTEGER, "
                + Main.CustomerForms.geo_create + " TEXT NOT NULL, "
                + Main.CustomerForms.geo_finish + " TEXT, "
                + Main.CustomerForms.tsa_name + " TEXT NOT NULL, "
                + Main.CustomerForms.tsa_address + " TEXT NOT NULL, "
                + Main.CustomerForms.photo + " BLOB);";

        db.execSQL(SQL_CREATE_DOCS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         if (oldVersion < newVersion) {


            String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + Main.CustomerForms.TABLE_NAME;
            db.execSQL(SQL_DROP_TABLE);


            String SQL_CREATE_DOCS_TABLE = "CREATE TABLE " + Main.CustomerForms.TABLE_NAME +
                    " ("+ Main.CustomerForms._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Main.CustomerForms.comment + " TEXT, "
                    + Main.CustomerForms.date_create + " INTEGER NOT NULL, "
                    + Main.CustomerForms.date_finish + " INTEGER, "
                    + Main.CustomerForms.geo_create + " TEXT NOT NULL, "
                    + Main.CustomerForms.geo_finish + " TEXT, "
                    + Main.CustomerForms.tsa_name + " TEXT NOT NULL, "
                    + Main.CustomerForms.tsa_address + " TEXT NOT NULL, "
                    + Main.CustomerForms.photo + " BLOB);";

            db.execSQL(SQL_CREATE_DOCS_TABLE);
        }


    }

    public static int addFormInBase(   SQLiteDatabase database,
                                        long date_create,
                                        long date_finish,
                                        String geo_create,
                                        String geo_finish,
                                        String tsa_name,
                                        String tsa_address,
                                        byte[] photoByteArray,
                                        String comment) {

        long unixTime = System.currentTimeMillis() / 1000L;

        ContentValues values = new ContentValues();
        values.put(Main.CustomerForms.date_create, date_create);
        values.put(Main.CustomerForms.date_finish, date_finish);
        values.put(Main.CustomerForms.geo_create, geo_create);
        values.put(Main.CustomerForms.geo_finish, geo_finish);
        values.put(Main.CustomerForms.tsa_name, tsa_name);
        values.put(Main.CustomerForms.tsa_address, tsa_address);
        values.put(Main.CustomerForms.photo, photoByteArray);
        values.put(Main.CustomerForms.comment, comment);

        return (int) database.insert (Main.CustomerForms.TABLE_NAME, null, values);
    }


    public  static  void updateTextDataByID(int ID, String COLUMN_NAME, String value) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, value);

        MainActivity.database.update(Main.CustomerForms.TABLE_NAME,
                values,
                "_id=?",
                new String[] { String.valueOf(ID)});
    }



    public static ArrayList <MainActivity.ListDataForm> getListDataForms () {
        ArrayList<MainActivity.ListDataForm> array = new ArrayList<>();

        String[] projection = {
                Main.CustomerForms._ID,
                Main.CustomerForms.comment,
                Main.CustomerForms.date_create,
                Main.CustomerForms.date_finish,
                Main.CustomerForms.geo_create,
                Main.CustomerForms.geo_finish,
                Main.CustomerForms.tsa_name,
                Main.CustomerForms.tsa_address,
                Main.CustomerForms.photo
        };

        Cursor cursor = MainActivity.database.query (
                Main.CustomerForms.TABLE_NAME,                           // таблица
                projection,                                     // столбцы
                "",                               // столбцы для условия WHERE full_name>?
                null,                             // значения для условия WHERE new String[]{String.valueOf(timeFrom)}
                null,                             // Don't group the rows
                null,                             // Don't filter by row groups
                null);                            // порядок сортировки


        int idColumnIndex_ID = cursor.getColumnIndex(Main.CustomerForms._ID);
        int idColumnIndex_date_create = cursor.getColumnIndex(Main.CustomerForms.date_create);
        int idColumnIndex_date_finish = cursor.getColumnIndex(Main.CustomerForms.date_finish);
        int idColumnIndex_tsa_name = cursor.getColumnIndex(Main.CustomerForms.tsa_name);

        while (cursor.moveToNext()) {

            int _id = cursor.getInt (idColumnIndex_ID);
            long _date_create = cursor.getLong(idColumnIndex_date_create);
            long _date_finish = cursor.getLong(idColumnIndex_date_finish);
            String _tsa_name = cursor.getString(idColumnIndex_tsa_name);


            MainActivity.ListDataForm objForm = new MainActivity.ListDataForm ();
            objForm.ID = _id;
            objForm.name = _tsa_name;
            objForm.date_create = _date_create;
            objForm.date_finish = _date_finish;

            array.add(objForm);
        }
        return array;
    }


    public static ArrayList <Form> getListForms () {
        ArrayList<Form> arrayForms = new ArrayList<>();

        String[] projection = {
                Main.CustomerForms._ID,
                Main.CustomerForms.comment,
                Main.CustomerForms.date_create,
                Main.CustomerForms.date_finish,
                Main.CustomerForms.geo_create,
                Main.CustomerForms.geo_finish,
                Main.CustomerForms.tsa_name,
                Main.CustomerForms.tsa_address,
                Main.CustomerForms.photo
        };

        Cursor cursor = MainActivity.database.query (
                Main.CustomerForms.TABLE_NAME,                           // таблица
                projection,                                     // столбцы
                "",                               // столбцы для условия WHERE full_name>?
                null,                             // значения для условия WHERE new String[]{String.valueOf(timeFrom)}
                null,                             // Don't group the rows
                null,                             // Don't filter by row groups
                null);                            // порядок сортировки


        int idColumnIndex_ID = cursor.getColumnIndex(Main.CustomerForms._ID);
        int idColumnIndex_date_create = cursor.getColumnIndex(Main.CustomerForms.date_create);
        int idColumnIndex_date_finish = cursor.getColumnIndex(Main.CustomerForms.date_finish);
        int idColumnIndex_geo_create = cursor.getColumnIndex(Main.CustomerForms.geo_create);
        int idColumnIndex_geo_finish = cursor.getColumnIndex(Main.CustomerForms.geo_finish);
        int idColumnIndex_tsa_name = cursor.getColumnIndex(Main.CustomerForms.tsa_name);
        int idColumnIndex_tsa_address = cursor.getColumnIndex(Main.CustomerForms.tsa_address);
        int idColumnIndex_photo = cursor.getColumnIndex(Main.CustomerForms.photo);
        int idColumnIndex_comment = cursor.getColumnIndex(Main.CustomerForms.comment);

        while (cursor.moveToNext()) {

            int _id = cursor.getInt (idColumnIndex_ID);
            long _date_create = cursor.getLong(idColumnIndex_date_create);
            long _date_finish = cursor.getLong(idColumnIndex_date_finish);

            String _geo_create = cursor.getString(idColumnIndex_geo_create);
            String _geo_finish = cursor.getString(idColumnIndex_geo_finish);
            String _tsa_name = cursor.getString(idColumnIndex_tsa_name);
            String _tsa_address = cursor.getString(idColumnIndex_tsa_address);
            byte[] _photo_File = cursor.getBlob(idColumnIndex_photo);
            String _comment = cursor.getString(idColumnIndex_comment);


            Form objForm = new Form ();
            objForm.setID(_id);
            objForm.setDate_create(_date_create);
            objForm.setDate_finish(_date_finish);
            objForm.setGeo_Create(_geo_create);
            objForm.setGeo_finish(_geo_finish);
            objForm.setTsa_name(_tsa_name);
            objForm.setTsa_address(_tsa_address);
            objForm.setPhoto_file(_photo_File);

            objForm.setComment(_comment);


            arrayForms.add(objForm);
        }
        return arrayForms;
    }


    public static void initForm (Form form) {
        System.out.println("initForm");

        if (form.getID() == 0) {
            int ID = MainDbHelper.addFormInBase(MainActivity.database,
                    0,
                    0,
                    "",
                    "",
                    "",
                    "",
                    null,
                    "");

            form.setID(ID);
        }


        String[] projection = {
                Main.CustomerForms._ID,
                Main.CustomerForms.comment,
                Main.CustomerForms.date_create,
                Main.CustomerForms.date_finish,
                Main.CustomerForms.geo_create,
                Main.CustomerForms.geo_finish,
                Main.CustomerForms.tsa_name,
                Main.CustomerForms.tsa_address,
                Main.CustomerForms.photo
        };

        Cursor cursor = MainActivity.database.query (
                Main.CustomerForms.TABLE_NAME,                           // таблица
                projection,                                     // столбцы
                "_id=?",                               // столбцы для условия WHERE full_name>?
                new String[]{String.valueOf(form.getID())},      // значения для условия WHERE new String[]{String.valueOf(timeFrom)}
                null,                             // Don't group the rows
                null,                             // Don't filter by row groups
                null);                            // порядок сортировки

        int idColumnIndex_date_create = cursor.getColumnIndex(Main.CustomerForms.date_create);
        int idColumnIndex_date_finish = cursor.getColumnIndex(Main.CustomerForms.date_finish);
        int idColumnIndex_geo_create = cursor.getColumnIndex(Main.CustomerForms.geo_create);
        int idColumnIndex_geo_finish = cursor.getColumnIndex(Main.CustomerForms.geo_finish);
        int idColumnIndex_tsa_name = cursor.getColumnIndex(Main.CustomerForms.tsa_name);
        int idColumnIndex_tsa_address = cursor.getColumnIndex(Main.CustomerForms.tsa_address);
        int idColumnIndex_photo = cursor.getColumnIndex(Main.CustomerForms.photo);
        int idColumnIndex_comment = cursor.getColumnIndex(Main.CustomerForms.comment);

        cursor.moveToFirst();

        long _date_create = cursor.getLong(idColumnIndex_date_create);
        long _date_finish = cursor.getLong(idColumnIndex_date_finish);

        String _geo_create = cursor.getString(idColumnIndex_geo_create);
        String _geo_finish = cursor.getString(idColumnIndex_geo_finish);
        String _tsa_name = cursor.getString(idColumnIndex_tsa_name);
        String _tsa_address = cursor.getString(idColumnIndex_tsa_address);
        byte[] _photo_file = cursor.getBlob(idColumnIndex_photo);
        String _comment = cursor.getString(idColumnIndex_comment);


        form.setDate_create(_date_create);
        form.setDate_finish(_date_finish);
        form.setGeo_Create(_geo_create);
        form.setGeo_finish(_geo_finish);
        form.setTsa_name(_tsa_name);
        form.setTsa_address(_tsa_address);
        form.setPhoto_file(_photo_file);
        form.setComment(_comment);


        System.out.println("init_ID = " + form.getID() );
        System.out.println("init_photo_file = " + String.valueOf(_photo_file) );

    }


    public static String getStringByID (int ID, String COLUMN_NAME) {
        String[] projection = {
                Main.CustomerForms._ID,
                Main.CustomerForms.comment,
                Main.CustomerForms.date_create,
                Main.CustomerForms.date_finish,
                Main.CustomerForms.geo_create,
                Main.CustomerForms.geo_finish,
                Main.CustomerForms.tsa_name,
                Main.CustomerForms.tsa_address,
                Main.CustomerForms.photo
        };

        Cursor cursor = MainActivity.database.query (
                Main.CustomerForms.TABLE_NAME,                           // таблица
                projection,                                     // столбцы
                "_id=?",                               // столбцы для условия WHERE full_name>?
                new String[]{String.valueOf(ID)},      // значения для условия WHERE new String[]{String.valueOf(timeFrom)}
                null,                             // Don't group the rows
                null,                             // Don't filter by row groups
                null);                            // порядок сортировки


        int idColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
        cursor.moveToFirst();

        String response = cursor.getString(idColumnIndex);
        return response;
    }



    public static SQLiteDatabase getDatabase (Context context) {
            mDbHelper = new MainDbHelper(context);
            SQLiteDatabase  vDb = mDbHelper.getWritableDatabase();
        return vDb;
    }


//    public Bitmap getTheImage(){
//
//        Cursor cursor = (Cursor) MainActivity.database.rawQuery(" SELECT * FROM "+Main.CustomerForms.TABLE_NAME,null,null);
//
//
//        if (cursor.moveToFirst()) {
//            byte[] imgByte =  cursor.getBlob(cursor.getColumnIndex(ImageDatabase.KEY_IMG_URL));
//            cursor.close();
//            return BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
//        }
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//
//        return null;
//    }


    public static void getPhotoForm(int ID) {
        System.out.println("getPhotoForm...");
        String[] projection = {
                Main.CustomerForms._ID,
                Main.CustomerForms.comment,
                Main.CustomerForms.date_create,
                Main.CustomerForms.date_finish,
                Main.CustomerForms.geo_create,
                Main.CustomerForms.geo_finish,
                Main.CustomerForms.tsa_name,
                Main.CustomerForms.tsa_address,
                Main.CustomerForms.photo
        };

        Cursor cursor = MainActivity.database.query (
                Main.CustomerForms.TABLE_NAME,                           // таблица
                projection,                                     // столбцы
                "_id=?",                               // столбцы для условия WHERE full_name>?
                new String[]{String.valueOf(ID)},      // значения для условия WHERE new String[]{String.valueOf(timeFrom)}
                null,                             // Don't group the rows
                null,                             // Don't filter by row groups
                null);                            // порядок сортировки


        int idColumnIndex = cursor.getColumnIndex(Main.CustomerForms.photo);
        cursor.moveToFirst();

        byte[] response = cursor.getBlob(idColumnIndex);
        System.out.println("777response = " + String.valueOf(response));
    }
}