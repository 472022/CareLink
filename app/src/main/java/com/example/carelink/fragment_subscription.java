package com.example.carelink;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class fragment_subscription extends Fragment {

    private static final String CHANNEL_ID = "my_channel";
    private NotificationManager notificationManager;
    private EditText titleEditText, messageEditText;
    private TimePicker timePicker;
    private Button addReminderButton;
    private ListView remindersListView;
    private ArrayList<String> remindersList;
    private ArrayAdapter<String> remindersAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        titleEditText = view.findViewById(R.id.titleEditText);
        messageEditText = view.findViewById(R.id.messageEditText);
        timePicker = view.findViewById(R.id.timePicker);
        addReminderButton = view.findViewById(R.id.addReminderButton);
        remindersListView = view.findViewById(R.id.remindersListView);

        // Initialize the ArrayList and ArrayAdapter for the ListView
        remindersList = new ArrayList<>();
        remindersAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, remindersList);
        remindersListView.setAdapter(remindersAdapter);

        createNotificationChannel();

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminder();
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

    private void setReminder() {
        String title = titleEditText.getText().toString();
        String message = messageEditText.getText().toString();

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // Create a Calendar object for the time set
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Check if the selected time is before the current time and add a day to it
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Add the reminder to the list and notify the adapter
        String reminder = "Title: " + title + "\nMessage: " + message + "\nTime: " + hour + ":" + String.format("%02d", minute);
        remindersList.add(reminder);
        remindersAdapter.notifyDataSetChanged();

        // Schedule the notification
        scheduleNotification(calendar, title, message);
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
