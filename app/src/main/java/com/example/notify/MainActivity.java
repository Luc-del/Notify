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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MenuItemCompat;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText editTextTitle, editTextBody;
    Button buttonSend;
    Spinner spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextBody = findViewById(R.id.editTextBody);
        buttonSend = findViewById(R.id.button);

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
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context ctx = getApplicationContext();

                String title = editTextTitle.getText().toString().trim();
                String body = editTextBody.getText().toString().trim();

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
//                        .setAutoCancel(true)
                        .setContentIntent(clickPendingIntent)
                        .addAction(R.mipmap.ic_launcher, "Done", closePendingIntent);


                builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_ID, builder.build());

                finish();
                System.exit(0);
            }
        });
    }

    // Handle close button on notification
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

    // Create the action bar elements
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        spinner = (Spinner) menu.findItem(R.id.spinner).getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i(TAG, "spinner selection: " + pos + " " + id);

                if (pos > 0) {
                    CharSequence text = "Feature coming soon ! (" + parent.getSelectedItem().toString() + ")";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i(TAG, "spinner nothing selected");
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}