package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReadingScheduleActivity1 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText titleInput, authorInput, publisherInput, totalPagesInput;
    private ArrayList<Integer> dateArray, monthArray, yearArray;
    private Spinner startYearSpinner, startMonthSpinner, startDaySpinner;
    private Spinner endYearSpinner, endMonthSpinner, endDaySpinner;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_schedule1);

        dbHelper = new DatabaseHelper(this);
        this.deleteDatabase("books.db");  //db삭제

        titleInput = findViewById(R.id.titleInput);
        authorInput = findViewById(R.id.authorInput);
        publisherInput = findViewById(R.id.publisherInput);
        totalPagesInput = findViewById(R.id.totalPagesInput);
        startButton = findViewById(R.id.startButton);

        startYearSpinner = findViewById(R.id.startYearSpinner);
        startMonthSpinner = findViewById(R.id.startMonthSpinner);
        startDaySpinner = findViewById(R.id.startDaySpinner);

        endYearSpinner = findViewById(R.id.endYearSpinner);
        endMonthSpinner = findViewById(R.id.endMonthSpinner);
        endDaySpinner = findViewById(R.id.endDaySpinner);

        initializeYearSpinner(startYearSpinner);
        initializeYearSpinner(endYearSpinner);
        initializeMonthSpinner(startMonthSpinner);
        initializeMonthSpinner(endMonthSpinner);

        startMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < parent.getCount()) {
                    updateDaysSpinner(startYearSpinner, startMonthSpinner, startDaySpinner);
                } else {
                    Log.e("SpinnerError", "Invalid position: " + position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        endMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < parent.getCount()) {
                    updateDaysSpinner(endYearSpinner, endMonthSpinner, endDaySpinner);
                } else {
                    Log.e("SpinnerError", "Invalid position: " + position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        startButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String author = authorInput.getText().toString();
            String publisher = publisherInput.getText().toString();
            int totalPages = Integer.parseInt(totalPagesInput.getText().toString());
            String startDate = getSelectedDate(startYearSpinner, startMonthSpinner, startDaySpinner);
            String endDate = getSelectedDate(endYearSpinner, endMonthSpinner, endDaySpinner);

            long id = dbHelper.insertBook(title, author, publisher, totalPages, startDate, endDate);

            if (id != -1) {
                Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReadingScheduleActivity1.this, ReadingScheduleActivity2.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeYearSpinner(Spinner spinner) {
        List<String> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            years.add(String.valueOf(currentYear + i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initializeMonthSpinner(Spinner spinner) {
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateDaysSpinner(Spinner yearSpinner, Spinner monthSpinner, Spinner daySpinner) {
        int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
        int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());

        List<String> days = new ArrayList<>();
        int maxDay = YearMonth.of(year, month).lengthOfMonth();
        for (int i = 1; i <= maxDay; i++) {
            days.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);
    }

    private String getSelectedDate(Spinner yearSpinner, Spinner monthSpinner, Spinner daySpinner) {
        String year = yearSpinner.getSelectedItem().toString();
        String month = monthSpinner.getSelectedItem().toString();
        String day = daySpinner.getSelectedItem().toString();

        // 날짜를 "yyyy-MM-dd" 형식으로 반환
        return year + "-" + (month.length() == 1 ? "0" + month : month) + "-" + (day.length() == 1 ? "0" + day : day);
    }
}