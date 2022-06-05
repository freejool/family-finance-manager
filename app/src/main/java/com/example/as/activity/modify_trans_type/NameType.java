package com.example.as.activity.modify_trans_type;

import androidx.annotation.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

class NameType {
    public String name;
    public String type;
    public String in_or_out;
    public int ID;

    NameType(ResultSet rs) throws SQLException {
        this.name = rs.getString("name");
        this.type = rs.getString("type");
        this.in_or_out = rs.getString("in_or_out");
        this.ID = rs.getInt("ID");
    }

    @NonNull
    @Override
    public String toString() {
        return name + " | " + type + " | " + in_or_out;
    }
}
