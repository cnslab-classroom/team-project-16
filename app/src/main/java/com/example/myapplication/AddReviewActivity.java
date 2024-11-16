package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddReviewActivity extends AppCompatActivity {

    private NoteDao noteDao;
    private EditText editTextTitle, editTextContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_add);
        setTitle("독후감 작성");

        noteDao = NoteDatabase.getInstance(this).noteDao();

        editTextTitle = findViewById(R.id.editText_title);
        editTextContent = findViewById(R.id.editText_content);

        findViewById(R.id.button_save_note).setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String content = editTextContent.getText().toString();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                Note note = new Note(title, content, System.currentTimeMillis());
                // 백그라운드에서 메모 저장
                new Thread(() -> {
                    noteDao.insert(note);  // 메모 데이터베이스에 저장
                    runOnUiThread(() -> {
                        setResult(RESULT_OK);  // 메모 추가 성공 결과 반환
                        finish();  // 메모 작성 화면 종료
                    });
                }).start();
            }
        });
    }

}
