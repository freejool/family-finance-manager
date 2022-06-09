package com.example.as.Entity;

import androidx.annotation.NonNull;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

public class TransType extends Row {
    @Col(order = 0)
    public CanBeRef<Integer> ID;
    @Col(order = 1)
    public CanBeRef<String> type;
    @Col(order = 2)
    public CanBeRef<String> in_or_out;
    @Col(order = 3)
    public CanBeRef<Integer> user_id;

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
        initRow(TransType.class);
    }

    public TransType() {
        super();
        tableName = "trans_type";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        type = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        in_or_out = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        user_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);

        Bind(TransType.class);
    }

    public TransType(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }

    @NonNull
    @Override
    public String toString() {
        return this.user_id + " | " + this.type;
    }
}