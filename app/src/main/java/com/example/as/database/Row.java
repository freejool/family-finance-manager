package com.example.as.database;


import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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
 * (2)在撰写构造函数时，先为{@link Row#tableName}赋表名的值，然后
 * 初始化表示数据库列的成员变量并为其设置抓取器和转换器。最终使用{@link Row#Bind()}方法进行绑定。</p>
 *
 * <p>此类已实现基本的从{@link ResultSet}抓取到A的方法{@link Row#setByResultSet(ResultSet)}。
 * 以及从实例转换为数据库所需的values(...)字符串的方法{@link Row#getSqlValues()}。其它功能等待被实现或继承后实现。</p>
 */
public class Row implements IRow {
    //进行数据库更新时会更新的字段
    public Vector<String> update_list;

    public HashMap<String, CanBeRef<?>> str2ObjDict;
    public HashMap<CanBeRef<?>, String> obj2StrDict;

    protected static HashMap<String, Field> str2FldDict;
    protected static HashMap<Field, String> fld2StrDict;
    protected static Vector<String> columnNameList;
    //表示表名，数据库表和Java类型匹配比较固定的情况下考虑使用反射获取最后一段类名作为表名
    protected static String tableName = "";

    static {
        //initRow(Row.class);
    }

    /**
     * 静态初始化流程，需要继承者手动调用，类型{@link Row}本身不会调用（对类型的解析似乎无法被继承）
     *
     * @param clazz 需要被解析的类型
     */
    protected static void initRow(Class<?> clazz) {
        str2FldDict = new HashMap<>();
        fld2StrDict = new HashMap<>();
        columnNameList = new Vector<>();

        Field[] declared_fields = clazz.getDeclaredFields();
        for (Field field : declared_fields) {
            if (field.isAnnotationPresent(Col.class)) {
                Col current_col = field.getAnnotation(Col.class);
                if (current_col.col_name().isEmpty()) {
                    str2FldDict.put(field.getName(), field);
                    fld2StrDict.put(field, field.getName());
                    columnNameList.add(field.getName());
                } else {
                    str2FldDict.put(current_col.col_name(), field);
                    fld2StrDict.put(field, current_col.col_name());
                    columnNameList.add(current_col.col_name());
                }
            }
        }

        columnNameList.sort(Comparator.comparingInt(
                m ->
                {
                    try {
                        return str2FldDict.get(m).getAnnotation(Col.class).order();
                    } catch (NullPointerException e) {
                        Log.e("Row", "对列名排序时出现问题");
                    }
                    return -1;
                }
        ));
    }

    public Row() {
        str2ObjDict = new HashMap<>();
        obj2StrDict = new HashMap<>();
    }

    //将变量名称与数据库列名称绑定
    public void Bind() {
        for (Map.Entry<String, Field> entry : str2FldDict.entrySet()) {
            try {
                //Log.e("SQL","加入了一个对象"+entry.getKey()+entry.getValue().getType());
                str2ObjDict.put(entry.getKey(), (CanBeRef<?>) entry.getValue().get(this));
                obj2StrDict.put((CanBeRef<?>) entry.getValue().get(this), entry.getKey());
            } catch (IllegalAccessException e) {
            }
        }
    }

    public Row(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }

    //表示表名，数据库表和Java类型匹配比较固定的情况下考虑使用反射获取最后一段类名作为表名
    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(tableName).append('{');
        for (String column_name : columnNameList) {
            builder.append(column_name).append('=')
                    .append(Objects.requireNonNull(str2ObjDict.get(column_name)))
                    .append(',');
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append('}');
        return builder.toString();
    }

    //将结果集的内容设置到此类实例上
    @Nullable
    @Override
    public void setByResultSet(ResultSet resultSet) {
        for (Map.Entry<String, CanBeRef<?>> entry : str2ObjDict.entrySet()) {
            entry.getValue().setValueByResultset(resultSet, entry.getKey());
        }
    }

    @Override
    public String getSqlValues() {
        StringBuilder builder = new StringBuilder();
        builder.append("values(");
        for (String column_name : columnNameList) {
            builder.append(Objects.requireNonNull(str2ObjDict.get(column_name)).
                    getSqlValues())
                    .append(',');
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(')');
        return builder.toString();
    }

    @Override
    public HashMap<String, Object> genDictData()
    {
        HashMap<String,Object> gened_dict=new HashMap<>();
        for(Map.Entry<String,CanBeRef<?>> entry:str2obj_dict.entrySet())
        {
            gened_dict.put(entry.getKey(),entry.getValue().value);
        }
        return gened_dict;
    }

    //这表示了一般的获取器,这将仅对部分适合的类型生效
    public static class DBCatcher<T> {
        public static IFromResultset<Integer> intCatcher;
        public static IFromResultset<Float> floatCatcher;
        public static IFromResultset<String> stringCatcher;
        public static IFromResultset<Double> doubleCatcher;
        public static IFromResultset<LocalDateTime> timeCatcher;

        static {
            intCatcher = (rs, col_name) ->
            {
                try {
                    return rs.getInt(col_name);
                } catch (SQLException e) {
                    Log.e("SQLError", "未能获取对应列" + e);
                    return -1;
                }
            };

            floatCatcher = (rs, col_name) ->
            {
                try {
                    return rs.getFloat(col_name);
                } catch (SQLException e) {
                    Log.e("SQLError", "未能获取对应列" + e);
                    return -1f;
                }
            };

            stringCatcher = (rs, col_name) ->
            {
                try {
                    return rs.getString(col_name);
                } catch (SQLException e) {
                    Log.e("SQLError", "未能获取对应列" + e);
                    return "";
                }
            };

            doubleCatcher = (rs, col_name) ->
            {
                try {
                    return rs.getDouble(col_name);
                } catch (SQLException e) {
                    Log.e("SQLError", "未能获取对应列" + e);
                    return -1d;
                }
            };

            timeCatcher = (rs, col_name) ->
            {
                try {
                    return rs.getTimestamp("Transaction_time").toInstant().atZone(ZoneId.of("CST")).toLocalDateTime();
                } catch (SQLException e) {
                    Log.e("SQL", Arrays.toString(e.getStackTrace()));
                    return null;
                }
            };
        }

    }

    //这表示了通用的给值器，表示了对象向数据库值要求的字符串转换的实现
    public static class DBTranser {
        public static IToDatabaseValue<Integer, String> intTransfer;
        public static IToDatabaseValue<Float, String> floatTransfer;
        public static IToDatabaseValue<String, String> stringTransfer;
        public static IToDatabaseValue<Double, String> doubleTransfer;
        public static IToDatabaseValue<LocalDateTime, String> timeTransfer;

        static {
            intTransfer = Object::toString;
            floatTransfer = Object::toString;
            stringTransfer = local_value -> "'" + local_value + "'";
            doubleTransfer = Objects::toString;
            timeTransfer = (time) -> "'" + time.toLocalDate().toString() + "'";
        }
    }
}
