package com.example.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // notificationId is a unique int for each notification that you must define
    public int notificationId = 1;
    public String CHANNEL_ID = "one";

    private TextView title;
    private TextView displayDate;

    private Calendar calendar = Calendar.getInstance();
    private CalendarView simpleCalendar;

    private Button newEvent;

    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        displayDate = (TextView) findViewById(R.id.displayDate);
        simpleCalendar = findViewById(R.id.simpleCalendar);
        newEvent = findViewById(R.id.newEventButton);


        simpleCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                displayDate.setText(String.format("Selected Date:\n" +
                        "%d/%d/%d", m + 1, d, y));

                year = y;
                month = m;
                day = d;
            }
        });

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });


        displayDate.setText(String.format("Today's Date:\n%s", String.valueOf(Calendar.getInstance().getTime())));

        // Create the NotificationChannel
        // you should execute this code as soon as your app starts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createEvent(){
        Intent intent = new Intent(Intent.ACTION_INSERT,
                CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "This event is from \'Notification\' app.");
        intent.putExtra(CalendarContract.Events.DESCRIPTION,
                "Learn Android Coding in Java");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,
                "UMKC");
        // set time to calendar
        Calendar startTime = Calendar.getInstance();
        startTime.set(year, month, day);
        // pass time through
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        startActivity(intent);
    }

    public void displayEvent(){
        Calendar.getInstance().set(2010,1,1,0,0);
        Uri uri = Uri.parse("content://com.android.calendar/time"
                + String.valueOf(Calendar.getInstance().getTimeInMillis()));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}