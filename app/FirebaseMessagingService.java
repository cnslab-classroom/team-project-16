package com.example.myapplication

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // 데이터 메시지 처리 (알림이 아닌 데이터 전송 시)
        if (remoteMessage.getData().size() > 0) {
            // 데이터 메시지에 대한 처리
        }

        // 알림 메시지 처리
        if (remoteMessage.getNotification() != null) {
            String messageBody = remoteMessage.getNotification().getBody();
            sendNotification(messageBody);  // 알림 표시
        }
    }

    @Override
    public void onNewToken(String token) {
        // 새로 발급된 FCM 토큰을 수신할 때 처리
        // 서버에 토큰을 보내거나 기타 처리
    }

    private void sendNotification(String messageBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "default_channel";

        // Android O 이상에서는 알림 채널을 만들어야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 객체 생성
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("푸시 알림 제목")
                .setContentText(messageBody)  // 수신한 메시지
                .setSmallIcon(R.drawable.ic_notification)  // 알림 아이콘
                .build();

        // 알림 표시
        notificationManager.notify(0, notification);
    }
}
