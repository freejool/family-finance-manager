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


public class InOrOut extends FragmentActivity {
    ListView gvInfo;// 创建GridView对象
    // 定义字符串数组，存储系统功能
    String[] titles = new String[]{"收入", "支出"};
    private String passType="";	//记录是分类型还是分用户
    String strType = "";// 创建字符串，记录是收入还是支出
    String[] statistic = new String[2];//创建字符串数组记录数据分类类型以及是收入还是支出
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_or_out);// 设置布局文件
        Intent intent=getIntent();		//获取Intent对象
        Bundle bundle=intent.getExtras();		//获取传递的数据包
        passType=bundle.getString("passType");
        statistic[0] = passType;
        gvInfo = findViewById(R.id.InOrOut);// 获取布局文件中的gvInfo组件
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);// 创建pictureAdapter对象
        gvInfo.setAdapter(adapter);// 为GridView设置数据源
        gvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {// 为GridView设置项单击事件

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;// 创建Intent对象
                switch (position) {
                    case 0:
                        strType = "ininfo";// 为strType变量赋值
                        statistic[1] = strType;
                        intent = new Intent(InOrOut.this, TotalChartTest.class);// 使用TotalChart窗口初始化Intent对象
                        intent.putExtra("statistic", statistic);// 设置要传递的数据
                        startActivity(intent);// 执行Intent，打开相应的Activity
                        break;
                    case 1:
                        strType = "outinfo";// 为strType变量赋值
                        statistic[1] = strType;
                        intent = new Intent(InOrOut.this, TotalChartTest.class);// 使用TotalChart窗口初始化Intent对象
                        intent.putExtra("statistic", statistic);// 设置要传递的数据
                        startActivity(intent);// 执行Intent，打开相应的Activity
                        break;
                }
            }
        });
    }
}
