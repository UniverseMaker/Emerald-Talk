package com.messenger.emeraldtalk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class FBMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = FBMessagingService.class.getSimpleName();
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        //super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived");

        try {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String content = data.get("content");
            String sound = "";
            if (data.containsKey("sound") == true)
                sound = data.get("sound");
            //String body = remoteMessage.getNotification().getBody();

            sendNotification(title, content, sound);

            //로컬 브로드캐스터로 액티비티와 통신
            Intent intent = new Intent("dbupdate");
            intent.putExtra("phone", "");
            broadcaster.sendBroadcast(intent);

        }catch (Exception e){

        }
    }

    private void sendNotification(String title, String message, String sound) {
        try {
            Log.i("emeraldtalk", "sendNotification");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    //.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_info))
                    .setSmallIcon(R.mipmap.ic_diamond2)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(message));

            //SharedPreferences prefs = getSharedPreferences("EMERALDTALK", MODE_PRIVATE);
            //String result = prefs.getString("SET_1", "0"); //키값, 디폴트값

            if (sound.isEmpty() == false)
                notificationBuilder.setSound(defaultSoundUri).setDefaults(Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelID = "notify_001";
                String channelName = "ChannelName";
                NotificationChannel channel = new NotificationChannel(channelID,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                notificationBuilder.setChannelId(channelID);
            }

            Log.i("emeraldtalk", "sendNotification-notify");
            notificationManager.notify((int) System.currentTimeMillis() /* ID of notification */, notificationBuilder.build());
        }catch (Exception e){

        }
    }

}