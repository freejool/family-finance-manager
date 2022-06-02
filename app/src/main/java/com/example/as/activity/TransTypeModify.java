package com.example.as.activity;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.as.Entity.TransType;
import com.example.as.Entity.Transactions;
import com.example.as.R;
import com.example.as.dao.CommonDAO;
import com.example.as.dao.OutaccountDAO;
import com.example.as.model.Tb_outaccount;

public class TransTypeModify extends Activity {

    ListView transTypeListview;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_type_modify);
        transTypeListview.findViewById(R.id.trans_type_listview);

        CommonDAO<TransType> transTypeDao = new CommonDAO<>();
        ResultSet rs = null;
        try {
            rs = transTypeDao.find(new TransType(), "", "");
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
        Log.i("SQL", transTypeDao.getLastSQLExecuted());
        List<String> transTypes = null;
        try {
            rs.next();
            while (!rs.next()) {
                TransType ts = new TransType();
                ts.setByResultSet(rs);
                transTypes.add(ts.toString());
            }
            rs.close();
        } catch (SQLException e) {
            Log.e("SQL", Arrays.toString(e.getStackTrace()));
        }
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, transTypes);
        transTypeListview.setAdapter(arrayAdapter);// 为ListView列表设置数据源

    }

//    private class TranstypeAdapter extends ArrayAdapter<TransType> {
//        List<TransType> transTypes;
//        Context context;
//        int resourceId;
//
//        TranstypeAdapter(Context context, int resourceId, List<TransType> transTypes) {
//            super(context, resourceId, transTypes);
//            this.transTypes=transTypes;
//            this.context=context;
//            this.resourceId=resourceId;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LinearLayout linearView;
//            TransType transType = getItem(position);
//            if (convertView == null) {
//                linearView = new LinearLayout(getContext());
//                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                layoutInflater.inflate(resourceId, linearView, true);
//            } else {
//                linearView = (LinearLayout) convertView;
//            }
//            TextView content = (TextView) linearView.findViewById(R.id.textView_content);
//            TextView days_left = (TextView) linearView.findViewById(R.id.dayleft);
//            TextView days = (TextView) linearView.findViewById(R.id.textView_day);
//
//
//            LocalDateTime target = LocalDateTime.parse(record.endTime);
//            content.setText(record.content);
//            Long minsLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), target);
//            days.setText(String.valueOf(minsLeft));
//            days_left.setText(R.string.mins_left);
//
//
//
//            return linearView;
//        }
//    }


}
