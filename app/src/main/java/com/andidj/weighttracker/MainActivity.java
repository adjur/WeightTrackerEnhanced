package com.andidj.weighttracker;

import android.Manifest;
import android.content.Intent;
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

    private DBHelper db;

    private static final double GOAL_WEIGHT = 150.0;
    private static final String ALERT_PHONE = "5551234567";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        EditText etWeight = findViewById(R.id.editTextText);
        Button btnLog = findViewById(R.id.button3);
        Button btnHistory = findViewById(R.id.button4);

        Button btnSms = findViewById(R.id.buttonSms);
        if (btnSms != null) {
            btnSms.setOnClickListener(v ->
                    startActivity(new Intent(this, SmsActivity.class))
            );
        }

        btnLog.setOnClickListener(v -> {
            String wStr = etWeight.getText().toString().trim();
            if (wStr.isEmpty()) {
                Toast.makeText(this, "Enter a weight.", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight;
            try {
                weight = Double.parseDouble(wStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid weight format.", Toast.LENGTH_SHORT).show();
                return;
            }

            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
            long id = db.addWeight(today, weight);

            if (id != -1) {
                Toast.makeText(this, "Logged weight for " + today, Toast.LENGTH_SHORT).show();
                etWeight.setText("");

                if (weight <= GOAL_WEIGHT) {
                    if (hasSmsPermission()) {
                        sendGoalSms(weight);
                        Toast.makeText(this, "Goal reached! SMS alert sent.", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(this, "Goal reached! SMS not sent (permission not granted).", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(this, "Could not save weight.", Toast.LENGTH_SHORT).show();
            }
        });

        btnHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });
    }

    private boolean hasSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void sendGoalSms(double currentWeight) {
        try {
            SmsManager.getDefault().sendTextMessage(
                    ALERT_PHONE,
                    null,
                    "Goal reached! Logged weight: " + currentWeight,
                    null,
                    null
            );
        } catch (Exception e) {
            Toast.makeText(this, "SMS failed to send.", Toast.LENGTH_SHORT).show();
        }
    }
}