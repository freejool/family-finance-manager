package com.example.as.database;

import java.sql.ResultSet;

//怎样从Java数据转为数据库数据
public interface IToDatabaseValue<T,C>
{
    C run(T local_value);
}
