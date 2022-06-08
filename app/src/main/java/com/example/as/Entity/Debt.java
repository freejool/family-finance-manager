package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.sql.ResultSet;
import java.time.LocalDateTime;

public class Debt extends Row {
    @Col(order = 0)
    public CanBeRef<Integer> ID;
    @Col(order = 1)
    public CanBeRef<String> type;
    @Col(order = 2)
    public CanBeRef<Double> amount;
    @Col(order = 3)
    public CanBeRef<LocalDateTime> time;
    @Col(order = 4)
    public CanBeRef<Double> passed_amount;
    @Col(order = 5)
    public CanBeRef<LocalDateTime> except_time;
    @Col(order = 6)
    public CanBeRef<String> note;
    @Col(order = 7)
    public CanBeRef<String> state;
    @Col(order = 8)
    public CanBeRef<Integer> user_id;

    static {
        initRow(Debt.class);
    }

    public Debt() {
        super();
        tableName = "debt";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        type = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        amount = new CanBeRef<>(DBCatcher.doubleCatcher, DBTransfer.doubleTransfer);
        time = new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        passed_amount = new CanBeRef<>(DBCatcher.doubleCatcher, DBTransfer.doubleTransfer);
        except_time = new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        note = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        state = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        user_id = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);

        Bind();
    }

    public Debt(ResultSet resultSet) {
        this();
        setByResultSet(resultSet);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("").append(type.value);
        sb.append(" | ").append(amount.value);
        sb.append(" | ").append(passed_amount.value);
        sb.append(" | ").append(except_time.value);
        sb.append(" | ").append(state.value);
        return sb.toString();
    }
}