package com.example.as.dao;

import android.util.Log;

import com.example.as.database.DatabaseQuery;
import com.example.as.database.IRow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

//提供基础的增删改查

public class CommonDAO<T extends IRow>
{
    private DatabaseQuery db;
    private String last_sql_executed;

    public CommonDAO() throws NullPointerException
    {
    }

    public String getLastSQLExecuted(){return last_sql_executed;}

    public void insert(T row)throws SQLException
    {
        try
        {
            Log.i("SQL", row.getSqlValues());
            String sql_to_execute=String.format
                    (
                        "insert into %s %s",
                        row.getTableName(),
                        row.getSqlValues()
                    );

            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed=sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null)
            {
                throw db.getException();
            }
        } catch (InterruptedException e)
        {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
    }

    public void delete(T row,String condition) throws SQLException
    {
        try
        {
            Log.i("SQL", row.getSqlValues());
            String sql_to_execute=String.format
                    (
                            "delete from %s %s",
                            row.getTableName(),
                            condition
                    );

            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed=sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null)
            {
                throw db.getException();
            }
        } catch (InterruptedException e)
        {
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

    public ResultSet find(T row,String top_condition, String end_condition)
    {
        try
        {
            String sql_to_execute=String.format
                    (
                            "select %s * from %s %s;",
                            top_condition,
                            row.getTableName(),
                            end_condition
                    );
            db = new DatabaseQuery(sql_to_execute);
            last_sql_executed=sql_to_execute;
            db.start();
            db.join();
            if (db.getException() != null)
            {
                throw db.getException();
            }
            return db.getResultSet();
        } catch (InterruptedException | SQLException e)
        {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
