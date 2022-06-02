package com.example.as.activity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.as.Entity.Transactions;
import com.example.as.R;
import com.example.as.dao.CommonDAO;

public class InAccountInfo extends Activity {
    public static final String FLAG = "id";// 定义一个常量，用来作为请求码
    ListView lvinfo;// 创建ListView对象
    String strType = "";// 创建字符串，记录管理类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inaccountinfo);// 设置布局文件
        lvinfo = findViewById(R.id.in_come_info_listview);// 获取布局文件中的ListView组件
        ShowInfo();// 调用自定义方法显示收入信息

    }

    private void ShowInfo() {// 用来根据传入的管理类型，显示相应的信息

        ArrayAdapter<String> arrayAdapter;// 创建ArrayAdapter对象
        Transactions transaction_row = new Transactions();
        CommonDAO<Transactions> transaction_dao = new CommonDAO<>();
        ResultSet rs;
        try {
            rs = transaction_dao.find(transaction_row, "*", "");
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("SQL", transaction_dao.getLastSQLExecuted());
        Vector<String> tr_str_list = new Vector<>();
        try {
            //rs.first();
            rs.next();
            while (rs.next()) {
                transaction_row.setByResultSet(rs);
                tr_str_list.add(transaction_row.toString());
                rs.next();
            }
            transaction_dao.close();
        } catch (SQLException e) {
            Log.e("SQL", "没能获取收入或支出信息" + e.getSQLState());
        }
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tr_str_list);
        lvinfo.setAdapter(arrayAdapter);// 为ListView列表设置数据源
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();// 实现基类中的方法
        ShowInfo();// 显示收入信息
    }
}

class TransactionInAdapter extends ArrayAdapter<HashMap<String,Object>>
{
	private int resourceId;
	public TransactionInAdapter(@NonNull Context context, int resource, @NonNull List<HashMap<String, Object>> objects)
	{
		super(context, resource, objects);
		resourceId=resource;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
	{
		HashMap<String,Object> str2value_dict=getItem(position);
		return super.getView(position, convertView, parent);
	}
}
