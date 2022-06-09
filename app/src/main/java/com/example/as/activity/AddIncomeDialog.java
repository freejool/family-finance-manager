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

import com.example.as.Entity.TransType;
import com.example.as.Entity.Transactions;
import com.example.as.R;
import com.example.as.dao.AddIncomeDAO;
import com.example.as.dao.CommonDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddIncomeDialog extends DialogFragment {

    private View back;
    private EditText money;
    private Button trans_in_switch;
    private Button trans_out_switch;
    private Spinner spinType;
    private EditText note;
    private Button saveButton;
    private Button cancelButton;

    private boolean is_in=false;

    CommonDAO<Transactions> transactionsDAO;
    CommonDAO<TransType> typeDAO;

    List<String> typeList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_income_dialog, container, true);
        back =view.findViewById(R.id.initem);
        money = view.findViewById(R.id.income_money_edittext);
        trans_in_switch=view.findViewById(R.id.trans_in_switch);
        trans_out_switch=view.findViewById(R.id.trans_out_switch);
        note = view.findViewById(R.id.income_note_edittext);
        spinType = view.findViewById(R.id.income_type_spinner);
        saveButton = view.findViewById(R.id.income_save_button);
        cancelButton = view.findViewById(R.id.income_cancel_button);

        trans_in_switch.setOnClickListener(this::OnTranInSelected);
        trans_out_switch.setOnClickListener(this::OnTranOutSelected);

        SharedPreferences shd = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int userId = shd.getInt("id", -1);
        assert userId != -1;

        transactionsDAO = new CommonDAO<>();

        saveButton.setOnClickListener(v -> {
            if (money.getText().toString().equals("")) {
                Toast.makeText(getContext(),
                        "金额是必填项！", Toast.LENGTH_SHORT).show();
                return;
            }
            Transactions newTransaction = new Transactions();
            newTransaction.user_id.value = userId;
            newTransaction.type.value = spinType.getSelectedItem().toString();
            newTransaction.in_or_out.value = is_in?"收入":"支出";
            newTransaction.amount.value = Double.valueOf(money.getText().toString());
            newTransaction.Transaction_time.value = LocalDateTime.now();
            newTransaction.note.value = note.getText().toString();
            try {
                transactionsDAO.insert(newTransaction, false, newTransaction.ID);
                Log.i("SQL", transactionsDAO.getLastSQLExecuted());
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
                dismiss();
            } catch (SQLException e) {
                Toast.makeText(getContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
            } finally {
                transactionsDAO.close();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        OnTranInSelected(null);
        return view;
    }

    private void OnTranInSelected(View v)
    {
        if(is_in)return;
        back.setBackgroundColor(getContext().getColor(R.color.transaction_in_bk));
        is_in=true;

        typeDAO = new CommonDAO<>();
        try {
            ResultSet rs;
            rs = typeDAO.find(new TransType(), "type", "where in_or_out='收入'");
            typeList = new ArrayList<>();
            while (rs.next()) {
                typeList.add(rs.getString(1));
            }
        } catch (SQLException e) {
            Log.e("SQL error", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        } finally {
            typeDAO.close();
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.simple_list_item_1, typeList);
        spinType.setAdapter(typeAdapter);
    }
    private void OnTranOutSelected(View v)
    {
        if(!is_in)return;
        back.setBackgroundColor(getContext().getColor(R.color.transaction_out_bk));
        is_in=false;

        typeDAO = new CommonDAO<>();
        try {
            ResultSet rs;
            rs = typeDAO.find(new TransType(), "type", "where in_or_out='支出'");
            typeList = new ArrayList<>();
            while (rs.next()) {
                typeList.add(rs.getString(1));
            }
        } catch (SQLException e) {
            Log.e("SQL error", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        } finally {
            typeDAO.close();
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.simple_list_item_1, typeList);
        spinType.setAdapter(typeAdapter);
    }
}
