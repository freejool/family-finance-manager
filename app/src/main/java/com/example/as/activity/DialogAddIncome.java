package com.example.as.activity;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.as.Entity.Transactions;
import com.example.as.Entity.UserInfo;
import com.example.as.R;
import com.example.as.dao.AddIncomeDAO;
import com.example.as.dao.LoginDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DialogAddIncome extends DialogFragment {
    private Transactions newtransaction = new Transactions();

    private EditText money;
    private EditText user_id;
    private Spinner spintype;
    private EditText note;
    private Button save_button;
    private Button cancel_button;

    private String in_or_out;
    private LocalDateTime Transcation_time;
    List<String> typelist = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.addincome_dialog, container, true);
        money = view.findViewById(R.id.txtInMoney);
        user_id = view.findViewById(R.id.txtInHandler);
        note = view.findViewById(R.id.txtInMark);
        spintype = view.findViewById(R.id.spInType);
        save_button = view.findViewById(R.id.btnInSave);
        cancel_button = view.findViewById(R.id.btnInCancel);

        AddIncomeDAO addincomeDAO = new AddIncomeDAO();
        try {
            typelist = addincomeDAO.findtype();
            Log.i("fdjfd", String.valueOf(typelist));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (this.getContext(),android.R.layout.simple_list_item_1,typelist);
        spintype.setAdapter(typeAdapter);



        save_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(money.getText().toString().equals("")
                        ||user_id.getText().toString().equals("")){
                    Toast.makeText(getContext(),
                            "用户名和金额是必填项！", Toast.LENGTH_SHORT).show();
                    return;
                }
                newtransaction.setUserId(Integer.parseInt(user_id.getText().toString()));
                newtransaction.setType(spintype.getSelectedItem().toString());
                newtransaction.setInOrOut("收入");
                newtransaction.setAmount(Integer.parseInt(money.getText().toString()));
                newtransaction.setTransactionTime(LocalDateTime.now());
                newtransaction.setNote(note.getText().toString());
                try {
                    addincomeDAO.addincome(newtransaction);
                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
                    dismiss();
                } catch (SQLException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        } );

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}
