package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.sql.ResultSet;

public class Account extends Row {
    @Col(order = 0)
    public CanBeRef<Integer> id;

    @Col(order = 1)
    public CanBeRef<String> type;

    @Col(order = 2)
    public CanBeRef<Integer> user_id;

    @Col(order = 3)
    public CanBeRef<Double> balance;

    @Col(order = 4)
    public CanBeRef<String> note;

    static {
        initRow(Account.class);
    }

    public Account() {
        tableName = "account";
        id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        type = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        user_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        balance = new CanBeRef<>(DBCatcher.doubleCatcher, DBTransfer.doubleTransfer);
        note = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        Bind();
    }

    public Account(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }
}