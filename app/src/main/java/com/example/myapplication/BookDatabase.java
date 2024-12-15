package com.example.myapplication;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;
import androidx.room.TypeConverters;

@Database(entities = {Book.class}, version = 3)
@TypeConverters(Converters.class)
public abstract class BookDatabase extends RoomDatabase {
    private static volatile BookDatabase instance;

    public abstract BookDao bookDao();

    public static synchronized BookDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class, "book_database")
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE book_table ADD COLUMN start_date TEXT DEFAULT NULL");
            database.execSQL("ALTER TABLE book_table ADD COLUMN end_date TEXT DEFAULT NULL");
            database.execSQL("ALTER TABLE book_table ADD COLUMN completedToday INTEGER DEFAULT 0");
            database.execSQL("ALTER TABLE book_table ADD COLUMN todayReadPages INTEGER DEFAULT 0 NOT NULL");
        }
    };
}

