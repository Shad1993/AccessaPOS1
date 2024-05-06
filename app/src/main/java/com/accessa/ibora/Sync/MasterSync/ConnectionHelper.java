package com.accessa.ibora.Sync.MasterSync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    // Database connection parameters
    private static final String _user = "sa";
    private static final String _pass = "Logi2131";
    private static final String _DB = "IboraPOS";
    private static final String _server = "192.168.1.89";

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
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

