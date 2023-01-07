package com.example.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText ed1, ed2;
    Button b1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1 = findViewById(R.id.editText);
        ed2 = findViewById(R.id.editText2);
        b1 = findViewById(R.id.button);

        final String CHANNEL_ID = "channel_id";

        NotificationManager globalNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

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

            globalNotificationManager.createNotificationChannel(mChannel);
        }

        final NotificationManager notificationManager = globalNotificationManager;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context ctx = getApplicationContext();

                String title = ed1.getText().toString().trim();
                String body = ed2.getText().toString().trim();

                int NOTIFICATION_ID = Math.abs(UUID.randomUUID().hashCode());
                Log.i(TAG, "created notification with ID " + NOTIFICATION_ID);

                int flag = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
                PendingIntent clickPendingIntent = PendingIntent.getActivity(ctx,  0, new Intent(), flag);

                Intent closeIntent = new  Intent(ctx, ActionReceiver.class);
                closeIntent.putExtra("id", NOTIFICATION_ID);
                PendingIntent closePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,  closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentIntent(clickPendingIntent)
                        .addAction(R.mipmap.ic_launcher, "Done", closePendingIntent);


                builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_ID, builder.build());

                finish();
                System.exit(0);
            }
        });
    }

    public static class ActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context ctx, Intent intent) {
                int id = intent.getIntExtra("id", -1);
                Log.i(TAG, "close action received with notification ID " + id);
                if (id < 0) {
                    return;
                }

                Log.i(TAG, "closing notification ID " + id);
                NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(id);
            }
    }
}