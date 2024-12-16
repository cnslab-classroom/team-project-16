package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "default_channel"; // 알림 채널 ID

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // 푸시 알림 제목과 본문 가져오기
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        // 알림을 화면에 표시
        showNotification(title, message);
    }

    private void showNotification(String title, String message) {
        // NotificationManager를 사용하여 알림을 생성하고 표시
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android 8.0 이상에서는 알림 채널이 필요함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // 알림을 클릭했을 때 실행될 액티비티 정의
        Intent intent = new Intent(this, MainActivity.class); // 알림 클릭 시 이동할 액티비티
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알림 빌더 설정
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true) // 알림 클릭 후 알림이 사라짐
                .setContentIntent(pendingIntent); // 클릭 시 액티비티 이동

        // 알림 표시
        notificationManager.notify(0, notificationBuilder.build());
    }

}
