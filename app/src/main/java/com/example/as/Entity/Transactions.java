package com.example.as.Entity;

import android.util.Log;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.IFromResultset;
import com.example.as.database.IToDatabaseValue;
import com.example.as.database.Row;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class Transactions extends Row //收支记录表实体类
{
    @Col
    public CanBeRef<Integer> ID;
    @Col
    private CanBeRef<Integer> user_id;
    @Col
    private CanBeRef<String> type;
    @Col
    private CanBeRef<Float> amount;
    @Col
    private CanBeRef<String> in_or_out;
    @Col(col_name = "transaction_time")
    private CanBeRef<LocalDateTime> Transaction_time;
    @Col
    private CanBeRef<String> note;

    //进行数据库更新时会更新的字段
//    public Vector<Object> update_list;

    public Transactions()
    {
        table_name="transactions";
        ID=new CanBeRef<>(DBCatcher.int_catcher, DBTranser.int_transer);
        user_id=new CanBeRef<>(DBCatcher.int_catcher, DBTranser.int_transer);
        type=new CanBeRef<>(DBCatcher.string_catcher, DBTranser.string_transer);
        amount=new CanBeRef<>(DBCatcher.float_catcher, DBTranser.float_transer);
        in_or_out=new CanBeRef<>(DBCatcher.string_catcher, DBTranser.string_transer);
        Transaction_time=new CanBeRef<>
        ((IFromResultset<LocalDateTime>) (rs, col_name) ->
        {
            try
            {
                return rs.getTimestamp("Transaction_time").toInstant().atZone(ZoneId.of("CST")).toLocalDateTime();
            }
            catch (SQLException e)
            {
                {Log.e("SQLError","未能获取对应列");return LocalDateTime.MIN;}
            }
        },
        (IToDatabaseValue<LocalDateTime, String>)local_value ->local_value.toLocalDate().toString());
        note=new CanBeRef<>(DBCatcher.string_catcher, DBTranser.string_transer);

        Bind();
    }

    public Transactions(ResultSet resultSet)
    {
        this();
        setByResultSet(resultSet);
    }

//    @Override
//    public String getTableName()
//    {
//        return Transactions.table_name;
//    }

//    @Override
//    public void setByResultSet(ResultSet resultSet)
//    {
//        super.setByResultSet(resultSet);
//    }


    @Override
    public String toString() {
        return "Transactions{" +
                "ID=" + ID +
                ", user_id=" + user_id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", in_or_out='" + in_or_out + '\'' +
                ", Transaction_time=" + Transaction_time +
                ", note='" + note + '\'' +
                '}';
    }

//    @Override
//    public String getSqlValues()
//    {
//        return super.getSqlValues();
//    }


}