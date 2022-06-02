package com.example.as.Entity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;

public class UserInfo extends Row// 密码数据表实体类
{
    @Col(order = 0)
    public CanBeRef<Integer> ID;

    @Col(order = 1)
    public CanBeRef<String> name;

    @Col(order = 2)
    public CanBeRef<String> password;// 定义字符串，表示用户密码

    @Col(order = 3)
    public CanBeRef<String> sex;

    @Col(order = 4)
    public CanBeRef<LocalDateTime> birthday;

    @Col(order = 5)
    public CanBeRef<String> email;

    @Col(order = 6)
    public CanBeRef<LocalDateTime> create_date;

    @Col(order = 7)
    public CanBeRef<Integer> isAdmin;

    static {
        initRow(UserInfo.class);
    }

    public UserInfo() {
        tableName = "user_info";
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTranser.intTransfer);
        name = new CanBeRef<>(DBCatcher.stringCatcher, DBTranser.stringTransfer);
        password = new CanBeRef<>(DBCatcher.stringCatcher, DBTranser.stringTransfer);
        sex = new CanBeRef<>(DBCatcher.stringCatcher, DBTranser.stringTransfer);
        birthday = new CanBeRef<>(DBCatcher.timeCatcher, DBTranser.timeTransfer);
        email = new CanBeRef<>(DBCatcher.stringCatcher, DBTranser.stringTransfer);
        create_date = new CanBeRef<>(DBCatcher.timeCatcher, DBTranser.timeTransfer);
        isAdmin = new CanBeRef<>(DBCatcher.intCatcher, DBTranser.intTransfer);

        Bind();
    }

    public UserInfo(ResultSet rs) {
        this();
        setByResultSet(rs);
    }
}
