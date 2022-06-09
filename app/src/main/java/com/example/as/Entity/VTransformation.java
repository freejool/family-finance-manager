package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;

public class VTransformation extends Row
{
    @Col(order = 0)
    CanBeRef<Integer> ID;
    @Col(order = 1)
    CanBeRef<String> in_or_out;
    @Col(order = 2)
    CanBeRef<Double> amount;
    @Col(order = 3)
    CanBeRef<LocalDateTime> time_created;
    @Col(order = 4)
    CanBeRef<LocalDateTime> time_ended;
    @Col(order = 5)
    CanBeRef<String> accept;
    @Col(order = 6)
    CanBeRef<Integer> promoter_id;
    @Col(order = 7)
    CanBeRef<String> promoter_name;
    @Col(order = 8)
    CanBeRef<String> promoter_sex;
    @Col(order = 9)
    CanBeRef<String> promoter_email;
    @Col(order = 10)
    CanBeRef<LocalDateTime> promoter_create_date;
    @Col(order = 11)
    CanBeRef<Integer> receiver_id;
    @Col(order = 12)
    CanBeRef<String> receiver_name;
    @Col(order = 13)
    CanBeRef<String> receiver_sex;
    @Col(order = 14)
    CanBeRef<String> receiver_email;
    @Col(order = 15)
    CanBeRef<LocalDateTime> receiver_create_date;

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
        initRow(VTransformation.class);
    }

    public VTransformation()
    {
        super();
        tableName="v_transformation_for_user_term";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        in_or_out=new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        amount=new CanBeRef<>(DBCatcher.doubleCatcher, DBTransfer.doubleTransfer);
        time_created=new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        time_ended =new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        accept =new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        promoter_id= new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        promoter_name=new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        promoter_sex=new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        promoter_email=new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        promoter_create_date=new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        receiver_id=new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        receiver_name=new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);;
        receiver_sex=new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);;
        receiver_email=new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        receiver_create_date=new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        Bind(VTransformation.class);
    }
}
