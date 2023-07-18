package com.accessa.ibora.Sync;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.accessa.ibora.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectToMssql extends AppCompatActivity {

    private static String ip = "192.168.1.97";// this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc
    private static String port = "1433";// the port sql server runs on
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";// the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql
    private static String database = "TPCentralDB";// the data base name
    private static String username = "reshad";// the user name
    private static String password = "reshad1234";// the password
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database; // the connection url string

    private Connection connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

    }

    public void start(View view) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Connected no", Toast.LENGTH_SHORT).show();
        }
    }
}