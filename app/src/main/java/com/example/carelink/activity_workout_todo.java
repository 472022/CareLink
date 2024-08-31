package com.example.carelink;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class activity_workout_todo extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView workoutTodoListView;
    private ArrayAdapter<String> workoutAdapter;
    private ArrayList<String> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_todo);

        dbHelper = new DBHelper(this);
        workoutTodoListView = findViewById(R.id.workoutTodoListView);

        workoutList = new ArrayList<>();
        loadWorkoutPlan();

        workoutAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, workoutList);
        workoutTodoListView.setAdapter(workoutAdapter);
        workoutTodoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        workoutTodoListView.setOnItemClickListener((parent, view, position, id) -> {
            String item = workoutList.get(position);
            toggleWorkoutCompletion(item);
        });
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

        // Fetch the current completion status from the database
        Cursor cursor = db.rawQuery("SELECT is_completed FROM workout_plan WHERE exercise_name = ?", new String[]{exerciseName});

        if (cursor.moveToFirst()) {
            boolean isCompleted = cursor.getInt(0) == 1;

            // Toggle the completion status
            ContentValues contentValues = new ContentValues();
            contentValues.put("is_completed", isCompleted ? 0 : 1);

            // Update the database with the new completion status
            db.update("workout_plan", contentValues, "exercise_name = ?", new String[]{exerciseName});

            // Reload the workout plan to reflect the changes
            loadWorkoutPlan();

            // Notify the adapter that the data has changed
            workoutAdapter.notifyDataSetChanged();

            // Provide feedback to the user
            Toast.makeText(this, exerciseName + (isCompleted ? " marked as incomplete" : " marked as complete"), Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

}
