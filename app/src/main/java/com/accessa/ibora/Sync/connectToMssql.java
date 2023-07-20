package com.accessa.ibora.Sync;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.accessa.ibora.R;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectToMssql extends AppCompatActivity {

    private static String ip = "SQL8005.site4now.net";
    private static int port = 1433;
    private static String database = "db_a9c818_test";
    private static String username = "db_a9c818_test_admin";
    private static String password = "Test1234";
    private static String url = "jdbc:sqlserver://" + ip + ":1433;DatabaseName=" + database + ";encrypt=true;trustServerCertificate=true;";


    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }

    public void start(View view) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            DriverManager.registerDriver(new SQLServerDriver());
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null && !connection.isClosed()) {
                Toast.makeText(this, "Connected to MSSQL Server", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ConnectionError", "Connection failed: " + e.getMessage());
        }
    }
}
