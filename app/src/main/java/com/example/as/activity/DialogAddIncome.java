package com.example.as.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.as.Entity.Transactions;
import com.example.as.R;
import com.example.as.dao.AddIncomeDAO;
import com.example.as.dao.CommonDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogAddIncome extends DialogFragment {
    private final Transactions transcToCommit = new Transactions();

    private EditText money;
    private Spinner spinnerIO;
    private Spinner spinType;
    private EditText note;
    private Button saveButton;
    private Button cancelButton;

    List<String> typeList = new ArrayList<>();
    static final String[] ioTypeList =new String[]{"收入","支出"};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addincome_dialog, container, true);
        money = view.findViewById(R.id.income_money_edittext);
        spinnerIO = view.findViewById(R.id.income_in_or_out_spinner);
        note = view.findViewById(R.id.income_note_edittext);
        spinType = view.findViewById(R.id.income_type_spinner);
        saveButton = view.findViewById(R.id.income_save_button);
        cancelButton = view.findViewById(R.id.income_cancel_button);

        AddIncomeDAO addincomeDAO = new AddIncomeDAO();
        try {
            typeList = addincomeDAO.findType();
        } catch (SQLException e) {
            Log.e("SQL error", Arrays.toString(e.getStackTrace()));
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.simple_list_item_1, typeList);
        spinType.setAdapter(typeAdapter);
        ArrayAdapter<String> ioAdapter=new ArrayAdapter<>
                (this.getContext(),android.R.layout.simple_list_item_1, ioTypeList);
        spinnerIO.setAdapter(ioAdapter);


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (money.getText().toString().equals("")) {
                    Toast.makeText(getContext(),
                            "金额是必填项！", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences shd=getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                int user_id=shd.getInt("id",-1);
                transcToCommit.user_id.value = user_id;
                transcToCommit.type.value = spinType.getSelectedItem().toString();
                transcToCommit.in_or_out.value = spinnerIO.getSelectedItem().toString();
                transcToCommit.amount.value = Float.valueOf(money.getText().toString());
                transcToCommit.Transaction_time.value = (LocalDateTime.now());
                transcToCommit.note.value = note.getText().toString();
                Log.i("SQL",transcToCommit.toString());
                CommonDAO<Transactions> dao=new CommonDAO<>();
                try {
                    dao.insert(transcToCommit,false,transcToCommit.ID);
                    Log.i("SQL",dao.getLastSQLExecuted());
                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
                    dao.close();
                    dismiss();
                } catch (SQLException e) {
                    Toast.makeText(getContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
                    dao.close();
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

}
