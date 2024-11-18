package com.example.myapplication;

import android.content.*;
import android.database.sqlite.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReadingPlanner.db";
    private static final int DATABASE_VERSION = 2;

    // Table and columns
    public static final String TABLE_BOOKS = "Books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author"; // 추가: 작가
    public static final String COLUMN_PUBLISHER = "publisher"; // 추가: 출판사
    public static final String COLUMN_START_DATE = "start_date"; // 시작 날짜
    public static final String COLUMN_END_DATE = "end_date"; // 종료 날짜
    public static final String COLUMN_TOTAL_PAGES = "total_pages";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_PAGES_READ = "pages_read";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " + // 추가
                COLUMN_PUBLISHER + " TEXT, " + // 추가
                COLUMN_START_DATE + " TEXT, " + // 추가
                COLUMN_END_DATE + " TEXT, " + // 추가
                COLUMN_TOTAL_PAGES + " INTEGER, " +
                COLUMN_DAYS + " INTEGER, " +
                COLUMN_PAGES_READ + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_BOOKS + " ADD COLUMN " + COLUMN_AUTHOR + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_BOOKS + " ADD COLUMN " + COLUMN_PUBLISHER + " TEXT");
        }
    }

    // Insert book
    public long insertBook(String title,String author, String publisher, int totalPages, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AUTHOR, author); // 추가
        values.put(COLUMN_PUBLISHER, publisher); // 추가
        values.put(COLUMN_TOTAL_PAGES, totalPages);

        int days = calculateDaysBetween(startDate, endDate);
        values.put(COLUMN_DAYS, days);

        return db.insert(TABLE_BOOKS, null, values);
    }

    // Update pages read
    public void updatePagesRead(int bookId, int pagesRead) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAGES_READ, pagesRead);
        db.update(TABLE_BOOKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookId)});
    }

    // Helper method to calculate days between two dates
    private int calculateDaysBetween(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }
}

