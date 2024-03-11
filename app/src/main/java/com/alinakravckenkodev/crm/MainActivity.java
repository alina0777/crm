package com.alinakravckenkodev.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alinakravckenkodev.crm.data.MainDbHelper;
import com.alinakravckenkodev.crm.objects.Form;
import com.alinakravckenkodev.crm.objects.WindowMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static AppCompatActivity activity;

    public static Context context;
    public static SQLiteDatabase database;
    public static FusedLocationProviderClient fusedLocationProviderClient;

    ArrayList<String> marks_gps = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marks_gps.add("50.4775197,30.4331252"); //Сирец
        marks_gps.add("50.4747006, 30.4491159"); //Дорогожичи
        marks_gps.add("50.4621155, 30.4844467"); //Лукьяновка

        activity = this;
        context = getApplicationContext();
        database = MainDbHelper.getDatabase(context);


        LinearLayout tableList = findViewById(R.id.tableList);


        enterRowsInFormList(tableList);


        FloatingActionButton fabNewForm = findViewById(R.id.floatingActionButton_createForm);
        fabNewForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Form newForm = new Form();
                MainDbHelper.initForm(newForm);
                newForm.setDate_create(new Date().getTime());
                newForm.CreateWindow(MainActivity.this);
                newForm.enterGeoData();

            }
        });

        FloatingActionButton fabOpenMap = findViewById(R.id.floatingActionButton_showMap);

        fabOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowMap windowMap = new WindowMap();
                windowMap.CreateWindow(MainActivity.this);

            }
        });
    }

    public static void enterRowsInFormList (LinearLayout tableList) {
        tableList.removeAllViews();

        TableRow.LayoutParams paramsRow = new TableRow.LayoutParams();
        paramsRow.width = TableRow.LayoutParams.MATCH_PARENT;
        paramsRow.height = TableRow.LayoutParams.WRAP_CONTENT;

        TableRow rowHead = new TableRow(context);
        rowHead.setGravity(Gravity.CENTER);
        rowHead.setLayoutParams(paramsRow);
        rowHead.setBackgroundColor(activity.getResources().getColor(R.color.yelow));
        rowHead.setWeightSum(100);

        TableRow.LayoutParams paramsName = new TableRow.LayoutParams();
        paramsName = new TableRow.LayoutParams();
        paramsName.weight = 40;
        paramsName.width = TableRow.LayoutParams.MATCH_PARENT;
        paramsName.height = TableRow.LayoutParams.WRAP_CONTENT;

        TextView nameTSA = new TextView(context);
        nameTSA.setText("Название");
        nameTSA.setTextColor(Color.BLACK);
        nameTSA.setTextSize(25);
        nameTSA.setLayoutParams(paramsName);
        rowHead.addView(nameTSA, 0);

        TableRow.LayoutParams paramsDateCreate = new TableRow.LayoutParams();
        paramsDateCreate = new TableRow.LayoutParams();
        paramsDateCreate.weight = 60;
        paramsDateCreate.width = TableRow.LayoutParams.MATCH_PARENT;
        paramsDateCreate.height = TableRow.LayoutParams.WRAP_CONTENT;

        TextView dateCreate = new TextView(context);
        dateCreate.setText("Дата заполнения");
        dateCreate.setTextSize(25);
        dateCreate.setTextColor(Color.BLACK);
        dateCreate.setLayoutParams(paramsDateCreate);
        rowHead.addView(dateCreate, 1);


        tableList.addView(rowHead);

        ArrayList<Form> listForms = MainDbHelper.getListForms(database);

        TableRow.LayoutParams param = new TableRow.LayoutParams();
        param.weight = 50;

        for (int i = 0; i < listForms.size(); i++) {

            TableRow row = new TableRow(context);
            row.setOrientation(TableRow.HORIZONTAL);

            row.setWeightSum(100);
            row.setBackgroundColor (activity.getResources().getColor(R.color.bisque));

            TextView tsa_name = new TextView(context);
            int finalI = i;
            tsa_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Form form = new Form();
                    form.setID(listForms.get(finalI).getID());
                    MainDbHelper.initForm(form);
                    form.CreateWindow(activity);
                }
            });

            tsa_name.setText(listForms.get(i).getID()  + " - " +listForms.get(i).getTsa_name());
            tsa_name.setTextSize(25);
            tsa_name.setLayoutParams(param);
            row.addView(tsa_name,0);


            TextView timeCreate = new TextView(context);

            DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

            long timeStamp = listForms.get(i).getDate_create();
            String createTime = df.format(new Date (timeStamp));

            timeCreate.setText(createTime);
            timeCreate.setTextSize(25);
            timeCreate.setLayoutParams(param);

            row.addView(timeCreate,1);

            tableList.addView(row);
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        recreate();
    }


}