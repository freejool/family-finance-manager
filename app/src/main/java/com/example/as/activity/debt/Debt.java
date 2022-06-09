package com.example.as.activity.debt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.as.R;
import com.example.as.dao.CommonDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Debt extends FragmentActivity {
    ListView deptListview;
    Button confirmButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dept);
        deptListview = findViewById(R.id.dept_info_listview);
        confirmButton = findViewById(R.id.confirm_button);

        CommonDAO<com.example.as.Entity.Debt> dao = new CommonDAO<>();
        ResultSet rs;
        List<String> debts = new ArrayList<>();
        try {
            rs = dao.find(new com.example.as.Entity.Debt(), "*", "");
            while (rs.next()) {
                com.example.as.Entity.Debt newDebt= new com.example.as.Entity.Debt(rs);
                debts.add(newDebt.toString());
            }

        } catch (SQLException e) {
            Log.e("SQL", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, debts);
        deptListview.setAdapter(arrayAdapter);

        deptListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return false;
            }
        });
    }
}
