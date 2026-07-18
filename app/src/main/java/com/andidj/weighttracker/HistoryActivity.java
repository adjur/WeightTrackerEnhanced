package com.andidj.weighttracker;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private DBHelper db;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DBHelper(this);

        RecyclerView rv = findViewById(R.id.rvWeights);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(
                new ArrayList<>(),
                entry -> { // DELETE
                    boolean deleted = db.deleteWeight(entry.id);
                    Toast.makeText(this, deleted ? "Deleted entry." : "Delete failed.", Toast.LENGTH_SHORT).show();
                    loadData();
                },
                entry -> { // UPDATE
                    showUpdateDialog(entry);
                }
        );

        rv.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        ArrayList<WeightEntry> list = new ArrayList<>();

        Cursor c = db.getAllWeights(); // expects: id, date, weight
        while (c.moveToNext()) {
            long id = c.getLong(0);
            String date = c.getString(1);
            double weight = c.getDouble(2);
            list.add(new WeightEntry(id, date, weight));
        }
        c.close();

        adapter.setData(list);
    }

    private void showUpdateDialog(WeightEntry entry) {
        AppCompatEditText input = new AppCompatEditText(this);
        input.setText(String.valueOf(entry.weight));
        input.setHint("New weight");

        new AlertDialog.Builder(this)
                .setTitle("Update Weight (" + entry.date + ")")
                .setView(input)
                .setPositiveButton("Save", (d, which) -> {
                    String s = input.getText() == null ? "" : input.getText().toString().trim();

                    if (s.isEmpty()) {
                        Toast.makeText(this, "Enter a weight.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double newWeight = Double.parseDouble(s);
                        boolean ok = db.updateWeight(entry.id, entry.date, newWeight);
                        Toast.makeText(this, ok ? "Updated." : "Update failed.", Toast.LENGTH_SHORT).show();
                        loadData();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid number.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}