package com.example.carelink;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class fragment_shorts extends Fragment {

    private DBHelper dbHelper;
    private ListView dietListView, workoutListView;
    private ArrayAdapter<String> dietAdapter, workoutAdapter;
    private ArrayList<String> dietList, workoutList;

    public fragment_shorts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shorts, container, false);

        dbHelper = new DBHelper(getContext());
        dietListView = view.findViewById(R.id.dietListView);
        workoutListView = view.findViewById(R.id.workoutListView);

        dietList = new ArrayList<>();
        workoutList = new ArrayList<>();

        loadDietPlan();
        loadWorkoutPlan();

        dietAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, dietList);
        workoutAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, workoutList);

        dietListView.setAdapter(dietAdapter);
        workoutListView.setAdapter(workoutAdapter);

        // Enable multiple choice for the lists
        dietListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        workoutListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        workoutListView.setOnItemClickListener((parent, view1, position, id) -> {
            String item = workoutList.get(position);
            toggleWorkoutCompletion(item);
        });


        TextView re = view.findViewById(R.id.textView14);


        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return view;
    }

    private void loadDietPlan() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT meal_type, food_items FROM diet_plan", null);
        dietList.clear();
        if (cursor.moveToFirst()) {
            do {
                String mealType = cursor.getString(0);
                String foodItems = cursor.getString(1);
                dietList.add(mealType + ": " + foodItems);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void loadWorkoutPlan() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT exercise_name, reps, sets, is_completed FROM workout_plan", null);
        workoutList.clear();
        if (cursor.moveToFirst()) {
            do {
                String exerciseName = cursor.getString(0);
                int reps = cursor.getInt(1);
                int sets = cursor.getInt(2);
                boolean isCompleted = cursor.getInt(3) == 1;
                workoutList.add(exerciseName + " - " + reps + " reps x " + sets + " sets" + (isCompleted ? " (Completed)" : ""));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void toggleWorkoutCompletion(String workoutItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String exerciseName = workoutItem.split(" - ")[0];
        Cursor cursor = db.rawQuery("SELECT is_completed FROM workout_plan WHERE exercise_name = ?", new String[]{exerciseName});
        if (cursor.moveToFirst()) {
            boolean isCompleted = cursor.getInt(0) == 1;
            ContentValues contentValues = new ContentValues();
            contentValues.put("is_completed", isCompleted ? 0 : 1);
            db.update("workout_plan", contentValues, "exercise_name = ?", new String[]{exerciseName});
            loadWorkoutPlan();
            workoutAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    private void addWorkoutPlan(String exerciseName, int reps, int sets) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("exercise_name", exerciseName);
        contentValues.put("reps", reps);
        contentValues.put("sets", sets);
        db.insert("workout_plan", null, contentValues);
    }

    private void deleteSelectedPlans() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete selected diet items
        for (int i = 0; i < dietListView.getCount(); i++) {
            if (dietListView.isItemChecked(i)) {
                String dietItem = dietList.get(i);
                String mealType = dietItem.split(":")[0];
                db.delete("diet_plan", "meal_type = ?", new String[]{mealType});
            }
        }

        // Delete selected workout items
        for (int i = 0; i < workoutListView.getCount(); i++) {
            if (workoutListView.isItemChecked(i)) {
                String workoutItem = workoutList.get(i);
                String exerciseName = workoutItem.split(" - ")[0];
                db.delete("workout_plan", "exercise_name = ?", new String[]{exerciseName});
            }
        }

        // Clear selections after deletion
        dietListView.clearChoices();
        workoutListView.clearChoices();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getContext()); // Use getContext() to get the correct context
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bottom4);

        Button add = dialog.findViewById(R.id.addWorkoutBtn);
        Button del = dialog.findViewById(R.id.deletePlanBtn);
        EditText workoutNameInput = dialog.findViewById(R.id.workoutNameInput);
        EditText repsInput = dialog.findViewById(R.id.repsInput);
        EditText setsInput = dialog.findViewById(R.id.setsInput);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String workoutName = workoutNameInput.getText().toString();
                int reps = Integer.parseInt(repsInput.getText().toString());
                int sets = Integer.parseInt(setsInput.getText().toString());

                addWorkoutPlan(workoutName, reps, sets);
                loadWorkoutPlan();
                workoutAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedPlans();
                loadDietPlan();
                loadWorkoutPlan();
                dietAdapter.notifyDataSetChanged();
                workoutAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}
