package com.accessa.ibora;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

    public class LoadingActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Retrieve extras (e.g., cashReturn) from intent
            double cashReturn = getIntent().getDoubleExtra("cash_return_key", 0.0);

            // Immediately navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("cash_return_key", cashReturn);
            startActivity(intent);
            finish(); // Finish LoadingActivity to prevent going back
        }
    }


