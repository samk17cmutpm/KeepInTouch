package com.keepintouch.service;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.keepintouch.R;
public class NotificationIntentService extends IntentService {

    public NotificationIntentService() {
        // Used to name the worker thread, important only for debugging.
        super("test-service");
    }
    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        // This describes what will happen when service is triggered
    }

}
