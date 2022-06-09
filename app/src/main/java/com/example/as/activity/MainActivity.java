package com.example.as.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;

import com.example.as.Entity.Account;
import com.example.as.R;
import com.example.as.activity.debt.Debt;
import com.example.as.activity.modify_trans_type.TransTypeModify;
import com.example.as.activity.profit.Profit;
import com.example.as.dao.CommonDAO;
import com.example.as.database.DatabaseQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;


public class MainActivity extends FragmentActivity {
    ListView gvInfo;// 创建GridView对象
    TextView balanceTextview;
    Vector<StrFuncPair> name2FuncList;
    // 定义字符串数组，存储系统功能
    String[] titles = new String[]{"新增支出", "新增收入", "我的支出", "我的收入",
            "收支分析", "系统设置", "投资收益", "收支类型", "债务", "用户信息"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);// 设置布局文件

        init();
        //TODO:检测转账

        balanceTextview = findViewById(R.id.balance_textview);
        CommonDAO<Account> dao = new CommonDAO<>();
        Account acc = new Account();
        SharedPreferences shd = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int user_id = shd.getInt("id", -1);
        assert user_id != -1;

        acc.user_id.value = user_id;
        acc.type.value = "默认";
        double balance = 0;
        try {
            ResultSet rs;
            rs = dao.find(acc, "balance", "where type='" + acc.type + "' and user_id=" + acc.user_id);
            while (rs.next()) {
                balance = rs.getDouble(1);
            }
            balanceTextview.setText(String.format(Locale.getDefault(), "   余额：%.2f", balance));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        gvInfo = findViewById(R.id.gv_info);// 获取布局文件中的gvInfo组件
        name2FuncList =new Vector<>();
        name2FuncList.add
                (new StrFuncPair("我的账户", () -> {
                    Toast.makeText(this,"我们建议您等待此功能",Toast.LENGTH_SHORT);
                }));
        name2FuncList.add
                (new StrFuncPair("新增收支", () -> {
                    AddIncomeDialog dialogaddincome = new AddIncomeDialog();
                    dialogaddincome.show(MainActivity.this.getSupportFragmentManager(), "register");
                }));
        name2FuncList.add
                (new StrFuncPair("我的收支", () -> {
                    Intent intent = new Intent(MainActivity.this, InAccountInfo.class);// 使用Inaccountinfo窗口初始化Intent
                    startActivity(intent);// 打开Inaccountinfo
                }));
        name2FuncList.add
                (new StrFuncPair("收支分析", () -> {
                    Intent intent = new Intent(MainActivity.this, StatisticsTransactions.class);// 使用Showinfo窗口初始化Intent
                    startActivity(intent);// 打开Showinfo
                }));
        name2FuncList.add
                (new StrFuncPair("发起转账", () -> {
                    Toast.makeText(this,"我们建议您等待此功能",Toast.LENGTH_SHORT);
                }));
        name2FuncList.add
                (new StrFuncPair("系统设置", () -> {
                    Intent intent = new Intent(MainActivity.this, Sysset.class);// 使用Sysset窗口初始化Intent
                    startActivity(intent);// 打开Sysset
                }));
        name2FuncList.add
                (new StrFuncPair("投资收益", () -> {
                    Intent intent = new Intent(MainActivity.this, Profit.class);// 使用Profit窗口初始化Intent
                    startActivity(intent);// 打开Profit
                }));
        name2FuncList.add
                (new StrFuncPair("收支类型", () -> {
                    Intent intent = new Intent(MainActivity.this, TransTypeModify.class);
                    startActivity(intent);
                }));
        name2FuncList.add
                (new StrFuncPair("债务", () -> {
                    Intent intent = new Intent(MainActivity.this, Debt.class); // 债务;
                    startActivity(intent);
                }));
        name2FuncList.add
                (new StrFuncPair("用户信息", () -> {
                    Intent intent = new Intent(MainActivity.this, VSafeUserInfo.class); // 用户信息
                    startActivity(intent);
                }));
        name2FuncList.add
                (new StrFuncPair("帮助", () -> {
                    Intent intent = new Intent(MainActivity.this, Help.class);// 使用Help窗口初始化Intent
                    startActivity(intent);// 打开Help
                }));
        name2FuncList.add
                (new StrFuncPair("退出", () -> {
                    finish();// 关闭当前Activity
                }));


        ArrayAdapter<StrFuncPair> adapter = new ArrayAdapter<StrFuncPair>(this, android.R.layout.simple_list_item_1, name2FuncList);// 创建pictureAdapter对象
        gvInfo.setAdapter(adapter);// 为GridView设置数据源
        gvInfo.setOnItemClickListener(new OnItemClickListener() {// 为GridView设置项单击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Pair<String,Runnable> str_func_pair= (Pair<String, Runnable>) gvInfo.getItemAtPosition(position);
                str_func_pair.second.run();
//                Intent intent = null;// 创建Intent对象
//                switch (position) {
//                    case 0:
//                        AddOutcomeDialog dialogaddpay = new AddOutcomeDialog();
//                        dialogaddpay.show(MainActivity.this.getSupportFragmentManager(), "addpay");
//                        break;
//                    case 1:
//                        AddIncomeDialog dialogaddincome = new AddIncomeDialog();
//                        dialogaddincome.show(MainActivity.this.getSupportFragmentManager(), "register");
//                        break;
//                    case 2:
//                        intent = new Intent(MainActivity.this, Outaccountinfo.class);// 使用Outaccountinfo窗口初始化Intent
//                        startActivity(intent);// 打开Outaccountinfo
//                        break;
//                    case 3:
//                        intent = new Intent(MainActivity.this, InAccountInfo.class);// 使用Inaccountinfo窗口初始化Intent
//                        startActivity(intent);// 打开Inaccountinfo
//                        break;
//                    case 4:
//                        intent = new Intent(MainActivity.this, StatisticsTransactions.class);// 使用Showinfo窗口初始化Intent
//                        startActivity(intent);// 打开Showinfo
//                        break;
//                    case 5:
//                        intent = new Intent(MainActivity.this, Sysset.class);// 使用Sysset窗口初始化Intent
//                        startActivity(intent);// 打开Sysset
//                        break;
//                    case 6:
//                        intent = new Intent(MainActivity.this, Profit.class);// 使用Profit窗口初始化Intent
//                        startActivity(intent);// 打开Profit
//                        break;
//                    case 7:
//                        intent = new Intent(MainActivity.this, TransTypeModify.class);
//                        startActivity(intent);
//                        break;
//                    case 8:
//                        intent = new Intent(MainActivity.this, Help.class);// 使用Help窗口初始化Intent
//                        startActivity(intent);// 打开Help
//                        break;
//                    case 9:
//                        finish();// 关闭当前Activity
//                        break;
//                }
            }
        });
    }

    private void init() {
        DatabaseQuery db;
        ResultSet rs;
        try {
            String sql = "exec p_gen_v_max_stock_bonus_of_all_users";
            db = new DatabaseQuery(sql);
            db.start();
            db.join();
//        } catch (SQLException e) {
//            Log.e("SQL", Arrays.toString(e.getStackTrace()));
//            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Log.e("Thread", Arrays.toString(e.getStackTrace()));
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
        }
    }

}

class StrFuncPair extends Pair<String,Runnable>
{

    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public StrFuncPair(String first, Runnable second) {
        super(first, second);
    }

    @Override
    public String toString()
    {
        return first.toString();
    }
}
