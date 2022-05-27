package com.example.as.database;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DatabaseQuery extends Thread {
    private Connection connection;
    private Statement statement;
    private final String sql;
    private ResultSet resultSet;
    private SQLException exception = null;

    public SQLException getException() {
        return exception;
    }

    public DatabaseQuery(String sql) {
        this.sql = sql;
    }


    @Override
    public void run() {
        ResultSet resultSet;
        try {
            //获取连接的全局实例
            connection = AppDatabase.getInstance();
            statement = connection.createStatement();
            statement.execute(sql);
            this.resultSet = statement.getResultSet();
        } catch (SQLException e) {
            this.exception = e;
        }
    }


    public ResultSet getResultSet() {
        return resultSet;
    }

    public void close()
    {
        try
        {
            statement.close();
            resultSet.close();
        }catch (SQLException e)
        {
            Log.e("SQL","语句和结果集关闭失败");
        }
    }
}
