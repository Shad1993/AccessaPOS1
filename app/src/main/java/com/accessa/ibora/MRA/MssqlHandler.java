package com.accessa.ibora.MRA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MssqlHandler {


    private static final String _user = "db_a9c818_test_admin";
    private static final String _pass = "Test1234";
    private static final String _DB = "db_a9c818_test";
    private static final String _server = "SQL8005.site4now.net";

    public static int getCounterValue() {
        int counter = 0;
        Connection conn = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password=" + _pass + ";";
            conn = DriverManager.getConnection(ConnURL);

            // Define your SQL query to retrieve the counter value
            String query = "SELECT counter FROM MRA_Table WHERE type =?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                counter = resultSet.getInt("counter");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return counter;
    }
}

