package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

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

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        bookViewModel.isBookDatabaseEmpty().observe(this, isEmpty -> {
            Log.d("Observer", "isEmpty: " + isEmpty);
            bookDBIsEmpty = isEmpty != null && isEmpty;
        });

        bookViewModel.getCompletedToday().observe(this, completedToday -> {
            Log.d("Observer", "completedToday: " + completedToday);
            isCompletedRead = completedToday != null && completedToday;
        });

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
                List<Book> books = bookViewModel.getBooks();
                boolean isAnyBookCompleted = books.stream().anyMatch(Book::isCompletedToday);

                runOnUiThread(() -> {
                    Intent intentReadingScheduleActivity;
                    if (bookDBIsEmpty) {
                        intentReadingScheduleActivity = new Intent(getApplicationContext(), RegisterBook.class);
                    } else if (isAnyBookCompleted) {
                        intentReadingScheduleActivity = new Intent(getApplicationContext(), TodayReadingEnd.class);
                    } else {
                        intentReadingScheduleActivity = new Intent(getApplicationContext(), ReadingSchedule.class);
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
}