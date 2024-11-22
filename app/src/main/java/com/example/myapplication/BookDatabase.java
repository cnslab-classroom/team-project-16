package com.example.myapplication;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;
import androidx.room.TypeConverters;

@Database(entities = {Book.class}, version = 2)
@TypeConverters(Converters.class)
public abstract class BookDatabase extends RoomDatabase {
    private static volatile BookDatabase instance;

    public abstract BookDao bookDao();

    public static synchronized BookDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class, "book_database")
                    .addMigrations(MIGRATION_1_2) // 마이그레이션 추가
                    .fallbackToDestructiveMigration() // 필요시 기존 데이터 삭제
                    .build();
        }
        return instance;
    }

    // 마이그레이션: 기존 데이터베이스 스키마 업데이트
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE book_table ADD COLUMN start_date TEXT DEFAULT NULL");
            database.execSQL("ALTER TABLE book_table ADD COLUMN end_date TEXT DEFAULT NULL");
            database.execSQL("ALTER TABLE book_table ADD COLUMN completedToday INTEGER DEFAULT 0");
        }
    };
}

