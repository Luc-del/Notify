package com.example.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    EditText ed1, ed2;
    Button b1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1 = findViewById(R.id.editText);
        ed2 = findViewById(R.id.editText2);
        b1 = findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            int NOTIFICATION_ID = 0;

            @Override
            public void onClick(View v) {

                Context ctx = getApplicationContext();

                String title = ed1.getText().toString().trim();
                String body = ed2.getText().toString().trim();

                NOTIFICATION_ID ++;
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                String CHANNEL_ID = "";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    CHANNEL_ID = "channel_id";
                    CharSequence name = "Notifications";
                    String Description = "Your own notifications should be THAT easy";

                    int importance = NotificationManager.IMPORTANCE_LOW;

                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.CYAN);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationManager.createNotificationChannel(mChannel);
                }


                NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.mipmap.ic_launcher);


                Intent resultIntent = new Intent(ctx, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        });
    }
}