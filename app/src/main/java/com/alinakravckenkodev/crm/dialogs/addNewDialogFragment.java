package com.alinakravckenkodev.crm.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

public class addNewDialogFragment extends DialogFragment {

    String winner = "";
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            winner = bundle.getString("winner");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(winner)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Игра окончена")
                .setPositiveButton("Выбрать следующий", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Переход
                        dismiss();
                        getActivity().finish();
                        getActivity().recreate();

                    }
                })
                .create();
    }



}