package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddReviewActivity extends AppCompatActivity {

    private NoteDao noteDao;
    private EditText editTextTitle, editTextAuthor, editTextContent;

    private long noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_add);
        setTitle("독후감 작성");

        noteDao = NoteDatabase.getInstance(this).noteDao();

        editTextTitle = findViewById(R.id.editText_title);
        editTextAuthor = findViewById(R.id.editText_author);
        editTextContent = findViewById(R.id.editText_content);

        Intent intent = getIntent();
        noteId = intent.getLongExtra("noteId", -1);
        if (noteId != -1) {
            new Thread(() -> {
                Note note = noteDao.getNoteById(noteId);
                runOnUiThread(() -> {
                    editTextTitle.setText(note.getTitle());
                    editTextAuthor.setText(note.getAuthor());
                    editTextContent.setText(note.getContent());
                });
            }).start();
        }

        findViewById(R.id.button_save_note).setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String subtitle = editTextAuthor.getText().toString();
            String content = editTextContent.getText().toString();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                if (noteId != -1) {
                    Note updatedNote = new Note(title, subtitle, content, System.currentTimeMillis());
                    updatedNote.setId(noteId);
                    updateNote(updatedNote);
                } else {
                    Note newNote = new Note(title, subtitle, content, System.currentTimeMillis());
                    saveNote(newNote);
                }
            }
        });
    }
    private void saveNote(Note note) {
        new Thread(() -> {
            noteDao.insert(note);
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

    private void updateNote(Note note) {
        new Thread(() -> {
            noteDao.update(note);
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

}
