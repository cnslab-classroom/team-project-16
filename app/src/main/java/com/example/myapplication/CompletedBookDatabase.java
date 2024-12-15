package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CompletedBook.class}, version = 1)
@TypeConverters(Converters.class)
abstract class CompletedBookDatabase extends RoomDatabase {
    public abstract CompletedBookDao completedBookDao();

    private static volatile CompletedBookDatabase instance;

    public static CompletedBookDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (CompletedBookDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    CompletedBookDatabase.class, "completed_books_database")
                            .build();
                }
            }
        }
        return instance;
    }
}
