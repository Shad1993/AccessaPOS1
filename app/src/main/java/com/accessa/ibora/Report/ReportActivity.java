package com.accessa.ibora.Report;

import static com.accessa.ibora.Constants.DB_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TOTAL_PRICE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private TextView dailyAmountTextView;
    private TextView weeklyAmountTextView;
    private TextView monthlyAmountTextView;
    public static final String TRANSACTION_TABLE_NAME = "Transactions";
    public static final String TRANSACTION_TOTAL_PRICE = "TotalPrice";
    public static final String TRANSACTION_DATE = "TransactionDate";

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dailyAmountTextView = findViewById(R.id.dailyAmountTextView);
        weeklyAmountTextView = findViewById(R.id.weeklyAmountTextView);
        monthlyAmountTextView = findViewById(R.id.monthlyAmountTextView);

        // Open your SQLite database
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // Get current date
        Date currentDate = new Date();

        // Calculate daily amount
        double dailyAmount = getDailyAmount(currentDate);
        dailyAmountTextView.setText(formatAmount(dailyAmount));

        // Calculate weekly amount
        double weeklyAmount = getWeeklyAmount(currentDate);
        weeklyAmountTextView.setText(formatAmount(weeklyAmount));

        // Calculate monthly amount
        double monthlyAmount = getMonthlyAmount(currentDate);
        monthlyAmountTextView.setText(formatAmount(monthlyAmount));
    }

    private double getDailyAmount(Date date) {
        // Calculate the start and end date of the current day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        // Query the database to get the daily amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + TOTAL_PRICE + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_DATE + " >= ? AND " + TRANSACTION_DATE + " < ?" ,new String[]{formatDateTime(startDate), formatDateTime(endDate)});
        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;
    }

    private double getWeeklyAmount(Date date) {
        // Calculate the start and end date of the current week
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        // Query the database to get the weekly amount

        Cursor cursor = database.rawQuery("SELECT SUM(" + TOTAL_PRICE + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_DATE + " >= ? AND " + TRANSACTION_DATE + " < ?", new String[]{formatDateTime(startDate), formatDateTime(endDate)});


        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;
    }

    private double getMonthlyAmount(Date date) {
        // Calculate the start and end date of the current month
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();

        // Query the database to get the monthly amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + TOTAL_PRICE + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_DATE + " >= ? AND " + TRANSACTION_DATE + " < ?",new String[]{formatDateTime(startDate), formatDateTime(endDate)});
        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;
    }

    private String formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(amount);
    }
    private String formatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        if (database != null) {
            database.close();
        }
    }
}

