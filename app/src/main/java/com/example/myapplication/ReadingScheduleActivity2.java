package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

public class ReadingScheduleActivity2 extends AppCompatActivity {
    private TextView todayTv, titleTv, goalTv, leftDayTv, leftPageTv, endDayTv;
    private EditText todayPageInput;
    private Button completeButton;
    private BookDatabase bookDatabase;
    private Integer todayReadPage;
    private String currentDate;
    private Boolean isReadingEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_schedule2);
        setTitle("독서 계획");

        todayTv = findViewById(R.id.todayTv);
        titleTv = findViewById(R.id.titleTv);
        goalTv = findViewById(R.id.goalTv);
        leftDayTv = findViewById(R.id.leftDayTv);
        leftPageTv = findViewById(R.id.leftPageTv);
        endDayTv = findViewById(R.id.endDayTv);

        todayPageInput = findViewById(R.id.todayPageInput);
        completeButton = findViewById(R.id.completeButton);

        bookDatabase = BookDatabase.getInstance(this);

        loadBooksFromDatabase();

        completeButton.setOnClickListener(view -> {
            String todayPageText = todayPageInput.getText().toString();
            if (!todayPageText.isEmpty() && todayPageText.matches("[0-9]+")) {
                todayReadPage = Integer.parseInt(todayPageText);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    UpdateSchedule();

                    runOnUiThread(() -> {
                        if(isReadingEnd){
                            Intent intent = new Intent(ReadingScheduleActivity2.this, ReadingScheduleActivity4.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(ReadingScheduleActivity2.this, ReadingScheduleActivity3.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                });
                executor.shutdown();
            } else {
                Toast.makeText(ReadingScheduleActivity2.this, "올바른 페이지 수를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBooksFromDatabase() {
        currentDate = LocalDate.now().toString();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Book> books = bookDatabase.bookDao().getAllBooks();
            runOnUiThread(() -> {
                if (!books.isEmpty()) {
                    Book book = books.get(0);
                    todayTv.setText("오늘은 " + currentDate + " 입니다.");
                    titleTv.setText("책 제목 : " + book.getBookTitle());
                    goalTv.setText("오늘의 목표량 : " + ((book.getPageCount() - book.getPagesRead()) / book.getPeriod()) + "쪽");
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

    private void UpdateSchedule() {
        List<Book> books = bookDatabase.bookDao().getAllBooks();
        if (!books.isEmpty()) {
            Book book = books.get(0);

            int pagesReadNew = book.getPagesRead() + todayReadPage;
            int leftDayNew = book.getPeriod() - 1;

            book.setPagesRead(pagesReadNew);
            book.setTodayReadPages(todayReadPage);
            book.setPeriod(leftDayNew);

            book.setCompletedToday(true);

            if(pagesReadNew >= book.getPageCount() || leftDayNew<=0)
                isReadingEnd = true;
            else
                isReadingEnd = false;

            bookDatabase.bookDao().updateBook(book);
            bookDatabase.bookDao().updateCompletedToday(0, true);
            Log.d("Observer", "completed: " + book.isCompletedToday());
        }
    }
}