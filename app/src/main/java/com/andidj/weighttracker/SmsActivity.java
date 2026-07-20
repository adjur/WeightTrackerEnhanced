package com.andidj.weighttracker;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsActivity extends AppCompatActivity {

    private static final int REQ_SMS = 2001;

    public static final String PREFS_NAME = "weight_tracker_preferences";
    public static final String KEY_THEME = "selected_theme";
    public static final String KEY_GOAL_WEIGHT = "goal_weight";

    private static final String DEFAULT_THEME = "Light";
    private static final float DEFAULT_GOAL_WEIGHT = 150.0f;

    private TextView tvStatus;
    private Spinner spinnerTheme;
    private EditText editGoalWeight;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        tvStatus = findViewById(R.id.textView9);
        spinnerTheme = findViewById(R.id.spinnerTheme);
        editGoalWeight = findViewById(R.id.editGoalWeight);

        Button btnEnableSms = findViewById(R.id.button7);
        Button btnSave = findViewById(R.id.buttonSave);

        setupThemeSpinner();
        loadSavedSettings();
        updateStatusText();

        btnEnableSms.setOnClickListener(v -> requestSmsPermission());

        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void setupThemeSpinner() {
        String[] themes = {"Light", "Dark", "Forest", "Ocean"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                themes
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerTheme.setAdapter(adapter);
    }

    private void loadSavedSettings() {
        String savedTheme = preferences.getString(
                KEY_THEME,
                DEFAULT_THEME
        );

        float savedGoalWeight = preferences.getFloat(
                KEY_GOAL_WEIGHT,
                DEFAULT_GOAL_WEIGHT
        );

        editGoalWeight.setText(String.valueOf(savedGoalWeight));

        for (int i = 0; i < spinnerTheme.getCount(); i++) {
            String theme = spinnerTheme.getItemAtPosition(i).toString();

            if (theme.equals(savedTheme)) {
                spinnerTheme.setSelection(i);
                break;
            }
        }
    }

    private void saveSettings() {
        String goalWeightText =
                editGoalWeight.getText().toString().trim();

        if (goalWeightText.isEmpty()) {
            Toast.makeText(
                    this,
                    "Enter a goal weight.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        float goalWeight;

        try {
            goalWeight = Float.parseFloat(goalWeightText);
        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Enter a valid goal weight.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (goalWeight <= 0) {
            Toast.makeText(
                    this,
                    "Goal weight must be greater than zero.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String selectedTheme =
                spinnerTheme.getSelectedItem().toString();

        preferences.edit()
                .putString(KEY_THEME, selectedTheme)
                .putFloat(KEY_GOAL_WEIGHT, goalWeight)
                .apply();

        Toast.makeText(
                this,
                "Settings saved.",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void requestSmsPermission() {
        if (hasSmsPermission()) {
            Toast.makeText(
                    this,
                    "SMS permission already granted.",
                    Toast.LENGTH_SHORT
            ).show();

            updateStatusText();
            return;
        }

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.SEND_SMS},
                REQ_SMS
        );
    }

    private boolean hasSmsPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateStatusText() {
        if (hasSmsPermission()) {
            tvStatus.setText("Permission Status: Granted");
        } else {
            tvStatus.setText("Permission Status: Not Granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == REQ_SMS) {
            boolean granted =
                    grantResults.length > 0
                            && grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;

            Toast.makeText(
                    this,
                    granted
                            ? "SMS permission granted."
                            : "SMS permission denied. The app will still work.",
                    Toast.LENGTH_SHORT
            ).show();

            updateStatusText();
        }
    }
}