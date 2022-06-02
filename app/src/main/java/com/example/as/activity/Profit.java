package com.example.as.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.as.R;
import com.example.as.dao.FlagDAO;
import com.example.as.model.Tb_flag;

public class Profit extends Activity {
    EditText txtFlag;// 创建EditText组件对象
    Button btnflagSaveButton;// 创建Button组件对象
    Button btnflagCancelButton;// 创建Button组件对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountflag);

        txtFlag = findViewById(R.id.txtFlag);// 获取便签文本框
        btnflagSaveButton = findViewById(R.id.btnflagSave);// 获取保存按钮
        btnflagCancelButton = findViewById(R.id.btnflagCancel);// 获取取消按钮
        btnflagSaveButton.setOnClickListener(new OnClickListener() {// 为保存按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String strFlag = txtFlag.getText().toString();// 获取便签文本框的值
                if (!strFlag.isEmpty()) {// 判断获取的值不为空
                    FlagDAO flagDAO = new FlagDAO(Profit.this);// 创建FlagDAO对象
                    Tb_flag tb_flag = new Tb_flag(
                            flagDAO.getMaxId() + 1, strFlag);// 创建Tb_flag对象
                    flagDAO.add(tb_flag);// 添加便签信息
                    // 弹出信息提示
                    Toast.makeText(Profit.this, "〖新增便签〗数据添加成功！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Profit.this, "请输入便签！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnflagCancelButton.setOnClickListener(new OnClickListener() {// 为取消按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                txtFlag.setText("");// 清空便签文本框
            }
        });
    }
}
