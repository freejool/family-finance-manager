package com.example.as.activity.login;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.as.Entity.UserInfo;
import com.example.as.R;
import com.example.as.dao.CommonDAO;
import com.example.as.dao.LoginDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class DialogRegister extends DialogFragment {
    private final UserInfo newUser = new UserInfo();

    private EditText username;
    private EditText password;
    private EditText repassword;
    private EditText email;
    private Button birthdayButton;
    private TextView birthdayView;
    private RadioGroup gender;
    private Button confirmButton;
    private Button cancelButton;
    private CheckBox isAdminCheckbox;

    private DatePickerDialog datePicker;


    private LocalDateTime birthday;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_dialog, container, true);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.passwd);
        repassword = view.findViewById(R.id.confirm_passwd);
        email = view.findViewById(R.id.email);
        birthdayButton = view.findViewById(R.id.birthday_button);
        birthdayView = view.findViewById(R.id.birthday_textview);
        gender = view.findViewById(R.id.gender_radio_group);
        confirmButton = view.findViewById(R.id.confirm_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        isAdminCheckbox = view.findViewById(R.id.is_admin);

        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickTimeFragment = new PickTimeFragment();
                pickTimeFragment.show(DialogRegister.this.getParentFragmentManager(), "timePicker");
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")
                        || password.getText().toString().equals("")
                        || repassword.getText().toString().equals("")) {
                    Toast.makeText(getContext(),
                            "用户名和密码是必填项！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.getText().toString().equals(
                        repassword.getText().toString())
                ) {
                    Toast.makeText(getContext(),
                            "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                newUser.name.value = username.getText().toString();
                newUser.password.value = password.getText().toString();
                newUser.email.value = email.getText().toString();
                newUser.birthday.value = birthday;
                RadioButton selected = getView().findViewById(gender.getCheckedRadioButtonId());
                newUser.sex.value = selected.getText().toString();
                newUser.create_date.value = LocalDateTime.now();
                newUser.isAdmin.value = isAdminCheckbox.isChecked() ? 1 : 0;

                CommonDAO<UserInfo> dao = new CommonDAO<>();
                try {
                    dao.insert(newUser, false, newUser.ID);
                    Toast.makeText(getContext(), "注册成功", Toast.LENGTH_LONG).show();
                    dismiss();
                } catch (SQLException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }
}
