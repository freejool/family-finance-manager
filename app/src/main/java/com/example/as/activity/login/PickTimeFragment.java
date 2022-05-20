package com.example.as.activity.login;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.as.Entity.UserInfo;
import com.example.as.R;

import java.time.LocalDateTime;
import java.util.Calendar;

public class PickTimeFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    TextView birthday;
    UserInfo user;
    DialogRegister dialogRegister;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getParentFragmentManager();
        dialogRegister = (DialogRegister) fragmentManager.findFragmentByTag("register");
        birthday = dialogRegister.getView().findViewById(R.id.birthday_textview);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        dialogRegister.setBirthday(LocalDateTime.of(year, month, day, 0, 0));
        birthday.setText(year + "年" + month + "月" + day + "日");

    }
}
