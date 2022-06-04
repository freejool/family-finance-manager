package com.example.as.Entity;

import com.example.as.database.CanBeRef;
import com.example.as.database.Col;
import com.example.as.database.Row;

import java.sql.ResultSet;
import java.time.LocalDateTime;

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
        ID = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);
        name = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        password = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        sex = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        birthday = new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        email = new CanBeRef<>(DBCatcher.stringCatcher, DBTransfer.stringTransfer);
        create_date = new CanBeRef<>(DBCatcher.timeCatcher, DBTransfer.timeTransfer);
        isAdmin = new CanBeRef<>(DBCatcher.intCatcher, DBTransfer.intTransfer);

        Bind();
    }

    public UserInfo(ResultSet rs) {
        this();
        setByResultSet(rs);
    }
}
