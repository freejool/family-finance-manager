package com.example.as.activity;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.as.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

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

    private final int resource;

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
        TextView originTextview = linearView.findViewById(R.id.origin_textview);
        TextView bonusTextview = linearView.findViewById(R.id.bonus_textview);
        TextView durationTextview = linearView.findViewById(R.id.duration_textview);

        originTextview.setText(String.format(Locale.getDefault(),
                "%.2f",userProfit.origin));
        bonusTextview.setText(String.format(Locale.getDefault(),
                "%.2f",userProfit.bonus));
        durationTextview.setText(String.format(Locale.getDefault(),
                "%d",userProfit.duration));

        return linearView;
    }
}
