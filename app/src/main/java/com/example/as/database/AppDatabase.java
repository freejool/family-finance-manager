package com.example.as.database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class AppDatabase {
    // Connect to your database.
    // Replace server name, username, and password with your credentials
    private static Connection INSTANCE;
    private static final Object sLock = new Object();
    private static final String connectionUrl ="jdbc:jtds:sqlserver://1.tragichank.asia:51433;"
            + "DatabaseName=family_fin_manager;"
            + "user=sa;"
            + "password=1234Qwerty;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    public static Connection getInstance() {
        synchronized (sLock) {
            if (INSTANCE == null) {
                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    INSTANCE = DriverManager.getConnection(connectionUrl);
                    Log.i("database", "connect succeed");
                }
                // Handle any errors that may have occurred.
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e("database", "connect failed");
                }
            }
            return INSTANCE;
        }
    }
}