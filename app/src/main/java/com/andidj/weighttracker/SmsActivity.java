package com.andidj.weighttracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsActivity extends AppCompatActivity {

    private static final int REQ_SMS = 2001;

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        tvStatus = findViewById(R.id.textView9);
        Button btnEnable = findViewById(R.id.button7);

        updateStatusText();

        btnEnable.setOnClickListener(v -> {
            if (hasSmsPermission()) {
                Toast.makeText(this, "SMS permission already granted.", Toast.LENGTH_SHORT).show();
                updateStatusText();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.SEND_SMS},
                        REQ_SMS
                );
            }
        });
    }

    private boolean hasSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void updateStatusText() {
        if (hasSmsPermission()) {
            tvStatus.setText("Permission Status: Granted");
        } else {
            tvStatus.setText("Permission Status: Not Granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_SMS) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            Toast.makeText(
                    this,
                    granted ? "SMS permission granted." : "SMS permission denied. App will still work.",
                    Toast.LENGTH_SHORT
            ).show();

            updateStatusText();
        }
    }
}