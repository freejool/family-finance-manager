package com.example.as.database;

import java.sql.ResultSet;

//怎样从一个Resultset中获取数据
public interface IFromResultset<T>
{
    public T run(ResultSet rs,String col_name);
}
