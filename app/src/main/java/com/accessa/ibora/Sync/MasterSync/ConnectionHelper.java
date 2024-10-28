package com.accessa.ibora.Sync.MasterSync;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {



    // Method to establish a connection to the database
    public static Connection getConnection( Context context) throws SQLException {
        // Get SharedPreferences where the DB parameters are stored
        SharedPreferences preferences = context.getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);

        // Retrieve values from SharedPreferences (or use defaults if not set)
        String _user = preferences.getString("_user", null);
        String _pass = preferences.getString("_pass", null);
        String _DB = preferences.getString("_DB", null);
        String _server = preferences.getString("_server", null);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String url = "jdbc:jtds:sqlserver://" + _server + ";databaseName=" + _DB;
            return DriverManager.getConnection(url, _user, _pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

