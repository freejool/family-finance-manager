package com.example.as.database;

import java.sql.ResultSet;
import java.util.Vector;

/**
 * 表示一个能够保存数据库某表某行数据的类型，
 * 它还要实现一些基本的功能
 */
public interface IRow
{
    public String getTableName();
    public void setByResultSet(ResultSet resultSet);
    public String getSqlValues();
    public String getSqlValues(boolean include_or_exclude, Vector<CanBeRef<?>> col_params);
    public String getSqlColumnNames(boolean include_or_exclude,Vector<CanBeRef<?>> col_params);
}
