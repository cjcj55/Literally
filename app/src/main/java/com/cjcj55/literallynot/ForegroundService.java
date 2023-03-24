package com.cjcj55.literallynot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.SpeechRecognizer;

import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification for the foreground service
        final String CHANNEL_ID = "Foreground Service";
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentText("Hello")
                    .setContentTitle("Blah");


            // Start the foreground service with the notification
            startForeground(1001, notification.build());
        }
        // Do your work here, such as recording audio
        SpeechRecognizer speechRecognizer;
        
        // Stop the foreground service when your work is done
        //stopForeground(true);
       // stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // This service does not support binding, so return null
        return null;
    }
}
