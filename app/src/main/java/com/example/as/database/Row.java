package com.example.as.database;


import android.util.Log;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class Row implements IRow
{
    //进行数据库更新时会更新的字段
    public Vector<String> update_list;

    public HashMap<String,CanBeRef<?>> str2obj_dict;
    public HashMap<CanBeRef<?>,String> obj2str_dict;

    protected static HashMap<String,Field> str2fld_dict;
    protected static HashMap<Field,String> fld2str_dict;
    protected static Vector<String> column_name_list;
    //表示表名，数据库表和Java类型匹配比较固定的情况下考虑使用反射获取最后一段类名作为表名
    protected static String table_name="";
    static
    {
        //initRow(Row.class);
    }

    //初始化行，将成员变量按照注解加入相应字典和列表
    protected static void initRow(Class<?> clazz)
    {
        str2fld_dict=new HashMap<>();
        fld2str_dict=new HashMap<>();
        column_name_list =new Vector<>();

        Field[] declared_fields =clazz.getDeclaredFields();
        for (Field field:declared_fields)
        {
            if(field.isAnnotationPresent(Col.class))
            {
                Col current_col=field.getAnnotation(Col.class);
                if(current_col.col_name().isEmpty())
                {
                    str2fld_dict.put(field.getName(),field);
                    fld2str_dict.put(field,field.getName());
                    column_name_list.add(field.getName());
                }
                else
                {
                    str2fld_dict.put(current_col.col_name(),field);
                    fld2str_dict.put(field,current_col.col_name());
                    column_name_list.add(current_col.col_name());
                }
            }
        }
    }

    public Row()
    {
        str2obj_dict=new HashMap<>();
        obj2str_dict=new HashMap<>();
    }

    //将变量名称与数据库列名称绑定
    public void Bind()
    {
        for(Map.Entry<String,Field> entry:str2fld_dict.entrySet())
        {
            try
            {
                System.out.println("加入了一个对象"+entry.getKey()+entry.getValue().getType());
                str2obj_dict.put(entry.getKey(),(CanBeRef<?>)entry.getValue().get(this));
                obj2str_dict.put((CanBeRef<?>)entry.getValue().get(this),entry.getKey());
            }
            catch (IllegalAccessException e){}
        }
    }

    public Row(ResultSet resultSet)
    {
        this();
        setByResultSet(resultSet);
    }

    //表示表名，数据库表和Java类型匹配比较固定的情况下考虑使用反射获取最后一段类名作为表名
    @Override
    public String getTableName()
    {
        return table_name;
    }

    //将结果集的内容设置到此类实例上
    @Override
    public void setByResultSet(ResultSet resultSet)
    {
        for(Map.Entry<String,CanBeRef<?>> entry:str2obj_dict.entrySet())
        {
            entry.getValue().setValueByResultset(resultSet,entry.getKey());
        }
    }

    @Override
    public String getSqlValues()
    {
        StringBuilder builder=new StringBuilder();
        builder.append("values(");
        for(String column_name: column_name_list)
        {
            builder.append(Objects.requireNonNull(str2obj_dict.get(column_name)).
                            how_to_convert_to_db_data_version)
                    .append(',');
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(')');
        return  builder.toString();
    }

    //这表示了一般的获取器,这将仅对部分适合的类型生效
    public static class DBCatcher<T>
    {
        public static IFromResultset<Integer> int_catcher;
        public static IFromResultset<Float> float_catcher;
        public static IFromResultset<String> string_catcher;
        public static IFromResultset<Double> double_catcher;

        static
        {
            int_catcher= (rs, col_name) ->
            {
                try
                {return rs.getInt(col_name);}
                catch (SQLException e)
                {
                    Log.e("SQLError","未能获取对应列");return -1;}
            };

            float_catcher=(rs, col_name) ->
            {
                try
                {return rs.getFloat(col_name);}
                catch (SQLException e)
                {Log.e("SQLError","未能获取对应列");return -1f;}
            };

            string_catcher=(rs, col_name) ->
            {
                try
                {return rs.getString(col_name);}
                catch (SQLException e)
                {Log.e("SQLError","未能获取对应列");return "";}
            };

            double_catcher=(rs, col_name) ->
            {
                try
                {return rs.getDouble(col_name);}
                catch (SQLException e)
                {Log.e("SQLError","未能获取对应列");return -1d;}
            };
        }

    }

    //这表示了一般的整型给值器，整型向数据库值要求的字符串转换的实现
    public static class DBTranser
    {
        public static IToDatabaseValue<Integer,String> int_transer;
        public static IToDatabaseValue<Float,String> float_transer;
        public static IToDatabaseValue<String,String> string_transer;
        public static IToDatabaseValue<Double,String> double_transer;

        static
        {
            int_transer= Object::toString;
            float_transer= Object::toString;
            string_transer= local_value -> {return "'"+local_value.toString()+"'";};
            double_transer=Objects::toString;
        }

    }
}
