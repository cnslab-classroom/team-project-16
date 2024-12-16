package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.time.LocalDate;

public class ReadingScheduleActivity4 extends AppCompatActivity {

    private TextView title_end, isAllSuccess1, isAllSuccess2, todayRead_end;
    private Button retryBtn_end, mainBtn_end;
    private BookDatabase bookDatabase;
    private CompletedBookDatabase completedBookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_schedule4);
        setTitle("독서 계획");

        bookDatabase = BookDatabase.getInstance(this);
        completedBookDatabase = CompletedBookDatabase.getInstance(this);

        title_end = findViewById(R.id.title_end);
        isAllSuccess1 = findViewById(R.id.isAllSuccess1);
        isAllSuccess2 = findViewById(R.id.isAllSuccess2);
        todayRead_end = findViewById(R.id.todayRead_end);
        retryBtn_end = findViewById(R.id.retryBtn_end);
        mainBtn_end = findViewById(R.id.mainBtn_end);

        loadBooksFromDatabase();

        retryBtn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReadingScheduleActivity4.this, ReadingScheduleActivity1.class);
                startActivity(intent);
            }
        });

        mainBtn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReadingScheduleActivity4.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadBooksFromDatabase() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Book> books = bookDatabase.bookDao().getAllBooks();
            runOnUiThread(() -> {
                if (!books.isEmpty()) {
                    Book book = books.get(0);
                    title_end.setText("책 제목 : " + book.getBookTitle());
                    if ((book.getPageCount() - book.getPagesRead()) > 0) {
                        isAllSuccess1.setText("기간 내로 독서를");
                        isAllSuccess2.setText("끝마치지 못했습니다.");
                        retryBtn_end.setVisibility(View.VISIBLE);
                        retryBtn_end.setClickable(true);
                    } else {
                        isAllSuccess1.setText("축하합니다!");
                        isAllSuccess2.setText("독서를 모두 끝마쳤습니다!");
                        retryBtn_end.setVisibility(View.INVISIBLE);
                        retryBtn_end.setClickable(false);
                        saveCompletedBook(book);
                    }
                    todayRead_end.setText("오늘 읽은 분량 : " + book.getTodayReadPages());
                    deleteBook(book);
                } else {
                    title_end.setText("책 제목 : 없음");
                    todayRead_end.setText("오늘 읽은 분량 : 없음");
                }
            });
            executor.shutdown();
        });
    }

    private void saveCompletedBook(Book book) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            CompletedBook completedBook = new CompletedBook(book.getBookTitle(), book.getBookAuthor(), LocalDate.now());
            completedBookDatabase.completedBookDao().insert(completedBook);
            executor.shutdown();
        });
    }

    private void deleteBook(Book book) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            bookDatabase.bookDao().delete(book);
            executor.shutdown();
        });
    }

}