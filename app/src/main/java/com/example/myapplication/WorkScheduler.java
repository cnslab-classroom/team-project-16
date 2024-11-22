package com.example.myapplication;

import android.content.Context;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class WorkScheduler {
    public static void scheduleDailyTask(Context context) {
        // 현재 시간에서 0시 0분까지의 차이를 계산
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 만약 현재 시간이 이미 0시 이후라면 내일 0시로 설정
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 0시 0분까지 남은 시간 계산
        long initialDelay = calendar.getTimeInMillis() - System.currentTimeMillis();

        // 0시 0분에 첫 실행되도록 설정하고, 이후에는 하루마다 반복
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                DailyWork.class,
                1, TimeUnit.DAYS) // 하루 주기
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS) // 첫 실행을 0시로 설정
                .build();

        // WorkManager에 작업 예약
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}