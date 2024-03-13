package com.alinakravckenkodev.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alinakravckenkodev.crm.data.MainDbHelper;
import com.alinakravckenkodev.crm.objects.Form;
import com.alinakravckenkodev.crm.objects.WindowMap;
import com.google.android.gms.location.FusedLocationProviderClient;
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

    public static Form form = null;

    ArrayList<String> marks_gps = new ArrayList<>();


    public static class ListDataForm {
        public int ID;
        public String name;
        public long date_create;
    }

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

                form = new Form();
                MainDbHelper.initForm(form);
                form.setDate_create(new Date().getTime());
                form.createWindow(MainActivity.this);
                form.enterGeoData();

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

        ArrayList<MainActivity.ListDataForm> listDataForms = MainDbHelper.getListDataForms();

        TableRow.LayoutParams param = new TableRow.LayoutParams();
        param.weight = 50;

        for (int i = 0; i < listDataForms.size(); i++) {

            TableRow row = new TableRow(context);
            row.setOrientation(TableRow.HORIZONTAL);

            row.setWeightSum(100);
            row.setBackgroundColor (activity.getResources().getColor(R.color.bisque));

            TextView tsa_name = new TextView(context);
            int finalI1 = i;
            tsa_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    form = new Form();
                    form.setID(listDataForms.get(finalI1).ID);
                    MainDbHelper.initForm(form);

                    form.createWindow(activity);
                }
            });

            tsa_name.setText(listDataForms.get(i).ID  + " - " +listDataForms.get(i).name);
            tsa_name.setTextSize(25);
            tsa_name.setLayoutParams(param);
            row.addView(tsa_name,0);


            TextView timeCreate = new TextView(context);

            DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

            long timeStamp = listDataForms.get(i).date_create;
            String createTime = df.format(new Date (timeStamp));

            timeCreate.setText(createTime);
            timeCreate.setTextSize(25);
            timeCreate.setLayoutParams(param);

            row.addView(timeCreate,1);

            tableList.addView(row);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


}