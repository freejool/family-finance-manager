package com.example.as.database;

import java.sql.ResultSet;

public interface IRow
{
    public String getTableName();
    public void setByResultSet(ResultSet resultSet);
    public String getSqlValues();
}
