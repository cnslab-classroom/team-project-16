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

public class ReadingScheduleActivity3 extends AppCompatActivity {
    private TextView title, isSuccess1, isSuccess2, todayRead, tomorrowRead;
    private Button mainBtn;
    private BookDatabase bookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_schedule3);

        title = findViewById(R.id.title);
        isSuccess1 = findViewById(R.id.isSuccess1);
        isSuccess2 = findViewById(R.id.isSuccess2);
        todayRead = findViewById(R.id.todayRead);
        tomorrowRead = findViewById(R.id.tomorrowRead);
        mainBtn = findViewById(R.id.mainBotton);

        bookDatabase = BookDatabase.getInstance(this);

        loadBooksFromDatabase();

        mainBtn.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReadingScheduleActivity3.this, MainActivity.class);
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
                    title.setText("책 제목 : " + book.getBookTitle());

                    todayRead.setText("오늘 읽은 분량 : " + book.getTodayReadPages());
                    if(book.getPeriod() == 0){
                        tomorrowRead.setText("내일 읽을 분량 : null");
                    }else{
                        tomorrowRead.setText("내일 읽을 분량 : " + ((book.getPageCount() - book.getPagesRead()) / book.getPeriod()) + "쪽");
                    }

                    if(book.getPeriod() + 1 == 0){
                        isSuccess1.setText("null!");
                        isSuccess2.setText("null!");
                    }else{
                        if(book.getTodayReadPages() < (book.getPagesRead() - book.getTodayReadPages()) / (book.getPeriod() + 1)){
                            isSuccess1.setText("오늘의 목표 달성 실패!");
                            isSuccess2.setText("분발합시다!");
                        }else{
                            isSuccess1.setText("오늘의 목표 달성!");
                            isSuccess2.setText("이대로 계속해 봅시다!");
                        }
                    }

                } else {
                    title.setText("책 제목 : 없음");
                    isSuccess1.setText("오늘의 목표량 : 없음");
                    isSuccess2.setText("남은 기간 : 없음" );
                    todayRead.setText("오늘 읽은 분량 : 없음");
                    tomorrowRead.setText("내일 읽을 분량 : 없음");
                }
            });
            executor.shutdown();
        });
    }
}