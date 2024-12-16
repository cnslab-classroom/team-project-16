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
        String notificationText = intent.getStringExtra("message");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("AlarmReceiver", "알람 리시버가 호출되었습니다.");
        Log.d("AlarmReceiver", "알림 내용: " + notificationText);

        // Android 8.0 (API 26) 이상 NotificationChannel 설정
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "daily_notification_channel", // 채널 ID
                    "Daily Notifications",        // 채널 이름
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 클릭 시 실행할 Activity 설정
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "daily_notification_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // 기본 아이콘 사용
                .setContentTitle("독서 알림")
                .setContentText(notificationText) // 전달받은 알림 메시지
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true) // 알림 클릭 시 자동 제거
                .setContentIntent(pendingIntent); // 클릭 시 실행될 인텐트

        // 알림 표시
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        Log.d("AlarmReceiver", "알림이 생성되었습니다.");

    }

}
