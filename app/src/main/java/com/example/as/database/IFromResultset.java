package com.example.as.database;

import java.sql.ResultSet;

/**
 * 表示怎样从{@link ResultSet}中抓取数据到{@link Row}的子类成员。即从结果集转换为成员变量的实现。
 * @param <T>
 */
public interface IFromResultset<T>
{
    public T run(ResultSet rs,String col_name);
}
