package com.example.carelink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class set extends AppCompatActivity {

    DBHelper DB;
    EditText nameEditText, ageEditText, weightEditText, heightEditText;
    Button weightGainButton, weightLossButton, immunityButton, hairFallButton, bulkButton;
    ImageView maleImageView, femaleImageView;
    String selectedGender = ""; // Variable to store selected gender

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set); // Use your layout file

        // Initialize DBHelper
        DB = new DBHelper(this);

        // Bind EditText fields
        nameEditText = findViewById(R.id.editTextTextPersonName);
        ageEditText = findViewById(R.id.editTextNumber);
        weightEditText = findViewById(R.id.editTextNumber2);
        heightEditText = findViewById(R.id.editTextNumber3);

        // Bind buttons
        weightGainButton = findViewById(R.id.button7);
        weightLossButton = findViewById(R.id.button8);
        immunityButton = findViewById(R.id.button91);
        hairFallButton = findViewById(R.id.button72);
        bulkButton = findViewById(R.id.button82);

        Button next = findViewById(R.id.button9);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the flag to false so the setup screen doesn't show again
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstTime", false);
                editor.apply();

                // Proceed to the main part of the app
                Intent intent = new Intent(set.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the setup activity
            }
        });

        // Bind ImageViews for gender selection
        maleImageView = findViewById(R.id.maleImageView);
        femaleImageView = findViewById(R.id.femaleImageView);

        // Set OnClickListener for gender selection
        maleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGender("Male");
            }
        });

        femaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGender("Female");
            }
        });

        // Set onClickListeners for goal buttons
        weightGainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUserData("Weight Gain");
            }
        });

        weightLossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUserData("Weight Loss");
            }
        });

        immunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUserData("Immunity");
            }
        });

        hairFallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUserData("Hair Fall");
            }
        });

        bulkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUserData("Bulk");
            }
        });
    }

    private void selectGender(String gender) {
        selectedGender = gender;

        // Reset background colors
        maleImageView.setColorFilter(null);
        femaleImageView.setColorFilter(null);

        // Apply dark overlay to the selected image
        if (gender.equals("Male")) {
            maleImageView.setColorFilter(Color.argb(150, 0, 0, 0)); // Darken image
        } else if (gender.equals("Female")) {
            femaleImageView.setColorFilter(Color.argb(150, 0, 0, 0)); // Darken image
        }
    }

    private void insertUserData(String goal) {
        // Get the input data
        String name = nameEditText.getText().toString();
        int age = Integer.parseInt(ageEditText.getText().toString());
        double weight = Double.parseDouble(weightEditText.getText().toString());
        double height = Double.parseDouble(heightEditText.getText().toString());

        // Insert user data into the database
        boolean isInserted = DB.insertUserData(name, age, selectedGender, weight, height);
        if (isInserted) {
            // Insert diet and workout plans based on the goal
            boolean dietInserted = DB.insertDietPlan(name, goal);
            boolean workoutInserted = DB.insertWorkoutPlan(name, goal);

            if (dietInserted && workoutInserted) {
                Toast.makeText(set.this, "Data Inserted: " + goal, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(set.this, "Plan Insertion Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(set.this, "User Data Insertion Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
