package com.example.as.database;

import java.sql.ResultSet;

public interface IRow
{
    String getTableName();
    void setByResultSet(ResultSet resultSet);
    String getSqlValues();
}
