package com.example.as.activity.modify_trans_type;

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
import com.example.as.R;
import com.example.as.dao.CommonDAO;

import java.sql.SQLException;
import java.util.Arrays;

public class NewTransTypeDialog extends DialogFragment {
    EditText typeEdittext;
    Spinner inOrOutSpinner;
    Button confirmButton;
    Button cancelButton;
    int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_trans_type_dialog, container, true);

        typeEdittext = view.findViewById(R.id.type_edittext);
        inOrOutSpinner = view.findViewById(R.id.in_or_out_spinner);
        inOrOutSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                new String[]{"收入", "支出"}));
        cancelButton = view.findViewById(R.id.cancel_button);
        confirmButton = view.findViewById(R.id.confirm_button);

        SharedPreferences shd = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        userID = shd.getInt("id", -1);
        assert userID != -1;


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeEdittext.getText().toString();
                String in_or_out = inOrOutSpinner.getSelectedItem().toString();
                CommonDAO<TransType> dao = new CommonDAO<>();
                TransType newTransType = new TransType();
                newTransType.type.value = type;
                newTransType.in_or_out.value = in_or_out;
                newTransType.user_id.value = userID;
                try {
                    dao.insert(newTransType, false, newTransType.ID);
                    Toast.makeText(getContext(),
                            "添加成功", Toast.LENGTH_LONG).show();
                    dismiss();
                } catch (SQLException e) {
                    Log.e("SQL", Arrays.toString(e.getStackTrace()));
                    Toast.makeText(getContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
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
