package com.example.as.database;


import android.os.Debug;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * 该类通过反射和注解来建立 字符串到特定成员对象的字典 和 特定成员对象到字符串的字典
 * 以及 包含顺序的特定成员对应列名称字符串列表。
 *
 * <p>上文的特定成员指使用了{@link Col}注解的成员，它们必须是public的，否则上述字典和列表会忽略它们。</p>
 * <p>由于Java的多数数据类型不可改，因此通常这些成员是{@link CanBeRef}的实例，该类型还提供两个功能，详情查看此类型的说明。
 * <p>使用方法：要声明一个与数据库表对接的类型A时，继承此类型，在表示数据库列的成员上使用{@link Col}注解。然后按照如下步骤：
 * (1)在新的类型中使用静态初始化块并且调用{@link Row#initRow(Class)}方法，参数为A.class。
 * (2)在撰写构造函数时，先为{@link Row#table_name}赋表名的值，然后
 * 初始化表示数据库列的成员变量并为其设置抓取器和转换器。最终使用{@link Row#Bind()}方法进行绑定。</p>
 *
 * <p>此类已实现基本的从{@link ResultSet}抓取到A的方法{@link Row#setByResultSet(ResultSet)}。
 * 以及从实例转换为数据库所需的values(...)字符串的方法{@link Row#getSqlValues()}。其它功能等待被实现或继承后实现。</p>
 *
 */
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

    /**
     * 静态初始化流程，需要继承者手动调用，类型{@link Row}本身不会调用（对类型的解析似乎无法被继承）
     * @param clazz 需要被解析的类型
     */
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

        column_name_list.sort(Comparator.comparingInt(
                m ->
                {
                    try
                    {
                        return str2fld_dict.get(m).getAnnotation(Col.class).order();
                    }catch (NullPointerException e)
                    {
                        Log.e("Row","对列名排序时出现问题");
                    }
                    return -1;
                }
        ));
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
                //Log.e("SQL","加入了一个对象"+entry.getKey()+entry.getValue().getType());
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

    @Override
    public String toString()
    {
        StringBuilder builder=new StringBuilder();
        builder.append(table_name).append('{');
        for(String column_name: column_name_list)
        {
            builder.append(column_name).append('=')
                    .append(Objects.requireNonNull(str2obj_dict.get(column_name)))
                    .append(',');
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append('}');
        return  builder.toString();
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


    public String getSqlColumnNames(boolean include_or_exclude,Vector<CanBeRef<?>> col_params)
    {
        StringBuilder builder=new StringBuilder();
        builder.append("(");
        boolean any_append=false;
        if(include_or_exclude)
        {
            if(col_params!=null)
            {
                for(CanBeRef<?> obj: col_params)
                {
                    if(!obj2str_dict.containsKey(obj))
                    {
                        continue;
                    }
                    builder.append(obj2str_dict.get(obj))
                            .append(',');
                    any_append=true;
                }
            }
        }
        else
        {

            HashSet<CanBeRef<?>> obj_set;
            if(col_params!=null)
            {
                obj_set=new HashSet<>(col_params);
            }
            else
            {
                obj_set=new HashSet<>();
            }
            for(String column_name: column_name_list)
            {
                CanBeRef<?> obj=Objects.requireNonNull(str2obj_dict.get(column_name));
                if(obj_set.contains(obj))
                    continue;
                builder.append(column_name)
                        .append(',');
                any_append=true;
            }
        }
        if(any_append)
            builder.deleteCharAt(builder.length()-1);
        builder.append(')');
        return  builder.toString();
    }


    /**
     * 获取数据库列的值组成的形如"values(2,4,...)"的字符串
     * @param include_or_exclude 包含或排除，此值为true时表示包含，
     *                           则该函数会尝试将参数列表的参数按列表顺序生成此字符串，忽略参数中不属于此实例成员的对象。
     *                           此值为false时表示排除，
     *                           该函数会按成员的注解中顺序生成字符串，忽略参数列表中提到的此实例的成员。
     * @param col_params 参数列表
     * @return 形如"values(2,4,...)"的字符串
     */
    @Override
    public String getSqlValues(boolean include_or_exclude,Vector<CanBeRef<?>> col_params)
    {
        StringBuilder builder=new StringBuilder();
        builder.append("values(");
        boolean any_append=false;
        if(include_or_exclude)
        {
            if(col_params!=null)
            {
                for(CanBeRef<?> obj: col_params)
                {
                    if(!obj2str_dict.containsKey(obj))
                    {
                        continue;
                    }
                    builder.append(obj.getSqlValues())
                            .append(',');
                    any_append=true;
                }
            }
        }
        else
        {

            HashSet<CanBeRef<?>> obj_set;
            if(col_params!=null)
            {
                obj_set=new HashSet<>(col_params);
            }
            else
            {
                obj_set=new HashSet<>();
            }
            for(String column_name: column_name_list)
            {
                CanBeRef<?> obj=Objects.requireNonNull(str2obj_dict.get(column_name));
                if(obj_set.contains(obj))
                    continue;
                builder.append(Objects.requireNonNull(str2obj_dict.get(column_name)).
                                getSqlValues())
                        .append(',');
                any_append=true;
            }
        }
        if(any_append)
            builder.deleteCharAt(builder.length()-1);
        builder.append(')');
        return  builder.toString();
    }

    @Override
    public String getSqlValues()
    {
        return getSqlValues(false,null);
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
                    Log.e("SQLError","未能获取对应列"+e);return -1;}
            };

            float_catcher=(rs, col_name) ->
            {
                try
                {return rs.getFloat(col_name);}
                catch (SQLException e)
                {Log.e("SQLError","未能获取对应列"+e);return -1f;}
            };

            string_catcher=(rs, col_name) ->
            {
                try
                {return rs.getString(col_name);}
                catch (SQLException e)
                {Log.e("SQLError","未能获取对应列"+e);return "";}
            };

            double_catcher=(rs, col_name) ->
            {
                try
                {return rs.getDouble(col_name);}
                catch (SQLException e)
                {Log.e("SQLError","未能获取对应列"+e);return -1d;}
            };
        }

    }

    //这表示了通用的给值器，表示了对象向数据库值要求的字符串转换的实现
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
            string_transer= local_value -> "'"+local_value+"'";
            double_transer=Objects::toString;
        }

    }
}
