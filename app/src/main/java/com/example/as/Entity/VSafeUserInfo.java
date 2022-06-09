package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.sql.ResultSet;
import java.time.LocalDateTime;

public class VSafeUserInfo extends Row {
    @Col(order = 0)
    public CanBeRef<Integer> ID;
    @Col(order = 1)
    public CanBeRef<String> name;
    @Col(order = 2)
    public CanBeRef<String> sex;
    @Col(order = 3)
    public CanBeRef<String> email;
    @Col(order = 4)
    public CanBeRef<LocalDateTime> create_date;
    @Col(order = 5)
    public CanBeRef<Integer> isAdmin;

    static {
        initRow(VSafeUserInfo.class);
    }

    public VSafeUserInfo() {
        tableName = "v_safe_user_info";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        name = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        sex = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        email = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        create_date = new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        isAdmin = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);

        Bind();
    }

    public VSafeUserInfo(ResultSet rs) {
        this();
        setByResultSet(rs);
    }

    @Override
    public String toString() {
        return ID + " | " +
                name + " | " +
                sex + " | " +
                email + " | " +
                create_date + " | " +
                isAdmin;
    }
}
