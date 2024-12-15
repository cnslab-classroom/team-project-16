package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private BookViewModel bookViewModel;
    private boolean bookDBIsEmpty = true, isCompletedRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

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
        bookViewModel.refreshData();  // 데이터 새로고침
    }
}