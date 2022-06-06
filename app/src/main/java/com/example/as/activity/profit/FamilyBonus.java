package com.example.as.activity.profit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class FamilyBonus {
    public String name;
    public double bonus;
    public String type;

    FamilyBonus() {
    }

    FamilyBonus(ResultSet rs) throws SQLException {
        name = rs.getString("name");
        bonus = rs.getDouble("bonus");
        type = rs.getString("type");
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s | %.2f | %s", name, bonus, type);
    }
}
