package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM note_table WHERE id = :noteId LIMIT 1")
    Note getNoteById(long noteId);

    @Query("SELECT * FROM note_table")
    List<Note> getAllNotes();
}

