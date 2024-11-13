package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.selectBookButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, SelectBookActivity.class))
        );

        findViewById(R.id.readingScheduleButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, ReadingScheduleActivity.class))
        );

        findViewById(R.id.completeListButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, CompleteListActivity.class))
        );

        findViewById(R.id.reviewButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, BookReviewActivity.class))
        );
    }
}