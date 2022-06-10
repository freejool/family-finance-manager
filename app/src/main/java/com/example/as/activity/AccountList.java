package com.example.as.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.as.Entity.Account;
import com.example.as.R;
import com.example.as.dao.CommonDAO;
import com.example.as.databinding.AccountListBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class AccountList extends Activity
{
    AccountListBinding binding;
    int user_id=-1;
    Account account;

    static final String ac_condition="where user_id=%d";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding= AccountListBinding.inflate(getLayoutInflater());
        View root=binding.getRoot();
        setContentView(root);
        SharedPreferences shd=getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user_id=shd.getInt("id",-1);

        Vector<String> account_list=new Vector<>();

        account=new Account();
        ResultSet ac_rs;
        CommonDAO<Account> ac_dao=new CommonDAO<>();
        try {
            ac_rs=ac_dao.find(account,"*",String.format(ac_condition,user_id));

            while (ac_rs.next())
            {
                account.setByResultSet(ac_rs);
                account_list.add(account.toString());
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ac_dao.close();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,account_list);
        binding.accountListview.setAdapter(adapter);
    }
}
