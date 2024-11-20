package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadingSchedule extends AppCompatActivity {
    private TextView todayTv, titleTv, goalTv, leftDayTv, leftPageTv, endDayTv;
    private LocalDate startDate, endDate;
    private BookDatabase bookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_schedule2);

        todayTv = findViewById(R.id.todayTv);
        titleTv = findViewById(R.id.titleTv);
        goalTv = findViewById(R.id.goalTv);
        leftDayTv = findViewById(R.id.leftDayTv);
        leftPageTv = findViewById(R.id.leftPageTv);
        endDayTv = findViewById(R.id.endDayTv);

        bookDatabase = BookDatabase.getInstance(this);

        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Book> books = bookDatabase.bookDao().getAllBooks();

            runOnUiThread(() -> {
                if (!books.isEmpty()) {
                    Book book = books.get(0);

                    todayTv.setText("오늘은 " + book.getStartDate() + " 입니다.");
                    titleTv.setText("책 제목 : " + book.getBookTitle());
                    goalTv.setText("오늘의 목표량 : " + (book.getPageCount() / 2) + "쪽"); // 예시로 1/2 목표
                    leftDayTv.setText("남은 기간 : " + book.getPeriod() + "일");
                    leftPageTv.setText("남은 쪽수 : " + (book.getPageCount() - book.getPagesRead()) + "쪽");
                    endDayTv.setText("도전은 " + book.getEndDate() + "에 종료됩니다.");
                } else {
                    todayTv.setText("저장된 책 정보가 없습니다.");
                    titleTv.setText("책 제목 : 없음");
                    goalTv.setText("오늘의 목표량 : 없음");
                    leftDayTv.setText("남은 기간 : 없음");
                    leftPageTv.setText("남은 쪽수 : 없음");
                    endDayTv.setText("도전 종료일 : 없음");
                }
            });
            executor.shutdown();
        });
    }
}
