package com.alinakravckenkodev.crm.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

import com.alinakravckenkodev.crm.MainActivity;
import com.alinakravckenkodev.crm.R;
import com.alinakravckenkodev.crm.data.Main;
import com.alinakravckenkodev.crm.data.MainDbHelper;
import com.alinakravckenkodev.crm.objects.Form;

public class ChangeGeoStartFragment extends DialogFragment {
    Form form;
    public ChangeGeoStartFragment (Form form) {
        this.form = form;
    }


    int ID;
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        Bundle args = this.getArguments();
        ID = args.getInt("ID");


        LinearLayout table = new LinearLayout(MainActivity.context);
        table.setOrientation(LinearLayout.VERTICAL);

        TextView textEnterGeo = new TextView(MainActivity.context);
        textEnterGeo.setText("Введите новую геолокацию:");
        table.addView(textEnterGeo);

//        marks_gps.add("50.4775197,30.4331252"); //Сирец
//        marks_gps.add("50.4747006,30.4491159"); //Дорогожичи
//        marks_gps.add("50.4621155,30.4844467"); //Лукьяновка


        String[] listGeoMarks = { "50.4775197,30.4331252 - Сирец","50.4747006,30.4491159 - Дорогожичи","50.4621155,30.4844467 - Лукьяновка" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (form.getActivity() ,android.R.layout.select_dialog_item, listGeoMarks);

        AutoCompleteTextView actv = new AutoCompleteTextView(MainActivity.context);
        actv.setThreshold(0);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
        table.addView(actv);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setView(table)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("textGeo = " + actv.getText().toString());
                        MainDbHelper.updateTextDataByID(ID, Main.CustomerForms.geo_create, actv.getText().toString());
                        form.setGeo_Create(actv.getText().toString());
                        form.update();
                    }
                })
                .create();
    }



}