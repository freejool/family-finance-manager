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
import com.example.as.Entity.VTransformation;
import com.example.as.R;
import com.example.as.dao.CommonDAO;
import com.example.as.database.DatabaseQuery;

public class TransformationList extends Activity
{
    ListView tformListView;
    public Button tformToAccept;
    public Button tformPromoted;
    public Button tformFinished;
    public static final String to_accept_condition="where accept='to_be_confirmed' and receiver_id=%d order by time_created desc";
    static final String finished_condition="where (accept='accepted' or accept='refused') and (receiver_id=%d or promoter_id=%d) order by time_created desc";
    static final String promoted_condition="where accept='to_be_confirmed' and promoter_id=%d order by time_created desc";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tform_list);
        tformListView=findViewById(R.id.tform_list_view);
        tformPromoted=findViewById(R.id.tform_promoted);
        tformToAccept=findViewById(R.id.tform_to_accept);
        tformFinished=findViewById(R.id.tform_finished);
        SharedPreferences shd=getSharedPreferences("user_info",Context.MODE_PRIVATE);
        int user_id=shd.getInt("id",-1);

        tformPromoted.setOnClickListener(v->showInfo(String.format(promoted_condition,user_id)));
        tformToAccept.setOnClickListener(v->showInfo(String.format(to_accept_condition,user_id)));
        tformFinished.setOnClickListener(v->showInfo(String.format(finished_condition,user_id,user_id)));

        showInfo(String.format(to_accept_condition,user_id));
    }

    public void showInfo(String condition)
    {
        VTransformationAdapter arrayAdapter;
        VTransformation vt=new VTransformation();
        CommonDAO<VTransformation> dao = new CommonDAO<>();
        ResultSet rs;
        try {
            rs = dao.find(vt, "*", condition);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
            return;
        }
        Vector<HashMap<String,Object>> mapList = new Vector<>();
        try {
            while (rs.next()) {
                vt.setByResultSet(rs);
                mapList.add(vt.genDictData());
            }
            dao.close();
        } catch (SQLException e) {
            Log.e("SQL", "没能获取收入或支出信息" + e.getSQLState());
        }

        arrayAdapter = new VTransformationAdapter(this,
                R.layout.tform_list_item, mapList);
        tformListView.setAdapter(arrayAdapter);// 为ListView列表设置数据源
    }
}

class VTransformationAdapter extends ArrayAdapter<HashMap<String,Object>>
{
    private final int resourceId;
    int user_id;
    static String left_arrow="<<<";
    static String right_arrow=">>>";
    static String null_hint="无";
    static String promoter="发起方";
    static String receiver="接收方";
    static String state_tobe_confirmed="待确认";
    static String state_be_accepted="已被接受";
    static String state_accepted="已接受";
    static String state_be_refused="已被拒绝";
    static String state_refused="已拒绝";
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    class ViewHolder
    {
        TextView tformLeftTitle;
        TextView tformLeftName;
        TextView tformLeftID;
        TextView tformLeftTime;
        TextView tformRightTitle;
        TextView tformRightName;
        TextView tformRightID;
        TextView tformRightTime;

        TextView tformState;
        TextView tformArrow;
        TextView tformAmount;

        View buttonGroup;
        Button tformAccept;
        Button tformRefuse;
    }


