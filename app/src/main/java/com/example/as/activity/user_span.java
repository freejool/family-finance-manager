package com.example.as.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.as.database.DatabaseQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class user_span extends Activity {
//    private final Transactions newTransaction = new Transactions();

    private EditText username;
    private EditText starttime;
    private Spinner in_or_out;
    private EditText overtime;
    private Button saveButton;
    private Button cancelButton;

//    List<String> typeList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_span);// 设置布局文件
        username = findViewById(R.id.username_edittext);
        starttime = findViewById(R.id.start_time);
        overtime = findViewById(R.id.over_time);
        in_or_out = findViewById(R.id.in_or_out_spinner);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

//


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (username.getText().toString().equals("")
                        || starttime.getText().toString().equals("")
                        || overtime.getText().toString().equals("")) {
                    Toast.makeText(user_span.this,
                            "用户名以及起始终止时间是必填项！", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseQuery db;
                SharedPreferences shd = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                int user_id = shd.getInt("id", -1);

                try {
                    String sql = "exec p_income_stats_between_time " +
                            "@user_id= " + user_id + ", " +
                            "@timemin='" + starttime.getText().toString() + "', " +
                            "@timemax='" + overtime.getText().toString() + "' ";
                    db = new DatabaseQuery(sql);
                    db.start();
                    db.join();
                    if (db.getException() != null) {
                        Toast.makeText(getApplicationContext(), db.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
                }
                String warning = "";
                try {
                    String sql = "select top 1 warn from transaction_warn order by ID desc";
                    db = new DatabaseQuery(sql);
                    db.start();
                    db.join();
                    if (db.getException() != null) {
                        Toast.makeText(getApplicationContext(), db.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                    ResultSet rs = db.getResultSet();
                    while (rs.next()) {
                        warning = rs.getString(1);
                    }

                } catch (InterruptedException e) {
                    Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    Log.e("SQL", e.toString());
                }
                Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(user_span.this, TotalChartspan.class);
                intent.putExtra("username", username.getText().toString());
                intent.putExtra("starttime", starttime.getText().toString());
                intent.putExtra("overtime", overtime.getText().toString());
                intent.putExtra("in_or_out", in_or_out.getSelectedItem().toString());
                startActivity(intent);

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return;
    }
}
