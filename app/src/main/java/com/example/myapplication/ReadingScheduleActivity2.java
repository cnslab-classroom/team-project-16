package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class ReadingScheduleActivity2 extends AppCompatActivity {
//    TextView todayTv, titleTv, goalTv, leftDayTv, leftPageTv, endDayTv;
//    EditText todayPageInput;
//    Button completeButton;
//
//    String title , startDate, endDate;
//    int totalPages, readPages, days;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reading_schedule2);
//
//        todayTv = findViewById(R.id.todayTv);
//        titleTv = findViewById(R.id.titleTv);
//        goalTv = findViewById(R.id.goalTv);
//        leftDayTv = findViewById(R.id.leftDayTv);
//        leftPageTv = findViewById(R.id.leftPageTv);
//        endDayTv = findViewById(R.id.endDayTv);
//        todayPageInput = findViewById(R.id.todayPageInput);
//        completeButton = findViewById(R.id.completeButton);
//
//
//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        Cursor cursor = dbHelper.getAllBooks();
//
//        if (cursor.moveToFirst()) {
//            do {
//                title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
//                //startDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_DATE));
//                endDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_DATE));
//                totalPages = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_PAGES));
//                days = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DAYS));
//                readPages = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAGES_READ));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        int dailyTarget = totalPages / days;
//        //todayTv.setText("오늘의 날짜 : " + );
//        titleTv.setText("책 제목 : " + title);
//        goalTv.setText("오늘의 목표량 : " + dailyTarget + "쪽");
//        leftDayTv.setText("남은 기간 : " + days + "일");
//        leftPageTv.setText("남은 쪽수 : " + (totalPages - readPages) + "쪽");
//        endDayTv.setText("끝나는 날짜 : " + endDate);
//
//
//        completeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ReadingScheduleActivity2.this, ReadingScheduleActivity3.class);
//                startActivity(intent);
//            }
//        });
//    }
}