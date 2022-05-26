package com.example.as.activity;


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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogAddIncome extends DialogFragment {
    private final Transactions newTransaction = new Transactions();

    private EditText money;
    private EditText userId;
    private Spinner spinType;
    private EditText note;
    private Button saveButton;
    private Button cancelButton;

    List<String> typeList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.addincome_dialog, container, true);
        money = view.findViewById(R.id.income_money_edittext);
        userId = view.findViewById(R.id.income_user_id_edittext);
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
                (this.getContext(),android.R.layout.simple_list_item_1, typeList);
        spinType.setAdapter(typeAdapter);



//        saveButton.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                if(money.getText().toString().equals("")
//                        || userId.getText().toString().equals("")){
//                    Toast.makeText(getContext(),
//                            "用户ID和金额是必填项！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                newTransaction.setUserId(Integer.parseInt(userId.getText().toString()));
//                newTransaction.setType(spinType.getSelectedItem().toString());
//                newTransaction.setInOrOut("收入");
//                newTransaction.setAmount(Integer.parseInt(money.getText().toString()));
//                newTransaction.setTransactionTime(LocalDateTime.now());
//                newTransaction.setNote(note.getText().toString());
//                try {
//                    addincomeDAO.addIncome(newTransaction);
//                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
//                    dismiss();
//                } catch (SQLException e) {
//                    Toast.makeText(getContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
//                }
//            }
//        } );

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}
