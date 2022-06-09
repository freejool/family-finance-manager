package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;

public class Transformation extends Row
{
    @Col(order = 0)
    private CanBeRef<Integer> ID;
    @Col(order = 1)
    private CanBeRef<Integer> promoter_id;
    @Col(order = 2)
    private CanBeRef<Integer> promoter_account_id;
    @Col(order = 3)
    private CanBeRef<Integer> receiver_id;
    @Col(order = 4)
    private CanBeRef<Integer> receiver_account_id;
    @Col(order = 5)
    private CanBeRef<String> in_or_out;
    @Col(order = 6)
    private CanBeRef<Double> amount;
    @Col(order = 7)
    private CanBeRef<LocalDateTime> time_created;
    @Col(order = 8)
    private CanBeRef<LocalDateTime> time_ended;
    @Col(order = 9)
    private CanBeRef<String> accept;

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
        initRow(Transformation.class);
    }

    public Transformation() {
        super();
        tableName = "transformation";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        promoter_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        promoter_account_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        receiver_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        receiver_account_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        in_or_out = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        amount = new CanBeRef<>(DBCatcher.doubleCatcher, DBTransfer.doubleTransfer);
        time_created=new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        time_ended=new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        accept = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);

        Bind(Transformation.class);
    }

    public Transformation(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }
}