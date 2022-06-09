package com.example.as.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

import com.example.as.R;
import com.example.as.dao.CommonDAO;
import com.example.as.database.DatabaseQuery;

import java.util.Arrays;


public class StatisticsTransactions extends FragmentActivity {
    ListView gvInfo;// 创建GridView对象
    // 定义字符串数组，存储系统功能
    String[] titles = new String[]{"分类型统计", "分用户统计", "指定用户和时间段"};
    String strType = "";// 创建字符串，记录是分类型还是分用户

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_transactions);// 设置布局文件

        gvInfo = findViewById(R.id.statistics_transactions);// 获取布局文件中的gvInfo组件
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);// 创建pictureAdapter对象
        gvInfo.setAdapter(adapter);// 为GridView设置数据源
        gvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {// 为GridView设置项单击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;// 创建Intent对象
                DatabaseQuery db;

                switch (position) {
                    case 0:
                        strType = "type";// 为strType变量赋值
                        intent = new Intent(StatisticsTransactions.this, InOrOut.class);// 使用TotalChart窗口初始化Intent对象
                        intent.putExtra("passType", strType);// 设置要传递的数据
                        startActivity(intent);// 执行Intent，打开相应的Activity
                        break;
                    case 1:
                        strType = "user_name";// 为strType变量赋值
                        intent = new Intent(StatisticsTransactions.this, InOrOut.class);// 使用TotalChart窗口初始化Intent对象
                        intent.putExtra("passType", strType);// 设置要传递的数据
                        startActivity(intent);// 执行Intent，打开相应的Activity
                        break;
                    case 2:

                        intent = new Intent(StatisticsTransactions.this, user_span.class);
                        startActivity(intent);// 执行Intent，打开相应的Activity
                        break;

                }
            }
        });
    }
}
