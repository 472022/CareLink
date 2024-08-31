package com.example.carelink;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class cal extends AppCompatActivity {
    DBHelper DB;
    TextView t1; // Declare t1 at class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);

        DB = new DBHelper(this); // Initialize DBHelper

        Button app = findViewById(R.id.button4);
        t1 = findViewById(R.id.progress_text); // Initialize t1

        String date = "2024-08-31"; // Example date, use actual date as needed
        displayCalories(date); // Call the method to display calories

        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bottom);

        Button add = dialog.findViewById(R.id.add);
        Button save = dialog.findViewById(R.id.save);
        EditText catagory = dialog.findViewById(R.id.cat);

        String date = "2024-08-31"; // Example date, use actual date as needed

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = catagory.getText().toString();
                int ca = Integer.parseInt(cat);

                boolean result = DB.addCalories(date, ca);
                if (result) {
                    Toast.makeText(cal.this, "Calories added successfully!", Toast.LENGTH_SHORT).show();
                    displayCalories(date); // Refresh the displayed calories
                } else {
                    Toast.makeText(cal.this, "Failed to add calories.", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = catagory.getText().toString();
                int ca = Integer.parseInt(cat);

                boolean result = DB.decreaseCalories(date, ca);
                if (result) {
                    Toast.makeText(cal.this, "Calories decreased successfully!", Toast.LENGTH_SHORT).show();
                    displayCalories(date); // Refresh the displayed calories
                } else {
                    Toast.makeText(cal.this, "Failed to decrease calories.", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Method to fetch and display calories in t1
    private void displayCalories(String date) {
        Cursor cursor = DB.getHealthData(date); // Fetch data for the specific date
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int calories = cursor.getInt(cursor.getColumnIndex("calories"));
            t1.setText("" + calories); // Display calories in t1
        } else {
            t1.setText("Calories: 0"); // If no data found, show 0 calories
        }
        cursor.close(); // Close the cursor to avoid memory leaks
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to avoid memory leaks
    }
}
