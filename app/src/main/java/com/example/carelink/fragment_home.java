package com.example.carelink;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import android.graphics.Color;
import android.text.format.DateFormat;
import java.util.Calendar;

public class fragment_home extends Fragment {
    CircularProgressBar circularProgressBar;
    private DBHelper dbHelper;

    public fragment_home() {
        // Required empty public constructor
    }

    public static fragment_home newInstance(String param1, String param2) {
        fragment_home fragment = new fragment_home();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle fragment arguments if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Find UI elements
        circularProgressBar = view.findViewById(R.id.c);
        LinearLayout buttonExample = view.findViewById(R.id.cal);
        LinearLayout water = view.findViewById(R.id.wat);
        LinearLayout steps = view.findViewById(R.id.ste);
        LinearLayout sleep = view.findViewById(R.id.sle);
        FrameLayout pro = view.findViewById(R.id.ci);

        // Populate the dates
        populateDates(view);

        // Update progress
        updateProgress();

        // Set click listeners
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_workout_todo.class);
                startActivity(intent);
            }
        });

        buttonExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), cal.class);
                startActivity(intent);
            }
        });

        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), waterin.class);
                startActivity(intent);
            }
        });

        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), steps.class);
                startActivity(intent);
            }
        });

        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), sleep.class);
                startActivity(intent);
            }
        });
    }

    private void populateDates(View view) {
        LinearLayout datesLayout = view.findViewById(R.id.dates_layout);

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 14; i++) {  // Display next 14 days
            View dateItem = LayoutInflater.from(getContext()).inflate(R.layout.date_item, datesLayout, false);

            TextView tvDay = dateItem.findViewById(R.id.tv_day);
            TextView tvDate = dateItem.findViewById(R.id.tv_date);

            String day = DateFormat.format("EEE", calendar).toString(); // Day of the week
            String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)); // Date of the month

            tvDay.setText(day);
            tvDate.setText(date);

            if (calendar.get(Calendar.DAY_OF_MONTH) == currentDay) {
                dateItem.setBackgroundResource(R.drawable.dateda); // Highlight current date
                tvDay.setTextColor(Color.WHITE);
                tvDate.setTextColor(Color.WHITE);
            } else {
                dateItem.setBackgroundResource(R.drawable.dateno);
                tvDay.setTextColor(Color.BLACK);
                tvDate.setTextColor(Color.BLACK);
            }

            datesLayout.addView(dateItem);
            calendar.add(Calendar.DAY_OF_MONTH, 1);  // Move to the next day
        }
    }

    private void updateProgress() {
        int[] taskStats = dbHelper.getTaskCompletionStats();
        int totalTasks = taskStats[0];
        int completedTasks = taskStats[1];

        int completionPercentage = (totalTasks == 0) ? 0 : (completedTasks * 100) / totalTasks;
        circularProgressBar.setProgress(completionPercentage);
    }
}
