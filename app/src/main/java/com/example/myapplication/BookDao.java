package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.time.LocalDate;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM book_table WHERE start_date = :currentDate")
    List<Book> getBooksByCurrentDate(LocalDate currentDate);

    @Query("SELECT * FROM book_table")
    List<Book> getAllBooks();
}