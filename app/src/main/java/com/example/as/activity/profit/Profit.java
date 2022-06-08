package com.example.as.activity.profit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.as.R;
import com.example.as.database.DatabaseQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profit extends FragmentActivity {

    int isAdmin;

    ListView profitInfoListview;
    Button familyBonusButton;
    Button adviseButton;

    public List<FamilyBonus> familyBonuses;

    public List<UserProfit> getUserProfits() {
        return userProfits;
    }

    List<UserProfit> userProfits;
    public double maxBonus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profit);

        init();

        SharedPreferences shd = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        isAdmin = shd.getInt("isAdmin", -1);
        assert isAdmin != -1;

        profitInfoListview = findViewById(R.id.profit_info_listview);
        familyBonusButton=findViewById(R.id.family_stats);
        adviseButton = findViewById(R.id.investment_advice_button);

        userProfits = new ArrayList<>();
        ResultSet rs = null;
        int user_id = shd.getInt("id", -1);
        assert user_id != -1;

        DatabaseQuery db;
        try {
            String sql = "select us.price * us.amount as origin,\n" +
                    "       us.amount * (us.price - etp.price)   as bonus,\n" +
                    "       datediff(day, us.in_time, getdate()) as duration\n" +
                    "from (select price, stock_id\n" +
                    "      from stock_hist_price as shp\n" +
                    "      where exists(select *\n" +
                    "                   from (select max(time) as end_time, stock_id\n" +
                    "                         from stock_hist_price\n" +
                    "                         where stock_id in (select stock_id\n" +
                    "                                            from user_stock\n" +
                    "                                            where user_id = " + user_id + ")\n" +
                    "                         group by stock_id) as mt\n" +
                    "                   where mt.end_time = shp.time\n" +
                    "                     and mt.stock_id = shp.stock_id)) as etp\n" +
                    "         join user_stock as us on etp.stock_id = us.stock_id\n" +
                    "         join user_info ui on ui.ID = us.user_id";
            db = new DatabaseQuery(sql);
            db.start();
            db.join();
            rs = db.getResultSet();
            while (rs.next()) {
                userProfits.add(new UserProfit(rs));
            }
        } catch (SQLException e) {
            Log.e("SQL", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Log.e("Thread", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
        ProfitInfoAdapter profitInfoAdapter = new ProfitInfoAdapter(getApplicationContext(), R.layout.single_user_profit, userProfits);
        profitInfoListview.setAdapter(profitInfoAdapter);

        maxBonus = -1;
        try {
            String sql = " declare @user_id int = " + user_id + "\n" +
                    "    declare @bonus float\n" +
                    "    declare @name varchar(255)\n" +
                    "    declare @result int\n" +
                    "    exec\n" +
                    "        @result = p_max_stock_bonus_of_user\n" +
                    "                  @user_id,\n" +
                    "                  @bonus output,\n" +
                    "                  @name output\n" +
                    "    select @bonus as bonus";
            db = new DatabaseQuery(sql);
            db.start();
            db.join();
            rs = db.getResultSet();
            while (rs.next()) {
                maxBonus = rs.getDouble("bonus");
            }

        } catch (SQLException e) {
            Log.e("SQL", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Log.e("Thread", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
        assert maxBonus != -1;

        familyBonuses = new ArrayList<>();
        try {
            String sql = "select * from family_bonus order by bonus desc";
            db = new DatabaseQuery(sql);
            db.start();
            db.join();
            rs = db.getResultSet();
            while (rs.next()) {
                familyBonuses.add(new FamilyBonus(rs));
            }

        } catch (SQLException e) {
            Log.e("SQL", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Log.e("Thread", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
        assert !familyBonuses.isEmpty();

        familyBonusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin != 1) {
                    Toast.makeText(getApplicationContext(), "只有管理员才能查看家庭投资信息", Toast.LENGTH_LONG).show();
                } else {
                    FamilyBonusDialog familyBonusDialog = new FamilyBonusDialog();
                    familyBonusDialog.show(Profit.this.getSupportFragmentManager(), "familyBonusDialog");
                }
            }
        });

        adviseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvestAdviseDialog investAdviseDialog = new InvestAdviseDialog();
                investAdviseDialog.show(Profit.this.getSupportFragmentManager(), "adviceDialog");
            }
        });
    }
    private void init(){
        DatabaseQuery db;
        ResultSet rs;
        try {
            String sql = "exec p_gen_v_max_invest_bonus_of_all_users";
            db = new DatabaseQuery(sql);
            db.start();
            db.join();
//        } catch (SQLException e) {
//            Log.e("SQL", Arrays.toString(e.getStackTrace()));
//            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Log.e("Thread", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
    }
}
