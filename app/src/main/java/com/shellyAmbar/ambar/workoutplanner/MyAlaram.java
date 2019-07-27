package com.shellyAmbar.ambar.workoutplanner;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class MyAlaram extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        //mediaPlayer.start();
        String taskName =intent.getStringExtra("massageHeader");
        String taskBody = intent.getStringExtra("massageBody");
        Vibrator vibrator =(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast.makeText(context, taskName +" "+taskBody , Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"shellyAmbar");
        builder
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.tasker)
                .setContentTitle(taskName + "  NEW TASK!")
                .setContentText(taskBody)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE |Notification.DEFAULT_SOUND);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

        Uri notifyRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(context,notifyRing);
        ringtone.play();




    }
}
