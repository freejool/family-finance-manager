package com.example.as.database;

import java.sql.ResultSet;

/**
 * 表示将一个值转换为另一个类型值的实现
 * 要转换为数据库接受的类型，{@link C}通常是{@link String}类型。
 * @param <T> 接受的类型
 * @param <C> 转出的类型
 */
public interface IToDatabaseValue<T,C>
{
    C run(T local_value);
}
