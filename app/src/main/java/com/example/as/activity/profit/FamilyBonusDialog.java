package com.example.as.activity.profit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.as.R;

import java.util.ArrayList;
import java.util.List;

public class FamilyBonusDialog extends DialogFragment {
    ListView bonuses;
    Button goBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.family_bonus_dialog, container, true);
        bonuses = view.findViewById(R.id.bonus_listview);
        List<FamilyBonus> familyBonuses = ((Profit) getActivity()).familyBonuses;
        List<String> strFamilyBonuses = new ArrayList<>();
        int idx = 1;
        for (FamilyBonus f : familyBonuses) {
            strFamilyBonuses.add(idx + "  " + f.toString());
            idx += 1;
        }

        ArrayAdapter<String> familyBonusAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strFamilyBonuses);
        bonuses.setAdapter(familyBonusAdapter);

        goBack = view.findViewById(R.id.go_back);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;

    }
}
