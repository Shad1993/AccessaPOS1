package com.accessa.ibora.Sync;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.accessa.ibora.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddProducts extends Activity {

    ConnectionClass connectionClass;
    EditText edtproname, edtprodesc;
    Button btnadd,btnupdate,btndelete;
    ProgressBar pbbar;
    ListView lstpro;
    String proid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproducts);

        connectionClass = new ConnectionClass();
        edtproname = (EditText) findViewById(R.id.edtproname);
        edtprodesc = (EditText) findViewById(R.id.edtprodesc);
        btnadd = (Button) findViewById(R.id.btnadd);
        btnupdate = (Button) findViewById(R.id.btnupdate);
        btndelete = (Button) findViewById(R.id.btndelete);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        lstpro = (ListView) findViewById(R.id.lstproducts);
        proid = "";

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPro addPro = new AddPro();
                addPro.execute("");

            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdatePro updatePro = new UpdatePro();
                updatePro.execute("");

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DeletePro deletePro = new DeletePro();
                deletePro.execute("");

            }
        });


    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();

            String[] from = { "A", "B", "C" };
            int[] views = { R.id.lblproid, R.id.lblproname,R.id.lblprodesc };
            final SimpleAdapter ADA = new SimpleAdapter(AddProducts.this,
                    prolist, R.layout.lsttemplate, from,
                    views);
            lstpro.setAdapter(ADA);


            lstpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    proid = (String) obj.get("A");
                    String proname = (String) obj.get("B");
                    String prodesc = (String) obj.get("C");
                    edtprodesc.setText(prodesc);
                    edtproname.setText(proname);
                    //     qty.setText(qtys);
                }
            });



        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from Producttbl";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("Id"));
                        datanum.put("B", rs.getString("ProName"));
                        datanum.put("C", rs.getString("ProDesc"));
                        prolist.add(datanum);
                    }


                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";

            }
            return z;
        }
    }

    public class AddPro extends AsyncTask<String, String, String> {



        String z = "";
        Boolean isSuccess = false;


        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());
                        String query = "insert into Producttbl (ProName,ProDesc,OnDate) values ('" + proname + "','" + prodesc + "','" + dates + "')";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Added Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
    public class UpdatePro extends AsyncTask<String, String, String> {



        String z = "";
        Boolean isSuccess = false;


        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());

                        String query = "Update Producttbl set ProName='"+proname+"',ProDesc='"+prodesc+"' , OnDate='"+dates+"' where Id="+proid;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";

                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
    public class DeletePro extends AsyncTask<String, String, String> {



        String z = "";
        Boolean isSuccess = false;


        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());

                        String query = "delete from Producttbl where Id="+proid;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Deleted Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
}
