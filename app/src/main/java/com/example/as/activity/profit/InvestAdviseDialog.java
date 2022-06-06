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
    TextView maxBonusTextview;
    TextView adviseTextview;
    Button confirmButton;
    double maxBonus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advise_dialog, container, true);

        ROITextview = view.findViewById(R.id.roi_text);
        confirmButton = view.findViewById(R.id.confirm_button);
        adviseTextview = view.findViewById(R.id.advise_textview);
        maxBonus = ((Profit) getActivity()).maxBonus;
        maxBonusTextview = view.findViewById(R.id.max_bonus_text);


        userProfits = ((Profit) getActivity()).getUserProfits();
        assert userProfits != null;

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


        maxBonusTextview.setText(String.valueOf(maxBonus));

        String advise;
        if (ROI > 0.5) {
            advise = "你真牛，投资回报率超过了全国99.9%的韭菜";
        } else if (ROI > 0) {
            advise = "继续努力，投资回报率超过了全国99%的韭菜";
        } else if (maxBonus > 0) {
            advise = "已经很不错了，至少你还有正收益的股票";
        } else {
            advise = "收手吧阿祖，再这么下去迟早药丸";
        }

        adviseTextview.setText(advise);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
