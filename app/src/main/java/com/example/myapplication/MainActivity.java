package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView todayTextView = findViewById(R.id.today);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String currentDate = dateFormat.format(new Date());

        todayTextView.setText(currentDate);

        Button selectBookButton = (Button) findViewById(R.id.selectBookButton);
        Button readingScheduleButton = (Button) findViewById(R.id.readingScheduleButton);
        Button completeListButton = (Button) findViewById(R.id.completeListButton);
        Button reviewButton = (Button) findViewById(R.id.reviewButton);

        selectBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSelectBookActivity = new Intent(getApplicationContext(),SelectBookActivity.class);
                //startActivity();
            }
        });

        readingScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReadingScheduleActivity = new Intent(getApplicationContext(),ReadingScheduleActivity.class);
                //startActivity();
            }
        });

        completeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCompleteListActivity = new Intent(getApplicationContext(),CompleteListActivity.class);
                //startActivity();
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReviewListActivity = new Intent(getApplicationContext(),ReviewListActivity.class);
                startActivity(intentReviewListActivity);
            }
        });
    }
}