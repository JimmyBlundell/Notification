package com.example.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        displayDate.setText(String.format("Today's Date:\n%s", formattedDate));
        // Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createEvent(){
        Intent intent = new Intent(Intent.ACTION_INSERT,
                CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Event from Notifications app in CS5590");
        intent.putExtra(CalendarContract.Events.DESCRIPTION,
                "Learning Android!");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,
                "KCMO");
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