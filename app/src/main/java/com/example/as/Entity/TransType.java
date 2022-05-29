package com.example.as.Entity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.IFromResultset;
import com.example.as.database.IToDatabaseValue;
import com.example.as.database.Row;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TransType extends Row {
    @Col(order = 0)
    private final CanBeRef<Integer> ID;
    @Col(order = 1)
    private final CanBeRef<String> type;
    @Col(order = 2)
    private final CanBeRef<Integer> user_id;

    static {
        initRow(TransType.class);
    }

    public TransType() {
        super();
        table_name = "trans_type";
        ID = new CanBeRef<>(DBCatcher.int_catcher, DBTranser.int_transer);
        type = new CanBeRef<>(DBCatcher.string_catcher, DBTranser.string_transer);
        user_id = new CanBeRef<>(DBCatcher.int_catcher, DBTranser.int_transer);

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