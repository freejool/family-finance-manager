package com.example.as.dao;

import android.util.Log;

import com.example.as.database.DatabaseQuery;
import com.example.as.Entity.UserInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginDAO {
    private DatabaseQuery db;

    public LoginDAO() {
    }

    public void addUser(UserInfo userInfo) throws SQLException {
        try {
            Log.i("SQL", userInfo.getSqlValues());
            db = new DatabaseQuery(
                    "insert into user_info " + userInfo.getSqlValues());
            db.start();
            db.join();
            if (db.getException() != null) {
                throw db.getException();
            }
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
    }


    public UserInfo findUser(String password) {
        UserInfo user = null;
        ResultSet res;
        try {
            db = new DatabaseQuery(
                    "select * from user_info where password=" + password);
            db.start();
            db.join();
            res = db.getResultSet();
            if (res.next()) {
                user = new UserInfo(res);
            }
            res.close();

        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return user;
    }

    public UserInfo findUser(String username, String password) {
        UserInfo user = null;
        ResultSet res;
        try {
            String sql =
                    "select * from user_info where name= '" + username + "' " +
                            "and password='" + password + "'";
            db = new DatabaseQuery(sql);
            db.start();
            db.join();
            res = db.getResultSet();
            if (res.next()) {
                user = new UserInfo(res);
            }
            res.close();

        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return user;
    }

    public int getPasswordCount() {
        try {
            db = new DatabaseQuery("select count(*) from user_info");
            db.start();
            db.join();
            ResultSet res = db.getResultSet();
            if (res.next()) {// 判断Cursor中是否有数据
                return res.getInt(1);
            }
            res.close();// 关闭游标
        } catch (Exception e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        }
        return 0;
    }
}