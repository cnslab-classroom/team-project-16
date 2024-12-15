package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompletedBookDao {
    @Insert
    void insert(CompletedBook completedBook);

    @Query("SELECT * FROM CompletedBook")
    List<CompletedBook> getAllCompletedBooks();
}