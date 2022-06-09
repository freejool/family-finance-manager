package com.example.as.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.as.R;
import com.example.as.dao.CommonDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VSafeUserInfo extends FragmentActivity {
    ListView infoListview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        SharedPreferences shd = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int isAdmin = shd.getInt("isAdmin", -1);
        assert isAdmin != -1;
        if (isAdmin != 1) {
            Toast.makeText(getApplicationContext(), "你不是管理员!", Toast.LENGTH_LONG).show();
            finish();
        }


        infoListview = findViewById(R.id.user_info_listview);

        CommonDAO<com.example.as.Entity.VSafeUserInfo> dao = new CommonDAO<>();
        List<com.example.as.Entity.VSafeUserInfo> safeUserInfos = new ArrayList<>();
        try {
            ResultSet rs;
            rs = dao.find(new com.example.as.Entity.VSafeUserInfo(), "*", "");
            while (rs.next()) {
                safeUserInfos.add(new com.example.as.Entity.VSafeUserInfo(rs));
            }
        } catch (SQLException e) {
            Log.e("SQL", e.toString());
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
        List<String> strInfos = new ArrayList<>();
        for (com.example.as.Entity.VSafeUserInfo info : safeUserInfos) {
            strInfos.add(info.toString());
        }
        ArrayAdapter<String> infoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strInfos);
        infoListview.setAdapter(infoAdapter);
    }
}
