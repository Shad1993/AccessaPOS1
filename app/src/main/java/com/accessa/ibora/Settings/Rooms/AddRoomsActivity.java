package com.accessa.ibora.Settings.Rooms;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRoomsActivity extends AppCompatActivity {

    private EditText editTextName;

    private EditText editTextOtherNames;

    private Button buttonSave;

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        databaseHelper = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        editTextName = findViewById(R.id.etName);
        editTextOtherNames = findViewById(R.id.counter);

        buttonSave = findViewById(R.id.btnAddRoom);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRooms();
            }
        });
    }

    private void saveRooms() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String name = editTextName.getText().toString();
        String counter = editTextOtherNames.getText().toString();

        // Check if counter is a valid integer
        try {
            int counterInt = Integer.parseInt(counter);

            // If counter is valid, proceed with saving
            Rooms newRooms = new Rooms(name, String.valueOf(counterInt), cashorId, lastmodified, lastmodified);

            boolean success = databaseHelper.addRoom(newRooms);

            if (success) {
                // Redirect to the Product activity
                returnHome();
                finish();
            } else {
                Toast.makeText(this, "Failed To insert Room", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            // Show error if counter is not a valid integer
            editTextOtherNames.setError("Please enter a valid integer for Counter");
        }
    }


    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Rooms_fragment");
        startActivity(home_intent1);
    }
}
