package com.example.as.activity.modify_trans_type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.as.Entity.TransType;
import com.example.as.R;
import com.example.as.dao.CommonDAO;

public class TransTypeModify extends FragmentActivity {

    ListView transTypeListview;
    Button newTransType;
    Button goBack;

    TransTypeAdapter arrayAdapter;

    int isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_type_modify);

        SharedPreferences shd = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        isAdmin = shd.getInt("isAdmin", -1);
        assert isAdmin != -1;

        transTypeListview = findViewById(R.id.trans_type_listview);

        CommonDAO<TransType> transTypeDao = new CommonDAO<>();
        ResultSet rs = null;
        try {
            // select
            rs = transTypeDao.find(new TransType(), "ui.name as name, trans_type.type as type, trans_type.in_or_out as in_or_out, trans_type.ID as ID", "join user_info as ui on ui.ID = trans_type.user_id");
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
        Log.i("SQL", transTypeDao.getLastSQLExecuted());
        List<NameType> transTypes = new ArrayList<>();
        try {
            while (rs.next()) {
                NameType ts = new NameType(rs);
                transTypes.add(ts);
            }
            rs.close();
        } catch (SQLException e) {
            Log.e("SQL", Arrays.toString(e.getStackTrace()));
        }
        arrayAdapter = new TransTypeAdapter(getApplicationContext(),
                R.layout.single_trans_type, transTypes);
        transTypeListview.setAdapter(arrayAdapter);// 为ListView列表设置数据源
        transTypeListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.delete_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (isAdmin != 1) {
                            Toast.makeText(getApplicationContext(),
                                    "你不是管理员，无法修改收支类型！", Toast.LENGTH_LONG).show();
                        } else {
                            NameType record = (NameType) parent.getItemAtPosition(position);
                            try {
                                transTypeDao.delete(new TransType(), "where ID=" + record.ID);
                            } catch (SQLException e) {
                                Log.e("SQL", Arrays.toString(e.getStackTrace()));
                                Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });

        newTransType = findViewById(R.id.new_trans_type);
        newTransType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin != 1) {
                    Toast.makeText(getApplicationContext(),
                            "你不是管理员，无法修改收支类型！", Toast.LENGTH_LONG).show();
                } else {
                    NewTransTypeDialog newTransTypeDialog = new NewTransTypeDialog();
                    newTransTypeDialog.show(getSupportFragmentManager(), "newTransTypeDialog");
                }
            }
        });

        goBack = findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}


