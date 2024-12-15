package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class RegisterBook extends AppCompatActivity {
    private EditText titleInput, authorInput, publisherInput, totalPagesInput;
    private Spinner startYearSpinner, startMonthSpinner, startDaySpinner;
    private Spinner endYearSpinner, endMonthSpinner, endDaySpinner;
    private Button startButton;
    private BookDatabase bookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_schedule1);
        setTitle("독서 계획");

        titleInput = findViewById(R.id.titleInput);
        authorInput = findViewById(R.id.authorInput);
        publisherInput = findViewById(R.id.publisherInput);
        totalPagesInput = findViewById(R.id.totalPagesInput);

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

        startButton = findViewById(R.id.startButton);

        bookDatabase = BookDatabase.getInstance(this);

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
            saveBookToDatabase();
        });
    }

    private void saveBookToDatabase() {
        String bookTitle = titleInput.getText().toString().trim();
        String bookAuthor = authorInput.getText().toString().trim();
        String publisher = publisherInput.getText().toString().trim();
        String _startDate = getSelectedDate(startYearSpinner, startMonthSpinner, startDaySpinner);
        String _endDate = getSelectedDate(endYearSpinner, endMonthSpinner, endDaySpinner);

        try {
            LocalDate startDate = LocalDate.parse(_startDate);
            LocalDate endDate = LocalDate.parse(_endDate);

            int period = (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);

            if (period < 0) {
                Toast.makeText(this, "종료 날짜가 현재보다 이전입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            int pageCount = parsePageCount(totalPagesInput.getText().toString().trim());
            if (pageCount < 0) {
                Toast.makeText(this, "페이지 수는 양수로 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isInputInvalid(bookTitle, bookAuthor, _startDate, _endDate)) {
                Toast.makeText(this, "모든 필드를 올바르게 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Book book = new Book(bookTitle, bookAuthor, publisher, pageCount, startDate, endDate, period + 1);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                bookDatabase.bookDao().insert(book);
                runOnUiThread(() -> {
                    Toast.makeText(RegisterBook.this, "책 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterBook.this, ReadingSchedule.class);
                    startActivity(intent);
                    finish();
                });
                executor.shutdown();
            });
        } catch (Exception e) {
            Log.e("RegisterBook", "Date parsing error", e);
            Toast.makeText(this, "날짜 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private int parsePageCount(String pageCountStr) {
        try {
            int pageCount = Integer.parseInt(pageCountStr);
            return pageCount > 0 ? pageCount : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isInputInvalid(String title, String author, String startDate, String endDate) {
        if (title.isEmpty() || author.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            return true;
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return start.isAfter(end);
    }

    private String getSelectedDate(Spinner yearSpinner, Spinner monthSpinner, Spinner daySpinner) {
        String year = yearSpinner.getSelectedItem().toString();
        String month = monthSpinner.getSelectedItem().toString();
        String day = daySpinner.getSelectedItem().toString();
        return String.format("%s-%02d-%02d", year, Integer.parseInt(month), Integer.parseInt(day));
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
}
