package com.example.as.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.as.R;
import com.example.as.dao.OutaccountDAO;
import com.example.as.model.Tb_outaccount;

public class AddOutcome extends Activity {
    protected static final int DATE_DIALOG_ID = 0;// 创建日期对话框常量
    EditText txtMoney, txtTime, txtAddress, txtMark;// 创建4个EditText对象
    Spinner spType;// 创建Spinner对象
    Button btnSaveButton;// 创建Button对象“保存”
    Button btnCancelButton;// 创建Button对象“取消”

    private int mYear;// 年
    private int mMonth;// 月
    private int mDay;// 日

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_outcome);// 设置布局文件
        txtMoney = findViewById(R.id.txtMoney);// 获取金额文本框
        txtTime = findViewById(R.id.txtTime);// 获取时间文本框
        txtAddress = findViewById(R.id.txtAddress);// 获取地点文本框
        txtMark = findViewById(R.id.txtMark);// 获取备注文本框
        spType = findViewById(R.id.spType);// 获取类别下拉列表
        btnSaveButton = findViewById(R.id.btnSave);// 获取保存按钮
        btnCancelButton = findViewById(R.id.btnCancel);// 获取取消按钮

        txtTime.setOnClickListener(new OnClickListener() {// 为时间文本框设置单击监听事件

            @Override
            public void onClick(View arg0) {
                showDialog(DATE_DIALOG_ID);// 显示日期选择对话框
            }
        });

        btnSaveButton.setOnClickListener(new OnClickListener() {// 为保存按钮设置监听事件

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String strMoney = txtMoney.getText().toString();// 获取金额文本框的值
                if (!strMoney.isEmpty()) {// 判断金额不为空
                    // 创建OutaccountDAO对象
                    OutaccountDAO outaccountDAO = new OutaccountDAO(
                            AddOutcome.this);
                    // 创建Tb_outaccount对象
                    Tb_outaccount tb_outaccount = new Tb_outaccount(
                            outaccountDAO.getMaxId() + 1, Double
                            .parseDouble(strMoney), txtTime
                            .getText().toString(), spType
                            .getSelectedItem().toString(),
                            txtAddress.getText().toString(), txtMark
                            .getText().toString());
                    outaccountDAO.add(tb_outaccount);// 添加支出信息
                    // 弹出信息提示
                    Toast.makeText(AddOutcome.this, "〖新增支出〗数据添加成功！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddOutcome.this, "请输入支出金额！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelButton.setOnClickListener(new OnClickListener() {// 为取消按钮设置监听事件

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        final Calendar c = Calendar.getInstance();// 获取当前系统日期
        mYear = c.get(Calendar.YEAR);// 获取年份
        mMonth = c.get(Calendar.MONTH);// 获取月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数

        updateDisplay();// 显示当前系统时间
    }

    @Override
    protected Dialog onCreateDialog(int id) {// 重写onCreateDialog方法
        switch (id) {
            case DATE_DIALOG_ID:// 弹出日期选择对话框
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
        }
        return null;
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;// 为年份赋值
            mMonth = monthOfYear;// 为月份赋值
            mDay = dayOfMonth;// 为天赋值
            updateDisplay();// 显示设置的日期
        }
    };

    private void updateDisplay() {
        // 显示设置的时间
        txtTime.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }
}
