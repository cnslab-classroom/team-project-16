package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReviewListActivity extends AppCompatActivity {

    private NoteDao noteDao;
    private NoteDatabase noteDatabase;
    private RecyclerView recyclerView;
    private NoteAdapter notesAdapter;
    private Executor executor;

    // ActivityResultLauncher 정의
    private final ActivityResultLauncher<Intent> addNoteLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 메모 추가 후 새로고침
                    executor.execute(() -> {
                        List<Note> notes = noteDao.getAllNotes();  // 새로 저장된 메모 가져오기
                        runOnUiThread(() -> notesAdapter.setNotes(notes));  // RecyclerView 갱신
                    });
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);
        setTitle("독후감 목록");

        noteDatabase = NoteDatabase.getInstance(this);
        noteDao = noteDatabase.noteDao();

        recyclerView = findViewById(R.id.reviewRecyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NoteAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(notesAdapter);

        // "메모 추가" 버튼 클릭 시 AddNoteActivity로 이동
        findViewById(R.id.button_add_note).setOnClickListener(v -> {
            Intent intent = new Intent(ReviewListActivity.this, AddReviewActivity.class);
            addNoteLauncher.launch(intent);  // ActivityResultLauncher를 통해 AddNoteActivity 실행
        });

        // Executor 초기화
        executor = Executors.newSingleThreadExecutor();

        // 비동기 작업 실행
        executor.execute(() -> {
            List<Note> notes = noteDao.getAllNotes();  // 백그라운드에서 데이터베이스 조회
            runOnUiThread(() -> notesAdapter.setNotes(notes));  // UI 업데이트
        });
    }
}
