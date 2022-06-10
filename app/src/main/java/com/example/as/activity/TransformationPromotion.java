package com.example.as.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.as.Entity.Account;
import com.example.as.Entity.UserInfo;
import com.example.as.R;
import com.example.as.dao.CommonDAO;
import com.example.as.database.DatabaseQuery;
import com.example.as.databinding.TformPromotionBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class TransformationPromotion extends Activity
{
    TformPromotionBinding binding;
    int user_id=-1;
    Account account;
    boolean is_in=true;
    static final String ac_condition="where user_id=%d and type='默认'";
    static final String promotion="exec p_promote_one_transformation %d,%d,'%s',%f";
    static final String invalid_number_alert="金额不得小于等于0";
    static final String not_enough_blance_alert="账户余额不足:%.2f";
    static final String in_tform_hint=" %s 同意后将会转给您 %.2f币";
    static final String out_tform_hint=" %s 同意后将得到来自您的 %.2f币";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding=TformPromotionBinding.inflate(getLayoutInflater());
        View root=binding.getRoot();
        setContentView(root);

        SharedPreferences shd=getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user_id=shd.getInt("id",-1);
        account=new Account();
        ResultSet ac_rs;
        CommonDAO<Account> ac_dao=new CommonDAO<>();
        try {
            ac_rs=ac_dao.find(account,"*",String.format(ac_condition,user_id));
            ac_rs.next();
            account.setByResultSet(ac_rs);
            Log.i("Account","默认账户是"+account.id);
            Toast.makeText(getApplicationContext(), "默认账户是"+account.id, Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ac_dao.close();

        Vector<NameIDPair> usr_name_list=new Vector<>();
        UserInfo userInfo=new UserInfo();
        ResultSet ui_rs;
        CommonDAO<UserInfo> ui_dao=new CommonDAO<>();
        try {
            ui_rs=ui_dao.find(userInfo,"*","");
            while(ui_rs.next())
            {
                userInfo.setByResultSet(ui_rs);
                if(userInfo.ID.value==user_id) continue;
                usr_name_list.add(new NameIDPair(userInfo.name.value,userInfo.ID.value));
            }
            ui_dao.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<NameIDPair> adapter=new ArrayAdapter<NameIDPair>(this,
                android.R.layout.simple_list_item_1, usr_name_list);
        binding.destUserSpinner.setAdapter(adapter);

        binding.tformOutSwitch.setOnClickListener(v->
        {
            is_in=false;
            binding.back.setBackgroundColor(getColor(R.color.transaction_out_dark));
            genNotice();
        });
        is_in=false;
        binding.back.setBackgroundColor(getColor(R.color.transaction_out_dark));
        binding.tformInSwitch.setOnClickListener(v->
        {
            is_in=true;
            binding.back.setBackgroundColor(getColor(R.color.transaction_in_dark));
            genNotice();
        });

        binding.destUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genNotice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genNotice();
            }
        });

        binding.moneyEdittext.setOnFocusChangeListener((v,f)->{if(!f)genNotice();});

        binding.back.setOnClickListener(v->
        {
            binding.back.clearChildFocus(binding.moneyEdittext);
        });

        binding.cancelButton.setOnClickListener(v->finish());
        binding.confirmButton.setOnClickListener(this::promote);
    }

    public void promote(View v)
    {
        int receiver_id=((NameIDPair)binding.destUserSpinner.getSelectedItem()).second;
        double money=-1;
        if(!binding.moneyEdittext.getText().toString().isEmpty())
            money=Double.parseDouble(binding.moneyEdittext.getText().toString());
        if(money<=0)
        {
            Toast.makeText(getApplicationContext(), invalid_number_alert, Toast.LENGTH_SHORT).show();
            return;
        }

        if(account.balance.value<=money&&!is_in)
        {
            Toast.makeText(getApplicationContext(), String.format(not_enough_blance_alert,account.balance.value), Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseQuery db=new DatabaseQuery(String.format(promotion,account.id.value,receiver_id,is_in?"收入":"支出",money));
        db.run();
        if(db.getException()!=null)
        {
            Toast.makeText(this,db.getException().toString(),Toast.LENGTH_SHORT).show();
        }
        db.close();
        Toast.makeText(this,"发起转账成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void genNotice()
    {
        String receiver_name=((NameIDPair)binding.destUserSpinner.getSelectedItem()).first;
        double money=-1;
        if(!binding.moneyEdittext.getText().toString().isEmpty())
            money=Double.parseDouble(binding.moneyEdittext.getText().toString());
        if(is_in)
        {
            binding.tformDescription.setText(String.format(in_tform_hint,receiver_name,money));
        }
        else
        {
            binding.tformDescription.setText(String.format(out_tform_hint,receiver_name,money));
        }

        if(money<=0)
        {
            binding.tformDescription.setVisibility(View.GONE);
            binding.tformAlert.setVisibility(View.VISIBLE);
            binding.tformAlert.setText(invalid_number_alert);
        }
        else if (account.balance.value<money&&!is_in)
        {
            binding.tformDescription.setVisibility(View.GONE);
            binding.tformAlert.setVisibility(View.VISIBLE);
            binding.tformAlert.setText(String.format(not_enough_blance_alert,account.balance.value));
        }
        else
        {
            binding.tformDescription.setVisibility(View.VISIBLE);
            binding.tformAlert.setVisibility(View.GONE);
        }
    }

    class NameIDPair extends Pair<String,Integer>
    {

        /**
         * Constructor for a Pair.
         *
         * @param first  the first object in the Pair
         * @param second the second object in the pair
         */
        public NameIDPair(String first, Integer second) {
            super(first, second);
        }

        @Override
        public String toString()
        {
            return first.toString();
        }
    }
}
