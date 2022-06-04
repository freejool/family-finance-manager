package com.example.as.activity.profit;

import android.app.Activity;
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

    ListView profitInfoListview;
    Button adviseButton;

    public List<UserProfit> getUserProfits() {
        return userProfits;
    }

    List<UserProfit> userProfits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profit);

        profitInfoListview = findViewById(R.id.profit_info_listview);
        adviseButton = findViewById(R.id.investment_advice_button);

        userProfits = new ArrayList<>();
        ResultSet rs = null;
        SharedPreferences shd = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int user_id = shd.getInt("id", -1);
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

        adviseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvestAdviseDialog investAdviseDialog = new InvestAdviseDialog();
                investAdviseDialog.show(Profit.this.getSupportFragmentManager(), "adviceDialog");
            }
        });
    }
}
