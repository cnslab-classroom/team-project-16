package com.example.myapplication;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT COUNT(*) FROM book_table")
    LiveData<Integer> countBooks();

    @Query("SELECT completedToday FROM book_table WHERE id = :bookId")
    LiveData<Boolean> getCompletedToday(int bookId);

    @Query("SELECT completedToday FROM book_table WHERE id = :bookId")
    boolean isCompletedToday(long bookId);

    @Query("UPDATE book_table SET completedToday = :completed WHERE id = :id")
    void updateCompletedToday(int id, boolean completed); // 읽기 상태 업데이트

    @Update
    void updateBook(Book book);
}