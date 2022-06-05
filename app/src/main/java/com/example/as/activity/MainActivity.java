package com.example.as.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

import com.example.as.R;
import com.example.as.activity.modify_trans_type.TransTypeModify;
import com.example.as.activity.profit.Profit;


public class MainActivity extends FragmentActivity {
    ListView gvInfo;// 创建GridView对象
    // 定义字符串数组，存储系统功能
    String[] titles = new String[]{"新增支出", "新增收入", "我的支出", "我的收入",
            "收支分析", "系统设置", "投资收益", "帮助", "退出", "收支类型"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);// 设置布局文件

        gvInfo = findViewById(R.id.gv_info);// 获取布局文件中的gvInfo组件
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);// 创建pictureAdapter对象
        gvInfo.setAdapter(adapter);// 为GridView设置数据源
        gvInfo.setOnItemClickListener(new OnItemClickListener() {// 为GridView设置项单击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;// 创建Intent对象
                switch (position) {
                    case 0:
                        DialogAddPay dialogaddpay = new DialogAddPay();
                        dialogaddpay.show(MainActivity.this.getSupportFragmentManager(), "register");
                        break;
                    case 1:
                        DialogAddIncome dialogaddincome = new DialogAddIncome();
                        dialogaddincome.show(MainActivity.this.getSupportFragmentManager(), "register");
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, Outaccountinfo.class);// 使用Outaccountinfo窗口初始化Intent
                        startActivity(intent);// 打开Outaccountinfo
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, InAccountInfo.class);// 使用Inaccountinfo窗口初始化Intent
                        startActivity(intent);// 打开Inaccountinfo
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, StatisticsTransactions.class);// 使用Showinfo窗口初始化Intent
                        startActivity(intent);// 打开Showinfo
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, Sysset.class);// 使用Sysset窗口初始化Intent
                        startActivity(intent);// 打开Sysset
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, Profit.class);// 使用Profit窗口初始化Intent
                        startActivity(intent);// 打开Profit
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this, TransTypeModify.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(MainActivity.this, Help.class);// 使用Help窗口初始化Intent
                        startActivity(intent);// 打开Help
                        break;
                    case 9:
                        finish();// 关闭当前Activity
                        break;
                }
            }
        });
    }
}
