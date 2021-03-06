package com.example.as.database;


import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
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
 * (2)在撰写构造函数时，先为{@link Row#tableName}赋表名的值，然后
 * 初始化表示数据库列的成员变量并为其设置抓取器和转换器。最终使用{@link Row#Bind(Class<?>)}方法进行绑定。</p>
 *
 * <p>此类已实现基本的从{@link ResultSet}抓取到A的方法{@link Row#setByResultSet(ResultSet)}。
 * 以及从实例转换为数据库所需的values(...)字符串的方法{@link Row#getSqlValues()}。其它功能等待被实现或继承后实现。</p>
 */
public class Row implements IRow {
    //进行数据库更新时会更新的字段
    public Vector<String> update_list;

    public HashMap<String, CanBeRef<?>> str2ObjDict;
    public HashMap<CanBeRef<?>, String> obj2StrDict;
    public Vector<String> field_columnNameList=new Vector<>();

    protected static HashMap<String, Field> str2FldDict=new HashMap<>();
    protected static HashMap<Field, String> fld2StrDict=new HashMap<>();
    protected static Vector<String> columnNameList=new Vector<>();
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
//        str2FldDict = new HashMap<>();
//        fld2StrDict = new HashMap<>();
//        columnNameList = new Vector<>();
        HashMap<String, Field> static_str2FldDict = new HashMap<>();
        HashMap<Field, String> static_fld2StrDict = new HashMap<>();
        Vector<String> static_columnNameList = new Vector<>();

        Field[] declared_fields = clazz.getDeclaredFields();
//        try
//        {
//            for (Field field : declared_fields)
//            {
//                field.setAccessible(true);
//                if(field.getName()=="str2FldDict"&& Modifier.isStatic(field.getModifiers()))
//                {
//                    static_str2FldDict= (HashMap<String, Field>) field.get(null);
//                }
//                if(field.getName()=="fld2StrDict"&& Modifier.isStatic(field.getModifiers()))
//                {
//                    static_fld2StrDict= (HashMap<Field, String>) field.get(null);
//                }
//                if(field.getName()=="columnNameList"&& Modifier.isStatic(field.getModifiers()))
//                {
//                    static_columnNameList= (Vector<String>) field.get(null);
//                }
//            }
//        }
//        catch (IllegalAccessException e)
//        {
//            Log.e("Row","寻找类型的静态变量失败");
//        }

        for (Field field : declared_fields)
        {
            if (field.isAnnotationPresent(Col.class)) {
                Col current_col = field.getAnnotation(Col.class);
                if (current_col.col_name().isEmpty()) {
                    static_str2FldDict.put(field.getName(), field);
                    static_fld2StrDict.put(field, field.getName());
                    static_columnNameList.add(field.getName());
                } else {
                    static_str2FldDict.put(current_col.col_name(), field);
                    static_fld2StrDict.put(field, current_col.col_name());
                    static_columnNameList.add(current_col.col_name());
                }
            }
        }

        static_columnNameList.sort(Comparator.comparingInt(
                m ->
                {
                    try {
                        return static_str2FldDict.get(m).getAnnotation(Col.class).order();
                    } catch (NullPointerException e) {
                        Log.e("Row", "对列名排序时出现问题");
                    }
                    return -1;
                }
        ));

        try
        {
            for (Field field : declared_fields)
            {
                field.setAccessible(true);
                if(field.getName()=="str2FldDict"&& Modifier.isStatic(field.getModifiers()))
                {
                    field.set(null,static_str2FldDict);
                }
                if(field.getName()=="fld2StrDict"&& Modifier.isStatic(field.getModifiers()))
                {
                    field.set(null,static_fld2StrDict);
                }
                if(field.getName()=="columnNameList"&& Modifier.isStatic(field.getModifiers()))
                {
                    Log.i("Row",static_columnNameList.toString());
                    field.set(null,static_columnNameList);
                }
            }
        } catch (IllegalAccessException e) {
            Log.e("Row","设置类型的静态变量失败");
            e.printStackTrace();
        }
    }

    public Row() {
        str2ObjDict = new HashMap<>();
        obj2StrDict = new HashMap<>();
    }

    //将变量名称与数据库列名称绑定
    public void Bind(Class<?> clazz) {
        HashMap<String, Field> static_str2FldDict = null;
        HashMap<Field, String> static_fld2StrDict =null;
        Vector<String> static_columnNameList =null;

        Field[] declared_fields = clazz.getDeclaredFields();
        try
        {
            for (Field field : declared_fields)
            {
                field.setAccessible(true);
                if(field.getName()=="str2FldDict"&& Modifier.isStatic(field.getModifiers()))
                {
                    static_str2FldDict= (HashMap<String, Field>) field.get(null);
                }
                if(field.getName()=="fld2StrDict"&& Modifier.isStatic(field.getModifiers()))
                {
                    static_fld2StrDict= (HashMap<Field, String>) field.get(null);
                }
                if(field.getName()=="columnNameList"&& Modifier.isStatic(field.getModifiers()))
                {
                    static_columnNameList= (Vector<String>) field.get(null);
                }
            }
        }
        catch (IllegalAccessException e)
        {
            Log.e("Row","寻找类型的静态变量失败");
        }


        for (Map.Entry<String, Field> entry : static_str2FldDict.entrySet()) {
            try {
                //Log.e("SQL","加入了一个对象"+entry.getKey()+entry.getValue().getType());
                str2ObjDict.put(entry.getKey(), (CanBeRef<?>) entry.getValue().get(this));
                obj2StrDict.put((CanBeRef<?>) entry.getValue().get(this), entry.getKey());
            } catch (IllegalAccessException e) {
            }
        }
        field_columnNameList=static_columnNameList;
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
        for (String column_name : field_columnNameList) {
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
    public String getSqlColumnNames(boolean include_or_exclude, Vector<CanBeRef<?>> col_params) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        boolean any_append = false;
        if (include_or_exclude) {
            if (col_params != null) {
                for (CanBeRef<?> obj : col_params) {
                    if (!obj2StrDict.containsKey(obj)) {
                        continue;
                    }
                    builder.append(obj2StrDict.get(obj))
                            .append(',');
                    any_append = true;
                }
            }
        } else {
            HashSet<CanBeRef<?>> obj_set;
            if (col_params != null) {
                obj_set = new HashSet<>(col_params);
            } else {
                obj_set = new HashSet<>();
            }
            for (String column_name : field_columnNameList) {
                CanBeRef<?> obj = Objects.requireNonNull(str2ObjDict.get(column_name));
                if (obj_set.contains(obj))
                    continue;
                builder.append(column_name)
                        .append(',');
                any_append = true;
            }
        }
        if (any_append)
            builder.deleteCharAt(builder.length() - 1);
        builder.append(')');
        Log.i("SQL",builder.toString());
        return builder.toString();
    }


    /**
     * 获取数据库列的值组成的形如"values(2,4,...)"的字符串
     *
     * @param include_or_exclude 包含或排除，此值为true时表示包含，
     *                           则该函数会尝试将参数列表的参数按列表顺序生成此字符串，忽略参数中不属于此实例成员的对象。
     *                           此值为false时表示排除，
     *                           该函数会按成员的注解中顺序生成字符串，忽略参数列表中提到的此实例的成员。
     * @param col_params         参数列表
     * @return 形如"values(2,4,...)"的字符串
     */
    @Override
    public String getSqlValues(boolean include_or_exclude, Vector<CanBeRef<?>> col_params) {
        StringBuilder builder = new StringBuilder();
        builder.append("values(");
        boolean any_append = false;
        if (include_or_exclude) {
            if (col_params != null) {
                for (CanBeRef<?> obj : col_params) {
                    if (!obj2StrDict.containsKey(obj)) {
                        continue;
                    }
                    builder.append(obj.getSqlValues())
                            .append(',');
                    any_append = true;
                }
            }
        } else {

            HashSet<CanBeRef<?>> obj_set;
            if (col_params != null) {
                obj_set = new HashSet<>(col_params);
            } else {
                obj_set = new HashSet<>();
            }
            for (String column_name : field_columnNameList) {
                CanBeRef<?> obj = Objects.requireNonNull(str2ObjDict.get(column_name));
                if (obj_set.contains(obj))
                    continue;
                builder.append(Objects.requireNonNull(str2ObjDict.get(column_name)).
                        getSqlValues())
                        .append(',');
                any_append = true;
            }
        }
        if (any_append)
            builder.deleteCharAt(builder.length() - 1);
        builder.append(')');
        Log.i("SQL",builder.toString());
        return builder.toString();
    }

    @Override
    public String getSqlValues() {
        return getSqlValues(false, null);
    }

    public HashMap<String, Object> genDictData() {
        HashMap<String, Object> genedDict = new HashMap<>();
        for (Map.Entry<String, CanBeRef<?>> entry : str2ObjDict.entrySet()) {
            genedDict.put(entry.getKey(), entry.getValue().value);
        }
        return genedDict;
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
                    Timestamp stamp=rs.getTimestamp(col_name);
                    if(stamp==null)
                        return null;
                    else
                        return stamp.toInstant().atZone(ZoneId.of("CST")).toLocalDateTime();
                } catch (SQLException e) {
                    Log.e("SQL", Arrays.toString(e.getStackTrace()));
                    return null;
                }
            };
        }

    }

    //这表示了通用的给值器，表示了对象向数据库值要求的字符串转换的实现
    public static class DBTransfer {
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
