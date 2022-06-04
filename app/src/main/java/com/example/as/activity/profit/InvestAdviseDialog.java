package com.example.as.activity.profit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.as.Entity.UserInfo;
import com.example.as.R;
import com.example.as.activity.login.PickTimeFragment;
import com.example.as.dao.CommonDAO;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InvestAdviseDialog extends DialogFragment {

    List<UserProfit> userProfits;
    TextView ROITextview;
    Button confirmButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advise_dialog, container, true);

        ROITextview = view.findViewById(R.id.roi_text);
        confirmButton = view.findViewById(R.id.confirm_button);

        userProfits = ((Profit) getActivity()).getUserProfits();

        //投资回报率 总年利润/投资总额 不足一年的投资按比例扩大计算年利润
        double ROI = 0;
        double bonusTotal = 0;
        double originTotal = 0;
        for (UserProfit u : userProfits) {
            bonusTotal += u.bonus * (360.0 / u.duration);
            originTotal += u.origin;
        }
        ROI = bonusTotal / originTotal;
        ROITextview.setText(String.format(Locale.getDefault(), "%.2f", ROI));


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
