package com.example.art_project;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class DialogHelper {
    Context context;

    OnDataUpdated fragmentMyDebtListener;
    OnDataUpdated fragmentTheirDebtListener;

    DialogHelper(Context context) {
        this.context = context;
    }

    public void setListener(OnDataUpdated onDataUpdated) {
        if (onDataUpdated instanceof FragmentMyDebt)
            this.fragmentMyDebtListener = onDataUpdated;

        if (onDataUpdated instanceof FragmentTheirDebt)
            this.fragmentTheirDebtListener = onDataUpdated;
    }

    public Dialog createAddDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_person);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setLayout(context.getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextInputLayout til_name = dialog.findViewById(R.id.til_name);
        final TextView tv_i_owe = dialog.findViewById(R.id.tv_i_owe);
        final TextView tv_owe_me = dialog.findViewById(R.id.tv_owe_me);
        final Switch sw = dialog.findViewById(R.id.sw);

        Button btn_add = dialog.findViewById(R.id.btn_add);
        btn_add.setText(R.string.btn_text_add);

        View.OnClickListener onButtonAddClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = -1;

                if (sw.isChecked())
                    status = 1;
                else if (!sw.isChecked()) {
                    status = 0;
                }

                if (Objects.requireNonNull(til_name.getEditText()).getText().toString().trim().equals("")) {
                    Toaster.makeToast(context, "No empty fields allowed!");
                } else {
                    Person newPerson = new Person(status, til_name.getEditText().getText().toString(), 0);

                    if (status == 0) {
                        fragmentMyDebtListener.onUpdate(newPerson);
                    }
                    else if (status == 1) {
                        fragmentTheirDebtListener.onUpdate(newPerson);
                    }
                    dialog.cancel();
                }
            }
        };

        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);

                if (isChecked) {
                    tv_i_owe.setTextColor(ContextCompat.getColor(context, R.color.colorDarkText));
                    tv_owe_me.setTextColor(ContextCompat.getColor(context, R.color.colorLightText));
                } else {
                    tv_i_owe.setTextColor(ContextCompat.getColor(context, R.color.colorLightText));
                    tv_owe_me.setTextColor(ContextCompat.getColor(context, R.color.colorDarkText));
                }
            }
        };

        View.OnClickListener onIOweClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckedChangeListener.onCheckedChanged(sw, false);
            }
        };

        View.OnClickListener onOweMeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckedChangeListener.onCheckedChanged(sw, true);
            }
        };

        sw.setOnCheckedChangeListener(onCheckedChangeListener);

        tv_i_owe.setOnClickListener(onIOweClickListener);
        tv_owe_me.setOnClickListener(onOweMeClickListener);

        btn_add.setOnClickListener(onButtonAddClickListener);

        return dialog;
    }
}