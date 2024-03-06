package com.alinakravckenkodev.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Получить с базы данных список заполненных анкет и поместить в массив

        //Отобразить и заполенить кнопки анкет

//        TextView initForm = findViewById(R.id.initForm);
//        initForm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
//                intent.putExtra("trigger", "trigger");
//                setResult(MainActivity.RESULT_OK, intent);
//                startActivityForResult(intent,101);
//                onNewIntent (intent);
//            }
//        });

        FloatingActionButton fabNewForm = findViewById(R.id.floatingActionButton);
        fabNewForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardForm.class);
                intent.putExtra("trigger", "trigger");
                setResult(MainActivity.RESULT_OK, intent);
                startActivityForResult(intent,101);
                onNewIntent (intent);
            }
        });



    }



    public void intNewForm() {

    }

}