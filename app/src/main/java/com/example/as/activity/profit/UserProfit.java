package com.example.as.activity.profit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfit {
    public Double origin;
    public Double bonus;
    public Integer duration;

    UserProfit(ResultSet rs) throws SQLException {
        this.origin = rs.getDouble("origin");
        this.bonus = rs.getDouble("bonus");
        this.duration = rs.getInt("duration");
    }
    UserProfit(Double origin,Double bonus,Integer duration){
        this.origin=origin;
        this.bonus=bonus;
        this.duration=duration;
    }


}
