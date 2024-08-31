package com.example.carelink;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class fragment_subscription extends Fragment {

    private static final String CHANNEL_ID = "my_channel";
    private NotificationManager notificationManager;
    private ListView remindersListView;
    private ArrayList<String> remindersList;
    private ArrayAdapter<String> remindersAdapter;
    private DBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        dbHelper = new DBHelper(getContext());
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        remindersListView = view.findViewById(R.id.remindersListView);
        TextView addReminderTextView = view.findViewById(R.id.textView14);

        remindersList = new ArrayList<>();
        loadReminders();

        remindersAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, remindersList);
        remindersListView.setAdapter(remindersAdapter);

        createNotificationChannel();

        addReminderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return view;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            String channelDescription = "My Channel Description";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bottom5);

        Button add = dialog.findViewById(R.id.addWorkoutBtn);
        EditText titleEditText = dialog.findViewById(R.id.titleEditText);
        EditText messageEditText = dialog.findViewById(R.id.messageEditText);
        TimePicker timePicker = dialog.findViewById(R.id.timePicker);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String message = messageEditText.getText().toString();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                addReminder(title, message, calendar);
                loadReminders();
                remindersAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void addReminder(String title, String message, Calendar calendar) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("message", message);
        contentValues.put("time", calendar.getTimeInMillis());

        db.insert("reminders", null, contentValues);

        scheduleNotification(calendar, title, message);
    }

    private void loadReminders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, message, time FROM reminders", null);
        remindersList.clear();
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String message = cursor.getString(1);
                long timeInMillis = cursor.getLong(2);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeInMillis);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                remindersList.add("Title: " + title + "\nMessage: " + message + "\nTime: " + hour + ":" + String.format("%02d", minute));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void scheduleNotification(Calendar calendar, String title, String message) {
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        intent.putExtra("notification_title", title);
        intent.putExtra("notification_message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
