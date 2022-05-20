package com.example.as.database;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

public class DatabaseService extends Thread {
    private Connection connection;
    private Statement statement;
    private String sql;
    private ResultSet resultSet;

    public DatabaseService(String sql) {
        this.sql = sql;
    }


    @Override
    public void run() {
        ResultSet resultSet;
        try {
            connection = AppDatabase.getInstance();
            statement = connection.createStatement();
            statement.execute(sql);
           this.resultSet = statement.getResultSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ResultSet getResultSet() {
        return resultSet;
    }
}
