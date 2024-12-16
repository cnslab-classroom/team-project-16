package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "daily_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationText = intent.getStringExtra("message"); // 전달받은 메시지 내용

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("AlarmReceiver", "알람 리시버가 호출되었습니다.");
        Log.d("AlarmReceiver", "알림 내용: " + notificationText);

        // Android 8.0 이상에서는 NotificationChannel이 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Daily Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 클릭 시 실행할 Activity를 설정
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // 알림 아이콘
                .setContentTitle("독서 알림")
                .setContentText(notificationText) // 알림 메시지
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true) // 알림 클릭 시 사라짐
                .setContentIntent(pendingIntent);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
