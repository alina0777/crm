package com.alinakravckenkodev.crm.objects;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alinakravckenkodev.crm.MainActivity;
import com.alinakravckenkodev.crm.R;
import com.alinakravckenkodev.crm.Services;
import com.alinakravckenkodev.crm.data.Main;
import com.alinakravckenkodev.crm.data.MainDbHelper;
import com.alinakravckenkodev.crm.dialogs.ChangeGeoStartFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Form {
    public PopupWindow popupWindow;


    int ID = 0;

    long date_create = 0;
    long date_finish = 0;
    String geo_create = "";
    String geo_finish = "";
    String tsa_name = "";
    String tsa_address = "";
    byte[] photoByteArray = null;
    String comment = "";

    AppCompatActivity activity;


    TextView numberForm_text;
    TextView nameTsa_text;
    EditText nameTSA_Edit;
    EditText addressTSA_Edit;
    EditText comment_Edit;
    TextView dateCreate_text;
    TextView geoCreate_text;
    TextView geoFinish_text;
    TextView dateFinish_text;
    Button latter_button;
    Button finish_button;
    Button takePhoto;
    Button showPhoto;

    public Photo photoManager = null;

    public void createWindow (AppCompatActivity activity) {
        this.activity = activity;

        LayoutInflater inflater = this.activity.getLayoutInflater();
        View mainView = inflater.inflate(R.layout.form, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;
        popupWindow = new PopupWindow(mainView, width, height, focusable);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("Form_dismiss_111");

                setTsa_name(nameTSA_Edit.getText().toString());
                setTsa_address(addressTSA_Edit.getText().toString());
                setComment(comment_Edit.getText().toString());

                if (photoManager.photoURI!=null) {
                    try {
                        Bitmap theImage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), photoManager.photoURI);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        theImage.compress(Bitmap.CompressFormat.JPEG, 25, stream);

                        byte[] byteArrayFile = stream.toByteArray();

                        setPhoto_file(byteArrayFile);

                        System.out.println("byteArrayFile.length = "+byteArrayFile.length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                saveFormInDatabase();


                MainActivity.enterRowsInFormList(activity.findViewById(R.id.tableList));
            }
        });

        popupWindow.showAtLocation(this.activity.findViewById(R.id.tableList), Gravity.CENTER, 0, 0);

        numberForm_text = mainView.findViewById(R.id.numberForm);
        nameTsa_text = mainView.findViewById(R.id.nameTSA_text);

        nameTsa_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Services.hideKeyboardFrom(mainView);
            }
        });

        nameTSA_Edit = mainView.findViewById(R.id.nameTSA_edit);
        addressTSA_Edit = mainView.findViewById(R.id.address_tsa_edit);
        comment_Edit = mainView.findViewById(R.id.comment_edit);
        dateCreate_text = mainView.findViewById(R.id.textDateCreate);
        geoCreate_text = mainView.findViewById(R.id.textGeoCreate);
        dateFinish_text = mainView.findViewById(R.id.textDateFinish);
        geoFinish_text = mainView.findViewById(R.id.textGeoFinish);

        geoCreate_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeGeoStartFragment fragment = new ChangeGeoStartFragment(Form.this);
                Bundle args = new Bundle();
                args.putInt("ID", ID);
                fragment.setArguments(args);
                fragment.show(activity.getSupportFragmentManager(), "custom");
            }
        });

        latter_button = mainView.findViewById(R.id.buttonFinishLater);
        latter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });

        finish_button = mainView.findViewById(R.id.buttonFinish);
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate_finish(new Date().getTime());
                Thread task1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        enterGeoData();
                    }
                });

                try {
                    task1.join();
                    task1.start();

                    popupWindow.dismiss();


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        takePhoto = mainView.findViewById(R.id.buttonTakePhoto);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoManager.takePhoto();
            }
        });

        showPhoto = mainView.findViewById(R.id.buttonShowPhoto);
        showPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoManager.createWindow();
            }
        });

        update();

        photoManager = new Photo (Form.this);


        if (this.date_finish!=0) {
            nameTSA_Edit.setEnabled(false);
            addressTSA_Edit.setEnabled(false);
            comment_Edit.setEnabled(false);
            takePhoto.setEnabled(false);
            finish_button.setEnabled(false);

        }
    }

    public void setID (int ID) {
        this.ID = ID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate_create(long date_create) {
        this.date_create = date_create;
    }

    public void setDate_finish(long date_finish) {
        this.date_finish = date_finish;
    }

    public void setGeo_Create(String geo_create) {
        this.geo_create = geo_create;
    }

    public void setGeo_finish(String geo_finish) {
        this.geo_finish = geo_finish;
    }

    public void setTsa_name(String tsa_name) {
        this.tsa_name = tsa_name;
    }

    public void setTsa_address(String tsa_address) {
        this.tsa_address = tsa_address;
    }

    public void setPhoto_file(byte[] photo_file) {
        System.out.println("setPhoto_file");
        System.out.println("photo_file = " +String.valueOf(photo_file));

        this.photoByteArray = photo_file;
    }


    public int getID() {
        return this.ID;
    }

    public String getComment() {
        return this.comment;
    }

    public long getDate_create() {
        return this.date_create;
    }

    public long getDate_finish() {
        return this.date_finish;
    }

    public String getGeo_Create() {
        return this.geo_create;
    }

    public String getGeo_finish() {
        return this.geo_finish;
    }

    public String getTsa_name() {
        return this.tsa_name;
    }

    public String getTsa_address() {
        return this.tsa_address;
    }

    public byte[] getPhoto_file() {
        return this.photoByteArray;
    }


    public AppCompatActivity getActivity() {
        return activity;
    }

    public void saveFormInDatabase() {
        System.out.println("saveFormInDatabase");
        System.out.println("ID = " + ID);
        System.out.println("this.photoByteArray = " + String.valueOf(this.photoByteArray));


        ContentValues values = new ContentValues();

        values.put(Main.CustomerForms.date_create, this.date_create);
        values.put(Main.CustomerForms.date_finish, this.date_finish);

        values.put(Main.CustomerForms.geo_create, this.geo_create);

        values.put(Main.CustomerForms.geo_finish, this.geo_finish);
        values.put (Main.CustomerForms.tsa_name, this.tsa_name);
        values.put (Main.CustomerForms.tsa_address, this.tsa_address);

        values.put (Main.CustomerForms.photo, this.photoByteArray);

        values.put (Main.CustomerForms.comment, this.comment);

        MainActivity.database.update(Main.CustomerForms.TABLE_NAME,
            values,
            "_id=?",
            new String[] { String.valueOf(ID) });


        MainDbHelper.getPhotoForm(ID);
    }



    public void update () {
        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        numberForm_text.setText("Анкета #"+ID);
        nameTSA_Edit.setText(tsa_name);
        addressTSA_Edit.setText(tsa_address);
        comment_Edit.setText(comment);


        dateCreate_text.setText(df.format(new Date(date_create)));
        geoCreate_text.setText(geo_create);

        if (date_finish!=0)
            dateFinish_text.setText(df.format(new Date(date_finish)));

        if (!geo_finish.equals(""))
            geoFinish_text.setText(geo_finish);



    }

    public void enterGeoData () {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity.getApplicationContext());

        if (ActivityCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    if (geo_create.equals("")) {
                        geo_create = location.getLatitude() + "," + location.getLongitude();

                    } else {
                        geo_finish = location.getLatitude() + "," + location.getLongitude();
                    }

                    saveFormInDatabase();
                    update();

                } else {
                    geoCreate_text.setTextColor(Color.RED);
                    geoCreate_text.setText("Геолокация недоступна");
                }
            }
        });
    }





}



