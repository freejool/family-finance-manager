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
import com.example.as.dao.LoginDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class DialogRegister extends DialogFragment {
    private UserInfo newUser = new UserInfo();

    private EditText username;
    private EditText password;
    private EditText repassword;
    private EditText email;
    private Button button_birthday;
    private TextView view_birthday;
    private RadioGroup gender;
    private Button confirm_button;
    private Button cancel_button;
    private CheckBox is_admin;

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
        button_birthday = view.findViewById(R.id.birthday_button);
        view_birthday = view.findViewById(R.id.birthday_textview);
        gender = view.findViewById(R.id.gender_radio_group);
        confirm_button = view.findViewById(R.id.confirm_button);
        cancel_button = view.findViewById(R.id.cancel_button);
        is_admin = view.findViewById(R.id.is_admin);

        button_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickTimeFragment = new PickTimeFragment();
                pickTimeFragment.show(DialogRegister.this.getParentFragmentManager(), "timePicker");
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
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
                newUser.setName(username.getText().toString());
                newUser.setPassword(password.getText().toString());
                newUser.setEmail(email.getText().toString());
                newUser.setBirthday(birthday);
                RadioButton selected = getView().findViewById(gender.getCheckedRadioButtonId());
                newUser.setSex(selected.getText().toString());
                newUser.setCreate_date(LocalDateTime.now());
                int isadmin = is_admin.isChecked() ? 1 : 0;
                newUser.setIsAdmin(isadmin);

                LoginDAO loginDAO = new LoginDAO();
                try {
                    loginDAO.addUser(newUser);
                    Toast.makeText(getContext(), "注册成功", Toast.LENGTH_LONG).show();
                    dismiss();
                } catch (SQLException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
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
