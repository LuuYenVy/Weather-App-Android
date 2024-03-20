package com.example.project_dbreathuit_app;
import android.content.ContextWrapper;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.project_dbreathuit_app.LocaleHelper;

import com.example.project_dbreathuit_app.ProfileActivity;



public class MyDialogFragment extends DialogFragment {
    Context context;
    Resources resources;
    private SharedPreferences sharedPreferences;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] choices = {"English", "VietNam", "France","ThaiLand"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Change Language")
                .setPositiveButton("Confirm", (dialog, which) -> {

                })

                .setSingleChoiceItems(choices, 0, (dialog, which) -> {
                    String selectedItem = choices[which];
                    String language="en";
                    if (selectedItem.equals("VietNam")) {
                        language="vi";
                    } else if (selectedItem.equals("English")) {
                        language="en";
                    } else if(selectedItem.equals("France")){
                        language="fr";
                    }
                    else{
                        language="th";
                    }

                });

        return builder.create();
    }



}
