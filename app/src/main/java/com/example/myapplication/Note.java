package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")  // 테이블 이름이 "note_table"로 설정되어 있어야 합니다.
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String author;
    private String content;
    private long date;

    // 생성자, getter, setter
    public Note(String title, String author, String content, long date) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String subtitle) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
