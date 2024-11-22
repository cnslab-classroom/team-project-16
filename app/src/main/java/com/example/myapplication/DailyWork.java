package com.example.myapplication;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myapplication.Book;
import com.example.myapplication.BookDatabase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DailyWork extends Worker {

    private BookDatabase bookDatabase;

    public DailyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        bookDatabase = Room.databaseBuilder(context, BookDatabase.class, "book_database").build();
    }

    @NonNull
    @Override
    public Result doWork() {
        // 설정 변경 작업
        updateSettings();
        return Result.success();
    }

    private void updateSettings() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Book> books = bookDatabase.bookDao().getAllBooks();
            for (Book book : books) {
                bookDatabase.bookDao().updateCompletedToday((int)book.getId(), false); // 모든 책의 completedToday를 초기화
            }
        });
    }
}