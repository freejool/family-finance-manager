package com.example.as.activity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.as.Entity.Transactions;
import com.example.as.R;
import com.example.as.dao.CommonDAO;

public class InAccountInfo extends Activity {
    public static final String FLAG = "id";// 定义一个常量，用来作为请求码
    public ListView lvinfo;// 创建ListView对象
    public Button in_out_button;
    public Button only_in_button;
    public Button only_out_button;
    String strType = "";// 创建字符串，记录管理类型
    static final String in_out_condition="where user_id=%d order by transaction_time desc";
    static final String only_in_condition="where in_or_out='收入' and user_id=%d order by transaction_time desc";
    static final String only_out_condition="where in_or_out='支出' and user_id=%d order by transaction_time desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inaccountinfo);// 设置布局文件
        lvinfo = findViewById(R.id.tform_list_view);// 获取布局文件中的ListView组件
        in_out_button=findViewById(R.id.tform_finished);
        only_in_button=findViewById(R.id.tform_to_accept);
        only_out_button=findViewById(R.id.tform_promoted);
        SharedPreferences shd=getSharedPreferences("user_info",Context.MODE_PRIVATE);
        int user_id=shd.getInt("id",-1);
        in_out_button.setOnClickListener(v -> ShowInfo(String.format(in_out_condition, user_id)));
        only_in_button.setOnClickListener(v -> ShowInfo(String.format(only_in_condition,user_id)));
        only_out_button.setOnClickListener(v -> ShowInfo(String.format(only_out_condition,user_id)));
        // 调用自定义方法显示收入信息
        ShowInfo(String.format(in_out_condition, user_id));
    }

    private void ShowInfo(String condition) {// 用来根据传入的管理类型，显示相应的信息

        TransactionInAdapter arrayAdapter;// 创建ArrayAdapter对象
        Transactions transaction_row = new Transactions();
        CommonDAO<Transactions> transaction_dao = new CommonDAO<>();
        ResultSet rs;
        try {
            rs = transaction_dao.find(transaction_row, "*", condition);
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("SQL", transaction_dao.getLastSQLExecuted());
        Vector<HashMap<String,Object>> mapList = new Vector<>();
        try {
            //rs.first();
            //rs.next();
            while (rs.next()) {
                transaction_row.setByResultSet(rs);
                mapList.add(transaction_row.genDictData());
            }
            transaction_dao.close();
        } catch (SQLException e) {
            Log.e("SQL", "没能获取收入或支出信息" + e.getSQLState());
        }
        arrayAdapter = new TransactionInAdapter(this,
                R.layout.transaction_in_item, mapList);
        lvinfo.setAdapter(arrayAdapter);// 为ListView列表设置数据源
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();// 实现基类中的方法
        //ShowInfo();// 显示收入信息
    }
}

class TransactionInAdapter extends ArrayAdapter<HashMap<String,Object>>
{
	private final int resourceId;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日\nHH时mm:ss");
    static String note_hint="备注:%s";
    static String null_hint="无";
    static String elip_hint="...";
	public TransactionInAdapter(@NonNull Context context, int resource, @NonNull List<HashMap<String, Object>> objects)
	{
		super(context, resource, objects);
		resourceId=resource;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
	{
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(resourceId,null);
            holder =new ViewHolder();
            holder.tranInType=convertView.findViewById(R.id.tran_in_type);
            holder.tranInAmount=convertView.findViewById(R.id.tran_in_amount);
            holder.tranInTime=convertView.findViewById(R.id.tran_in_time);
            holder.tranInNote=convertView.findViewById(R.id.tran_in_note);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder) convertView.getTag();
        }

        HashMap<String,Object> str2value_dict=getItem(position);
        Log.i("SQL", str2value_dict.toString());
        holder.tranInType.setText((String)str2value_dict.get("type"));
        String in_or_out=(String)str2value_dict.get("in_or_out");
        if(Objects.equals(in_or_out, "收入"))
        {
            holder.tranInAmount.setText(String.format("+%.2f", str2value_dict.get("amount")));
            holder.tranInAmount.setBackgroundColor(getContext().getColor(R.color.transaction_in_bk));
            holder.tranInAmount.setTextColor(getContext().getColor(R.color.transaction_in_char));
        }
        else if(Objects.equals(in_or_out, "支出"))
        {
            holder.tranInAmount.setText(String.format("-%.2f",str2value_dict.get("amount")));
            holder.tranInAmount.setBackgroundColor(getContext().getColor(R.color.transaction_out_bk));
            holder.tranInAmount.setTextColor(getContext().getColor(R.color.transaction_out_char));
        }
        else
        {
            holder.tranInAmount.setText(String.format("%.2f",str2value_dict.get("amount")));
            holder.tranInAmount.setBackgroundColor(getContext().getColor(R.color.transaction_ud_bk));
            holder.tranInAmount.setTextColor(getContext().getColor(R.color.transaction_ud_char));
        }

        holder.tranInTime.setText(((LocalDateTime)str2value_dict.get("transaction_time")).format(formatter));

        String note=(String)str2value_dict.get("note");
        if(note==null)
            note=null_hint;
        else if(note.length()>=7)
            note=note.substring(0,6)+elip_hint;
        holder.tranInNote.setText(String.format(note_hint,note));

		return convertView;
	}


    class ViewHolder {

        TextView tranInType;
        TextView tranInAmount;
        TextView tranInTime;
        TextView tranInNote;
    }
}
