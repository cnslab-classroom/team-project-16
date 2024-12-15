package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity
public class CompletedBook {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String bookTitle;
    private String bookAuthor;
    private LocalDate completionDate;

    public CompletedBook(String bookTitle, String bookAuthor, LocalDate completionDate) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.completionDate = completionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}
