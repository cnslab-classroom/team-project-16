package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmHelper {

    public static void setDailyAlarms(Context context) {
        setAlarm(context, 7, 0, "아침 독서를 시작해 보세요!");
        setAlarm(context, 12, 0, "점심 독서 시간입니다.");
        setAlarm(context, 19, 0, "저녁 독서를 마무리하세요!");
    }

    public static void setImmediateTestAlarm(Context context, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                9999, // 테스트 알람용 고유 requestCode
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long triggerTime = System.currentTimeMillis() + 5000; // 현재 시간으로부터 5초 뒤
        Log.d("AlarmHelper", "알람이 5초 후에 설정되었습니다.");

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    public static void setAlarm(Context context, int hour, int minute, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("message", message);

        int requestCode = hour * 100 + minute; // 각 시간마다 고유한 requestCode 설정
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Log.d("AlarmHelper", "알람 설정됨: " + hour + ":" + minute);

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }
}
