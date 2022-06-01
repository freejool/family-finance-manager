package com.example.as.database;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * 表示一个能够保存数据库某表某行数据的类型，
 * 它还要实现一些基本的功能
 */
public interface IRow
{
    public String getTableName();
    public void setByResultSet(ResultSet resultSet);
    public String getSqlValues();

    /**
     * 生成数据的字典
     * @return 返回生成的数据的字典，它是一份原始数据的拷贝，因此和原始数据没有修改上的关系
     */
    public HashMap<String,?> genDictData();
}
