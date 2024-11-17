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
        noteId = intent.getLongExtra("noteId", -1); // 수정할 메모의 ID 받기
        if (noteId != -1) {
            // 수정할 메모가 있을 경우 기존 메모 데이터 가져오기
            new Thread(() -> {
                Note note = noteDao.getNoteById(noteId); // DB에서 메모 가져오기
                runOnUiThread(() -> {
                    editTextTitle.setText(note.getTitle());
                    editTextAuthor.setText(note.getAuthor());
                    editTextContent.setText(note.getContent());
                });
            }).start();
        }

        findViewById(R.id.button_save_note).setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String subtitle = editTextAuthor.getText().toString(); // 부제
            String content = editTextContent.getText().toString();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                if (noteId != -1) {  // 수정할 메모가 있으면 수정 처리
                    // 기존 메모 수정
                    Note updatedNote = new Note(title, subtitle, content, System.currentTimeMillis());  // 수정된 시간 설정
                    updatedNote.setId(noteId);  // 수정할 메모의 ID 설정
                    updateNote(updatedNote);
                } else {
                    // 새 메모 추가
                    Note newNote = new Note(title, subtitle, content, System.currentTimeMillis());  // 새 메모 추가 시 현재 시간으로 설정
                    saveNote(newNote);
                }
            }
        });
    }
    // 새 메모 추가
    private void saveNote(Note note) {
        new Thread(() -> {
            noteDao.insert(note);  // 메모 저장
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

    // 메모 수정
    private void updateNote(Note note) {
        new Thread(() -> {
            noteDao.update(note);  // 메모 수정
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

}
