package com.andidj.weighttracker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String ALERT_PHONE = "5551234567";
    private static final float DEFAULT_GOAL_WEIGHT = 150.0f;

    private DBHelper db;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        preferences = getSharedPreferences(
                SmsActivity.PREFS_NAME,
                MODE_PRIVATE
        );

        EditText etWeight = findViewById(R.id.editTextText);
        Button btnLog = findViewById(R.id.button3);
        Button btnHistory = findViewById(R.id.button4);
        Button btnSettings = findViewById(R.id.buttonSms);

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SmsActivity.class))
        );

        btnLog.setOnClickListener(v -> {
            String weightText = etWeight.getText().toString().trim();

            if (weightText.isEmpty()) {
                Toast.makeText(
                        this,
                        "Enter a weight.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            double weight;

            try {
                weight = Double.parseDouble(weightText);
            } catch (NumberFormatException e) {
                Toast.makeText(
                        this,
                        "Invalid weight format.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (weight <= 0) {
                Toast.makeText(
                        this,
                        "Weight must be greater than zero.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            String today = new SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.US
            ).format(new Date());

            long id = db.addWeight(today, weight);

            if (id == -1) {
                Toast.makeText(
                        this,
                        "Could not save weight.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Toast.makeText(
                    this,
                    "Logged weight for " + today,
                    Toast.LENGTH_SHORT
            ).show();

            etWeight.setText("");

            float goalWeight = preferences.getFloat(
                    SmsActivity.KEY_GOAL_WEIGHT,
                    DEFAULT_GOAL_WEIGHT
            );

            if (weight <= goalWeight) {
                handleGoalReached(weight, goalWeight);
            }
        });

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );
    }

    private void handleGoalReached(double currentWeight, float goalWeight) {
        if (hasSmsPermission()) {
            sendGoalSms(currentWeight, goalWeight);

            Toast.makeText(
                    this,
                    "Goal reached! SMS alert sent.",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(
                    this,
                    "Goal reached! SMS not sent because permission was not granted.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private boolean hasSmsPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void sendGoalSms(double currentWeight, float goalWeight) {
        try {
            SmsManager.getDefault().sendTextMessage(
                    ALERT_PHONE,
                    null,
                    "Goal reached! Current weight: "
                            + currentWeight
                            + ". Goal weight: "
                            + goalWeight,
                    null,
                    null
            );
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "SMS failed to send.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}