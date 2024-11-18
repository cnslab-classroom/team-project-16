package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReadingScheduleActivity1 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText titleInput, authorInput, publisherInput, totalPagesInput;
    private ArrayList<Integer> dateArray, monthArray, yearArray;
    private Spinner startDateSpinner, endDateSpinner, startYSpinner, startMSpinner, startDSpinner, endYSpinner, endMSpinner, endDSpinner;
    private String startDate, endDate;
    private Date startFormat, endFormat;
    private long diffDays = 5;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_schedule1);

        dbHelper = new DatabaseHelper(this);

        titleInput = findViewById(R.id.titleInput);
        authorInput = findViewById(R.id.authorInput);
        publisherInput = findViewById(R.id.publisherInput);
        totalPagesInput = findViewById(R.id.totalPagesInput);
        startButton = findViewById(R.id.startButton);

        startDateSpinner = findViewById(R.id.startDateSpinner);
        endDateSpinner = findViewById(R.id.endDateSpinner);

        initializeDateSpinner(startDateSpinner);
        initializeDateSpinner(endDateSpinner);
/*
        startYSpinner = findViewById(R.id.startYearSpinner);
        startMSpinner = findViewById(R.id.startMonthSpinner);
        startDSpinner = findViewById(R.id.startDateSpinner);
        endYSpinner = findViewById(R.id.endYearSpinner);
        endMSpinner = findViewById(R.id.endMonthSpinner);
        endDSpinner = findViewById(R.id.endDateSpinner);

        for(int i = 0;i<31;i++){
            dateArray.set(i,i+1);
        }
        for(int i = 0;i<12;i++){
            monthArray.set(i,i+1);
        }
        for(int i = 0;i<40;i++){
            yearArray.set(i,i+1);
        }
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, yearArray);
        ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, monthArray);
        ArrayAdapter<Integer> dateAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, dateArray);
        startYSpinner.setAdapter(yearAdapter);
        startMSpinner.setAdapter(monthAdapter);
        startDSpinner.setAdapter(dateAdapter);
        endYSpinner.setAdapter(yearAdapter);
        endMSpinner.setAdapter(monthAdapter);
        endMSpinner.setAdapter(dateAdapter);
*/

        startButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String author = authorInput.getText().toString();
            String publisher = publisherInput.getText().toString();
            int totalPages = Integer.parseInt(totalPagesInput.getText().toString());
            String startDate = startDateSpinner.getSelectedItem().toString();
            String endDate = endDateSpinner.getSelectedItem().toString();

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

    private void initializeDateSpinner(Spinner spinner) {
        List<String> dateList = generateDateList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private List<String> generateDateList() {
        List<String> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 365; i++) {
            dates.add(today.plusDays(i).format(formatter));
        }
        return dates;
    }
}