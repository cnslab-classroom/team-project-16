package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BookViewModel bookViewModel;
    private boolean bookDBIsEmpty = true, isCompletedRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Read A Book");

        requestExactAlarmPermission();

        // 알림 권한 요청 (Android 13 이상)
        requestNotificationPermission();

        // 알람 설정
        AlarmHelper.setDailyAlarms(this);

        // 테스트 알람 설정 (5초 후)
        AlarmHelper.setImmediateTestAlarm(this, "테스트 알람입니다!");
        Log.d("MainAct", "테스트 알람이 설정되었습니다.");

/*
        BookDatabase db = Room.databaseBuilder(getApplicationContext(), BookDatabase.class,
                "book_database").build();

        new Thread(() -> {
            db.clearAllTables();
        }).start();
*/

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // LiveData Observer 등록
        bookViewModel.isBookDatabaseEmpty().observe(this, isEmpty -> {
            Log.d("Observer", "isEmpty: " + isEmpty);
            bookDBIsEmpty = isEmpty != null && isEmpty;
        });

        bookViewModel.getCompletedToday().observe(this, completedToday -> {
            Log.d("Observer", "completedToday: " + completedToday);
            isCompletedRead = completedToday != null && completedToday;
        });

        // 초기 값 설정
        bookDBIsEmpty = bookViewModel.isBookDatabaseEmpty().getValue() != null
                ? bookViewModel.isBookDatabaseEmpty().getValue()
                : true;

        isCompletedRead = bookViewModel.getCompletedToday().getValue() != null
                ? bookViewModel.getCompletedToday().getValue()
                : false;

        TextView todayTextView = findViewById(R.id.today);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String currentDate = dateFormat.format(new Date());

        todayTextView.setText(currentDate);

        Button searchBookButton = findViewById(R.id.searchBookButton);
        Button readingScheduleButton = findViewById(R.id.readingScheduleButton);
        Button completeListButton = findViewById(R.id.completeListButton);
        Button reviewButton = findViewById(R.id.reviewButton);

        WorkScheduler.scheduleDailyTask(getApplicationContext());

        readingScheduleButton.setOnClickListener(view -> {
            Log.d("Observer", "bookDBIsEmpty: " + bookDBIsEmpty + ", isCompletedRead: " + isCompletedRead);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                List<Book> books = bookViewModel.getBooks(); // DB에서 책 목록 가져오기
                boolean isAnyBookCompleted = books.stream().anyMatch(Book::isCompletedToday);

                runOnUiThread(() -> {
                    Intent intentReadingScheduleActivity;
                    if (bookDBIsEmpty) {
                        // 데이터베이스가 비어 있는 경우
                        intentReadingScheduleActivity = new Intent(getApplicationContext(), ReadingScheduleActivity1.class);
                    } else if (isAnyBookCompleted) {
                        // 오늘 목표가 완료된 경우
                        intentReadingScheduleActivity = new Intent(getApplicationContext(), ReadingScheduleActivity3.class);
                    } else {
                        // 오늘 목표가 완료되지 않은 경우
                        intentReadingScheduleActivity = new Intent(getApplicationContext(), ReadingScheduleActivity2.class);
                    }
                    startActivity(intentReadingScheduleActivity);
                });
                executor.shutdown();
            });
        });

        searchBookButton.setOnClickListener(view -> {
            Intent intentSearchBookActivity = new Intent(getApplicationContext(), SearchBookActivity.class);
            startActivity(intentSearchBookActivity);
        });

        completeListButton.setOnClickListener(view -> {
            Intent intentCompleteListActivity = new Intent(getApplicationContext(), CompleteListActivity.class);
            startActivity(intentCompleteListActivity);
        });

        reviewButton.setOnClickListener(view -> {
            Intent intentReviewListActivity = new Intent(getApplicationContext(), ReviewListActivity.class);
            startActivity(intentReviewListActivity);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookViewModel.refreshData();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "알림 권한이 허용되었습니다.");
            } else {
                Log.d("MainActivity", "알림 권한이 거부되었습니다.");
            }
        }
    }
    // 정확한 알람 권한 확인 및 요청
    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            } else {
                Log.d("MainActivity", "정확한 알람 권한이 이미 허용되었습니다.");
            }
        }
    }

}

