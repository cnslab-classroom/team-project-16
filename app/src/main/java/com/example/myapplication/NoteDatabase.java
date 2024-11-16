package com.example.myapplication;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 2)  // 버전 번호 증가
public abstract class NoteDatabase extends RoomDatabase {
    private static volatile NoteDatabase instance;

    public abstract NoteDao noteDao();

    // 버전 1 -> 2 마이그레이션
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 예시: subtitle 열 추가
            database.execSQL("ALTER TABLE note_table ADD COLUMN subtitle TEXT DEFAULT ''");
        }
    };

    public static NoteDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (NoteDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    NoteDatabase.class, "note_database")
                            .addMigrations(MIGRATION_1_2)  // 마이그레이션 추가
                            .build();
                }
            }
        }
        return instance;
    }
}

