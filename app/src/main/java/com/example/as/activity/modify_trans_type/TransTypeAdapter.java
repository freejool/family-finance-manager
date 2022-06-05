package com.example.as.activity.modify_trans_type;

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

import java.util.List;

class TransTypeAdapter extends ArrayAdapter<NameType> {

    private int resource;

    public TransTypeAdapter(@NonNull Context context, int resource, @NonNull List<NameType> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout linearView;
        NameType record = getItem(position);
        if (convertView == null) {
            linearView = new LinearLayout(getContext());
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(resource, linearView, true);
        } else {
            linearView = (LinearLayout) convertView;
        }
        TextView content = (TextView) linearView.findViewById(R.id.text);
        content.setText(record.toString());
        return linearView;
    }
}