    public VTransformationAdapter(@NonNull Context context, int resource, @NonNull List<HashMap<String, Object>> objects)
    {
        super(context, resource, objects);
        resourceId=resource;

        SharedPreferences shd=context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        user_id=shd.getInt("id",-1);
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
            holder.tformLeftTitle=convertView.findViewById(R.id.tform_left_title);
            holder.tformLeftName=convertView.findViewById(R.id.tform_left_name);
            holder.tformLeftID=convertView.findViewById(R.id.tform_left_id);
            holder.tformLeftTime=convertView.findViewById(R.id.tform_left_time);
            holder.tformRightTitle=convertView.findViewById(R.id.tform_right_title);
            holder.tformRightID=convertView.findViewById(R.id.tform_right_id);
            holder.tformRightName=convertView.findViewById(R.id.tform_right_name);
            holder.tformRightTime=convertView.findViewById(R.id.tform_right_time);

            holder.tformState=convertView.findViewById(R.id.tform_state);
            holder.tformArrow=convertView.findViewById(R.id.tform_arrow);
            holder.tformAmount=convertView.findViewById(R.id.tform_amount);

            holder.buttonGroup=convertView.findViewById(R.id.button_group);
            holder.tformAccept=convertView.findViewById(R.id.tform_accept);
            holder.tformRefuse=convertView.findViewById(R.id.tform_refuse);

            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder) convertView.getTag();
        }
        HashMap<String,Object> dict=getItem(position);
        Log.i("SQL", dict.toString());

        holder.tformAmount.setText(String.format("+%.2f", dict.get("amount")));

        //需要被确认，则保持按钮
        if(Objects.equals(dict.get("accept"),"to_be_confirmed"))
        {
            holder.tformState.setText(state_tobe_confirmed);
            if((int)dict.get("promoter_id")==user_id)
            {
                holder.buttonGroup.setVisibility(View.GONE);
            }
            else
            {
                holder.tformAccept.setOnClickListener(v->{
                    String sql=String.format("exec p_accept_one_transformation %d,1",(Integer)dict.get("ID"));
                    DatabaseQuery db=new DatabaseQuery(sql);
                    db.run();
                    db.close();
                    ((TransformationList)getContext()).showInfo(String.format(TransformationList.to_accept_condition,user_id));
                });

                holder.tformRefuse.setOnClickListener(v->
                {
                    String sql=String.format("exec p_refuse_one_transformation %d",(Integer)dict.get("ID"));
                    DatabaseQuery db=new DatabaseQuery(sql);
                    db.run();
                    db.close();
                    ((TransformationList)getContext()).showInfo(String.format(TransformationList.to_accept_condition,user_id));
                });
            }
        }
        else if (Objects.equals(dict.get("accept"),"accepted"))
        {
            holder.buttonGroup.setVisibility(View.GONE);
            if((int)dict.get("promoter_id")==user_id)
            {
                holder.tformState.setText(state_be_accepted);
            }
            else if((int)dict.get("receiver_id")==user_id)
            {
                holder.tformState.setText(state_accepted);
            }
        }
        else if (Objects.equals(dict.get("accept"),"refused"))
        {
            holder.buttonGroup.setVisibility(View.GONE);
            if((int)dict.get("promoter_id")==user_id)
            {
                holder.tformState.setText(state_be_refused);
            }
            else if((int)dict.get("receiver_id")==user_id)
            {
                holder.tformState.setText(state_refused);
            }
        }


        //发起者是登录用户
        if((int)dict.get("promoter_id")==user_id)
        {
            holder.tformLeftTitle.setText(promoter);
            holder.tformLeftName.setText(dict.get("promoter_name").toString());
            holder.tformLeftID.setText(dict.get("promoter_id").toString());
            holder.tformLeftTime.setText(((LocalDateTime)dict.get("time_created")).format(formatter));

            holder.tformRightTitle.setText(receiver);
            holder.tformRightName.setText(dict.get("receiver_name").toString());
            holder.tformRightID.setText(dict.get("receiver_id").toString());

            LocalDateTime time_ended=(LocalDateTime)dict.get("time_ended");
            if(time_ended==null)
                holder.tformRightTime.setText(null_hint);
            else
                holder.tformRightTime.setText(time_ended.format(formatter));

            //以支出为目的的转账
            if(Objects.equals(dict.get("in_or_out"),"支出"))
            {
                holder.tformArrow.setText(right_arrow);
            }
            else if(Objects.equals(dict.get("in_or_out"),"收入"))
            {
                holder.tformArrow.setText(left_arrow);
            }
        }
        else if((int)dict.get("receiver_id")==user_id)
        {
            holder.tformLeftTitle.setText(receiver);
            holder.tformLeftName.setText(dict.get("receiver_name").toString());
            holder.tformLeftID.setText(dict.get("receiver_id").toString());


            holder.tformRightTitle.setText(promoter);
            holder.tformRightName.setText(dict.get("promoter_name").toString());
            holder.tformRightID.setText(dict.get("promoter_id").toString());
            holder.tformRightTime.setText(((LocalDateTime)dict.get("time_created")).format(formatter));
            LocalDateTime time_ended=(LocalDateTime)dict.get("time_ended");
            if(time_ended==null)
                holder.tformLeftTime.setText(null_hint);
            else
                holder.tformLeftTime.setText(time_ended.format(formatter));

            //以支出为目的的转账
            if(Objects.equals(dict.get("in_or_out"),"支出"))
            {
                holder.tformArrow.setText(left_arrow);
            }
            else if(Objects.equals(dict.get("in_or_out"),"收入"))
            {
                holder.tformArrow.setText(right_arrow);
            }
        }


        return convertView;
    }
}
