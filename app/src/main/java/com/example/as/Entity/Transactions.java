package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.sql.ResultSet;
import java.time.LocalDateTime;

/**
 * 表示转账表的数据库模型
 */
public class Transactions extends Row //收支记录表实体类
{
    @Col(order = 0)
    public CanBeRef<Integer> ID;
    @Col(order = 1)
    public CanBeRef<Integer> user_id;
    @Col(order = 2)
    public CanBeRef<String> type;
    @Col(order = 3)
    public CanBeRef<Float> amount;
    @Col(order = 4)
    public CanBeRef<String> in_or_out;
    @Col(order = 5, col_name = "transaction_time")
    public CanBeRef<LocalDateTime> Transaction_time;
    @Col(order = 6)
    public CanBeRef<String> note;

    //进行数据库更新时会更新的字段
//    public Vector<Object> update_list;

    static {
        initRow(Transactions.class);
    }

    public Transactions() {
        tableName = "transactions";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        user_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        type = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        amount = new CanBeRef<>(DBCatcher.floatCatcher, DBTransfer.floatTransfer);
        in_or_out = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        Transaction_time = new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        note = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);

        Bind();
    }

    public Transactions(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }

}