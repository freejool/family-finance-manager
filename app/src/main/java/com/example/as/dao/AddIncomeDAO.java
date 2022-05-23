package com.example.as.dao;

import android.util.Log;

import com.example.as.Entity.Transactions;
import com.example.as.database.DatabaseQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddIncomeDAO {
    private DatabaseQuery db;

    public AddIncomeDAO(){

    }
    public void addincome(Transactions transactions)throws SQLException {
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
    public  List<String> findtype () throws SQLException{
        List<String> rerurnList = new ArrayList<>();
        ResultSet res;
        String type;
        try {

            db = new DatabaseQuery(
                    "select type from trans_type where in_or_out ='收入' " );
            db.start();
            db.join();
            res = db.getResultSet();
            while (res.next())
            {
                type=res.getString("type");
                rerurnList.add(type);
            }
            res.close();
        }  catch (SQLException e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            Log.e("ThreadError", Arrays.toString(e.getStackTrace()));
        }
        return rerurnList;
    }
}