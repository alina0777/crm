package com.alinakravckenkodev.crm;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alinakravckenkodev.crm.data.MainDbHelper;
import com.alinakravckenkodev.crm.dialogs.ChangeGeoStartFragment;
import com.alinakravckenkodev.crm.objects.Form;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CardForm extends AppCompatActivity {
    boolean isNew = true;
    int ID = 0;
    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        //Узнаем нужно заполнить форму существующими данными или создать пустую форму
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isNew = extras.getBoolean("isNew");

            if (isNew == true) {
                ID = MainDbHelper.addFormInBase(MainActivity.database,
                        0,
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "");
            } else {
                ID = extras.getInt("ID");
            }
        }

        //Создаем объект Анкеты, инициализируем его значениями из базы данных, используя его ID в базе
        form = new Form();
        //form = MainDbHelper.getFormByID(ID);

        //Если время создания анкеты не выставлено, инициализируем объект Анкеты текущей датой
        if (form.getDate_create() == 0) {
            form.setDate_create( new Date().getTime());
        }

        initLocation(false);

        //currentForm.saveDataFormInDatabase();


        initAllTexts();



        TextView nameTSA_text = findViewById(R.id.nameTSA_text);
        nameTSA_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Services.hideKeyboardFrom(view);
            }
        });

        Button latter = findViewById(R.id.buttonFinishLater);
        latter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button finish = findViewById(R.id.buttonFinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void initAllTexts() {
        System.out.println("initAllTexts");


        TextView numberForm = findViewById(R.id.numberForm);
        TextView dataCreate = findViewById(R.id.textDateCreate);

        EditText nameTsa_edit = findViewById(R.id.nameTSA_edit);
        EditText address_edit = findViewById(R.id.address_tsa_edit);

        numberForm.setText("Анкета #" + form.getID());

        long timeStapmCreate = form.getDate_create();

        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        dataCreate.setText(df.format(new Date(timeStapmCreate)));

        nameTsa_edit.setText(form.getTsa_name());
        address_edit.setText(form.getTsa_address());
    }


    public void initLocation(Boolean is_finish) {
        System.out.println("initLocation...");

        FusedLocationProviderClient fusedLocationProviderClient;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.context);

        if (ActivityCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) MainActivity.context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    String _str = location.getLatitude() + "," + location.getLongitude();
                    System.out.println("location: " + location.getLatitude() + "," + location.getLongitude());

                    if (!is_finish) {
                        form.setGeo_Create(_str);
                    } else {
                        form.setGeo_finish(_str);
                    }
                    //Процедура отрисовывает данные геолокации после окончания поиска расположения
                    initTextLocation(is_finish);

                    //Сохранение всех полей Анкеты после окончания получения Гео
                    form.saveFormInDatabase();
                } else {
                    Toast toast;
                    toast = Toast.makeText(getApplicationContext(), "Геолокаия недоступна",Toast.LENGTH_LONG);
                    toast.show();

                    initTextLocation(is_finish);
                    form.saveFormInDatabase();

                }
            }
        });
    }

    public void initTextLocation(Boolean is_finish) {
        if (!is_finish)
        {
            TextView geoCreateText = findViewById(R.id.textGeoCreate);
            geoCreateText.setText(form.getGeo_Create());

            geoCreateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangeGeoStartFragment dialog = new ChangeGeoStartFragment(form);
                    Bundle args = new Bundle();
                    args.putInt("ID", ID);
                    dialog.setArguments(args);
                    dialog.show(getSupportFragmentManager(), "custom");
                }
            });
        }
    }


}