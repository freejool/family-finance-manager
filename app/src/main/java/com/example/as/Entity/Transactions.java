package com.example.as.Entity;

import android.util.Log;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

//injsdfk
public class Transactions //收支记录表实体类
{
    private int ID;
    private int user_id;
    private String type;
    private float amount;
    private String in_or_out;
    private LocalDateTime Transaction_time;
    private String note;

    public Transactions() {

    }

    public Transactions(ResultSet rs) {
        try {
            this.ID = rs.getInt("ID");
            this.user_id = rs.getInt("user_id");
            this.type = rs.getString("type");
            this.amount = rs.getFloat("amount");
            this.in_or_out = rs.getString("in_or_out");
            this.Transaction_time = rs.getTimestamp("Transaction_time").toInstant().atZone(ZoneId.of("CST")).toLocalDateTime();
            this.note = rs.getString("note");
        } catch (Exception e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        }
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getInOrOut() {
        return in_or_out;
    }

    public void setInOrOut(String in_or_out) {
        this.in_or_out = in_or_out;
    }

    public LocalDateTime getTransactionTime() {
        return Transaction_time;
    }

    public void setTransactionTime(LocalDateTime Transaction_time) {
        this.Transaction_time = Transaction_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "ID=" + ID +
                ", user_id=" + user_id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", in_or_out='" + in_or_out + '\'' +
                ", Transaction_time=" + Transaction_time +
                ", note='" + note + '\'' +
                '}';
    }

    public String getSqlValues() {
        return "values(" +
                user_id + "," +
                "'" + type + "'" + "," + amount + "," + "'" + in_or_out + "'" + "," +
                "'" + Transaction_time.toLocalDate() + "'" + "," +
                (note.equals("") ? "null" : "'" + note + "'") +
                ")";
    }
}