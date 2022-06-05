package com.example.as.dao;

import android.util.Log;

import com.example.as.Entity.Transactions;
import com.example.as.database.DatabaseQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPayDAO {
    private DatabaseQuery db;

    public AddPayDAO() {
    }

    public void addPay(Transactions transactions) throws SQLException {
        try {
            Log.i("SQL", transactions.getSqlValues());
            db = new DatabaseQuery(
                    "insert into transactions " + transactions.getSqlValues());
            db.start();
            db.join();
            if (db.getException() != null) {
                throw db.getException();
            }
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
    }

    public List<String> findType() throws SQLException {
        List<String> returnList = new ArrayList<>();
        ResultSet res;
        String type;
        try {

            db = new DatabaseQuery(
                    "select type from trans_type  ");
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next()) {
                type = res.getString("type");
                returnList.add(type);
            }
            res.close();
        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return returnList;
    }
    public List<String> findTypefromtransactions() throws SQLException {
        List<String> returnList = new ArrayList<>();
        ResultSet res;
        String type;
        try {

            db = new DatabaseQuery(
                    "select distinct type from transactions  where in_or_out = '支出'");
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next()) {
                type = res.getString("type");
                returnList.add(type);
            }
            res.close();
        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return returnList;
    }
    public Map<String,Float> FindTypeMoney() throws SQLException {
//        List<String> returnList = new ArrayList<>();
        ResultSet res;
        String type;
        Float money;
        Map<String,Float> map=new HashMap<String,Float>();	//创建一个Map对象
        try {

            db = new DatabaseQuery(
                    "select type,sum(amount) as amount from transactions where in_or_out ='支出' group by type");
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next()) {
                type = res.getString("type");
                money = res.getFloat("amount");
                map.put(type,money);
            }
            res.close();
        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return map;
    }
    public List<String> finduserfromtransactions() throws SQLException {
        List<String> returnList = new ArrayList<>();
        ResultSet res;
        String username;
        try {

            db = new DatabaseQuery(
                    "select distinct name from transactions,user_info  where in_or_out = '支出' and user_info.ID=transactions.user_id");
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next()) {
                username = res.getString("name");
                returnList.add(username);
            }
            res.close();
        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return returnList;
    }
    public List<String> findTypefromtransactionsspan(String UserName, String starttime,String overtime) throws SQLException {
        List<String> returnList = new ArrayList<>();
        ResultSet res;
        String type;
        try {

            db = new DatabaseQuery(
                    "select distinct type " +
                            "from transactions,user_info  " +
                            "where in_or_out = '支出' and name='" +
                            UserName +
                            "' and transaction_time between '" +
                            starttime +
                            "' and '" +
                            overtime +
                            "'");
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next()) {
                type = res.getString("type");
                returnList.add(type);
            }
            res.close();
        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return returnList;
    }
    public Map<String,Float> FindUserMoney() throws SQLException {
//        List<String> returnList = new ArrayList<>();
        ResultSet res;
        String type;
        Float money;
        Map<String,Float> map=new HashMap<String,Float>();	//创建一个Map对象
        try {

            db = new DatabaseQuery(
                    "select name,sum(amount) as amount from transactions,user_info where in_or_out ='支出' and transactions.user_id=user_info.ID  group by name");
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next()) {
                type = res.getString("name");
                money = res.getFloat("amount");
                map.put(type,money);
            }
            res.close();
        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return map;
    }
    public Map<String,Float> FindSpanTypeMoney(String UserName, String starttime, String overtime) throws SQLException {
//        List<String> returnList = new ArrayList<>();
        ResultSet res;
        String type;
        Float money;
        Map<String,Float> map=new HashMap<String,Float>();	//创建一个Map对象
        try {

            db = new DatabaseQuery(
                    "select type,sum(amount) as amount " +
                            "from transactions,user_info " +
                            "where in_or_out ='支出' and name='" +UserName+
                            "' and transaction_time between '" +
                            starttime +
                            "' " +
                            "and '" +
                             overtime +
                            "' group by type");
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next()) {
                type = res.getString("type");
                money = res.getFloat("amount");
                map.put(type,money);
            }
            res.close();
        } catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return map;
    }
}