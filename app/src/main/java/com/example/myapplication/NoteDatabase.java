package com.example.myapplication;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {
    private static volatile NoteDatabase instance;  // volatile 키워드를 추가하여 스레드 안전성을 보장

    public abstract NoteDao noteDao();

    public static NoteDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (NoteDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    NoteDatabase.class, "note_database")
                            .fallbackToDestructiveMigration() // 마이그레이션 실패 시 기존 데이터 삭제
                            .build();
                }
            }
        }
        return instance;
    }
}
