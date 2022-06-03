package com.example.as.dao;

import android.util.Log;

import com.example.as.database.CanBeRef;
import com.example.as.database.DatabaseQuery;
import com.example.as.database.IRow;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

//提供基础的增删改查

public class CommonDAO<T extends IRow> {
    private DatabaseQuery db;
    private String last_sql_executed;

    public CommonDAO() throws NullPointerException {

    }

    public String getLastSQLExecuted() {
        return last_sql_executed;
    }

    public void insert(T row)throws SQLException
    {
        try
        {
            Log.i("SQL", row.getSqlValues());
            String sql_to_execute = String.format
                    (
                            "insert into %s %s",
                            row.getTableName(),
                            row.getSqlValues()
                    );

            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed = sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null) {
                throw db.getException();
            }
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
    }

    public void insert(T row,boolean include_or_exclude,CanBeRef<?>... col_params)throws SQLException
    {
        try
        {
            Log.i("SQL", row.getSqlValues(include_or_exclude,new Vector<>(Arrays.asList(col_params))));
            String sql_to_execute = String.format
                    (
                            "insert into %s %s %s",
                            row.getTableName(),
                            row.getSqlColumnNames(include_or_exclude,new Vector<>(Arrays.asList(col_params))),
                            row.getSqlValues(include_or_exclude,new Vector<>(Arrays.asList(col_params)))
                    );

            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed = sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null) {
                throw db.getException();
            }
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
    }

    public void delete(T row, String condition) throws SQLException {
        try {
            Log.i("SQL", row.getSqlValues());
            String sql_to_execute = String.format
                    (
                            "delete from %s %s",
                            row.getTableName(),
                            condition
                    );

            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed = sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null) {
                throw db.getException();
            }
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
    }

//    public void update(T row,String condition)
//    {
//        try
//        {
//            Log.i("SQL", row.getSqlValues()+"更新");
//            String sql_to_execute=String.format
//                    (
//                            "update %s set %s where %s",
//                            row.getTableName(),
//                            condition
//                    );
//
//            db = new DatabaseQuery(sql_to_execute);
//            db.start();
//            db.join();
//            if (db.getException() != null)
//            {
//                throw db.getException();
//            }
//        } catch (InterruptedException | SQLException e)
//        {
//            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
//        }
//    }

    public ResultSet find(T row, String top_condition, String end_condition) throws SQLException {
        try {
            String sql_to_execute = String.format
                    (
                            "select %s from %s %s",
                            top_condition,
                            row.getTableName(),
                            end_condition
                    );
            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed = sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null) {
                throw db.getException();
            }
            return db.getResultSet();
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    public int count(T row) throws SQLException {

        try {
            String sql_to_execute = String.format
                    (
                            "select count(*) as count from %s",
                            row.getTableName()
                    );
            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed = sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null) {
                throw db.getException();
            }
            ResultSet rs = db.getResultSet();
            rs.next();
            return rs.getInt(1);
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }


    public void close() {
        db.close();
    }
}
