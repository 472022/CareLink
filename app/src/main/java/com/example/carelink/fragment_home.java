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

public class fragment_home extends Fragment {
    CircularProgressBar circularProgressBar;
    private DBHelper dbHelper;
    private ProgressBar progressBar;
    private TextView progressText;

    public fragment_home() {
        // Required empty public constructor
    }

    public static fragment_home newInstance(String param1, String param2) {
        fragment_home fragment = new fragment_home();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    private void updateProgress() {
        int[] taskStats = dbHelper.getTaskCompletionStats();
        int totalTasks = taskStats[0];
        int completedTasks = taskStats[1];

        int completionPercentage = (totalTasks == 0) ? 0 : (completedTasks * 100) / totalTasks;

        circularProgressBar.setProgress(completionPercentage);

    }
}
