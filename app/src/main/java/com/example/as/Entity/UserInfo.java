package com.example.as.Entity;

import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;

public class UserInfo// 密码数据表实体类
{
    private int ID;
    private String name;
    private String password;// 定义字符串，表示用户密码
    private String sex;
    private LocalDateTime birthday;
    private String email;
    private LocalDateTime create_date;
    private int isAdmin;

    public UserInfo(ResultSet rs) {
        try {
            this.ID = rs.getInt("ID");
            this.name = rs.getString("name");
            this.password = rs.getString("password");
            this.sex = rs.getString(4);
            this.birthday = rs.getTimestamp("birthday").toInstant().atZone(ZoneId.of("CST")).toLocalDateTime();
            this.email = rs.getString("email");
            this.create_date = rs.getTimestamp("create_date").toInstant().atZone(ZoneId.of("CST")).toLocalDateTime();
            this.isAdmin = rs.getInt("isAdmin");
        } catch (Exception e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        }
    }


    public UserInfo(int ID, String name, String password, String sex, LocalDateTime birthday, @Nullable String email, LocalDateTime create_date, int isAdmin) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.birthday = birthday;
        this.email = email;
        this.create_date = create_date;
        this.isAdmin = isAdmin;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
