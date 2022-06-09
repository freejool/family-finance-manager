package com.example.as.Entity;

import android.util.Log;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

public class Account extends Row {
    @Col(order = 0)
    private final CanBeRef<Integer> id;

    @Col(order = 1)
    private final CanBeRef<String> type;

    @Col(order = 2)
    private final CanBeRef<Integer> user_id;

    @Col(order = 3)
    private final CanBeRef<Double> balance;

    @Col(order = 4)
    private final CanBeRef<String> note;


    protected static HashMap<String, Field> str2FldDict=new HashMap<>();
    protected static HashMap<Field, String> fld2StrDict=new HashMap<>();
    protected static Vector<String> columnNameList=new Vector<>();
    //表示表名，数据库表和Java类型匹配比较固定的情况下考虑使用反射获取最后一段类名作为表名
    protected static String tableName = "";
    @Override
    public String getTableName() {
        return tableName;
    }

    static {
        initRow(Account.class);
    }

    public Account() {
        tableName = "account";
        id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        type = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        user_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        balance = new CanBeRef<>(DBCatcher.doubleCatcher, DBTransfer.doubleTransfer);
        note = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        Bind(Account.class);
    }

    public Account(ResultSet resultSet){
        this();
        setByResultSet(resultSet);
    }
}