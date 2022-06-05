package com.example.as.Entity;

import androidx.annotation.NonNull;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.sql.ResultSet;

public class TransType extends Row {
    @Col(order = 0)
    public CanBeRef<Integer> ID;
    @Col(order = 1)
    public CanBeRef<String> type;
    @Col(order = 2)
    public CanBeRef<String> in_or_out;
    @Col(order = 3)
    public CanBeRef<Integer> user_id;

    static {
        initRow(TransType.class);
    }

    public TransType() {
        super();
        tableName = "trans_type";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        type = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        in_or_out = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        user_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);

        Bind();
    }

    public TransType(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }

    @NonNull
    @Override
    public String toString() {
        return this.user_id + " | " + this.type;
    }
}