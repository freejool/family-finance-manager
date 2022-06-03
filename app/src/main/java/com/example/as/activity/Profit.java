package com.example.as.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.as.Entity.UserStock;
import com.example.as.R;
import com.example.as.dao.CommonDAO;
import com.example.as.dao.FlagDAO;
import com.example.as.database.AppDatabase;
import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.DatabaseQuery;
import com.example.as.database.Row;
import com.example.as.model.Tb_flag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Profit extends Activity {

    ListView profitInfoListview;
    Button adviseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profit);

        profitInfoListview = findViewById(R.id.profit_info_listview);
        adviseButton = findViewById(R.id.investment_advice_button);

        List<UserProfit> userProfits = new ArrayList<>();
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

    }
}

class UserProfit {
    public Double origin;
    public Double bonus;
    public Integer duration;

    UserProfit(ResultSet rs) throws SQLException {
        this.origin = rs.getDouble("origin");
        this.bonus = rs.getDouble("bonus");
        this.duration = rs.getInt("duration");
    }

}

class ProfitInfoAdapter extends ArrayAdapter<UserProfit> {

    private int resource;

    public ProfitInfoAdapter(@NonNull Context context, int resource, @NonNull List<UserProfit> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout linearView;
        UserProfit userProfit = getItem(position);
        if (convertView == null) {
            linearView = new LinearLayout(getContext());
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(resource, linearView, true);
        } else {
            linearView = (LinearLayout) convertView;
        }
        TextView originTextview = (TextView) linearView.findViewById(R.id.origin_textview);
        TextView bonusTextview = (TextView) linearView.findViewById(R.id.bonus_textview);
        TextView durationTextview = (TextView) linearView.findViewById(R.id.duration_textview);

        originTextview.setText(String.format(Locale.getDefault(),
                "%.2f",userProfit.origin));
        bonusTextview.setText(String.format(Locale.getDefault(),
                "%.2f",userProfit.bonus));
        durationTextview.setText(String.format(Locale.getDefault(),
                "%d",userProfit.duration));

        return linearView;
    }
}
