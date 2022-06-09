package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;

/**
 * 表示转账表的数据库模型
 */
public class Transactions extends Row  //收支记录表实体类
{
    @Col(order = 0)
    public CanBeRef<Integer> ID;
    @Col(order = 1)
    public CanBeRef<Integer> user_id;
    @Col(order = 2)
    public CanBeRef<String> type;
    @Col(order = 3)
    public CanBeRef<Double> amount;
    @Col(order = 4)
    public CanBeRef<String> in_or_out;
    @Col(order = 5, col_name = "transaction_time")
    public CanBeRef<LocalDateTime> Transaction_time;
    @Col(order = 6)
    public CanBeRef<String> note;


    protected static HashMap<String, Field> str2FldDict=new HashMap<>();
    protected static HashMap<Field, String> fld2StrDict=new HashMap<>();
    protected static Vector<String> columnNameList=new Vector<>();
    //表示表名，数据库表和Java类型匹配比较固定的情况下考虑使用反射获取最后一段类名作为表名
    protected static String tableName = "";
    @Override
    public String getTableName() {
        return tableName;
    }

    //进行数据库更新时会更新的字段
//    public Vector<Object> update_list;

    static {
        initRow(Transactions.class);
    }

    public Transactions() {
        super();
        tableName = "transactions";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        user_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        type = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        amount = new CanBeRef<>(DBCatcher.doubleCatcher, DBTransfer.doubleTransfer);
        in_or_out = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        Transaction_time = new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        note = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);

        Bind(Transactions.class);
    }

    public Transactions(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }

}