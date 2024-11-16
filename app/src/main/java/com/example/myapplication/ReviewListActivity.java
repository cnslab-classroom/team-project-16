package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.app.AlertDialog;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReviewListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter notesAdapter;
    private NoteDao noteDao;
    private NoteDatabase noteDatabase;
    private Executor executor;

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

        // 데이터베이스 및 DAO 초기화
        noteDatabase = NoteDatabase.getInstance(this);
        noteDao = noteDatabase.noteDao();

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.reviewRecyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // NoteAdapter 생성자 수정: OnItemClickListener 및 OnItemDeleteClickListener를 전달
        notesAdapter = new NoteAdapter(this, new ArrayList<>(),
                note -> {
                    // 메모 클릭 시 수정 화면으로 이동
                    Intent intent = new Intent(ReviewListActivity.this, AddReviewActivity.class);
                    intent.putExtra("noteId", note.getId());  // 수정할 노트의 ID 전달
                    addNoteLauncher.launch(intent);  // AddReviewActivity 실행
                },
                note -> {
                    // 삭제 클릭 리스너
                    new AlertDialog.Builder(this)
                            .setMessage("정말 삭제하시겠습니까?")
                            .setPositiveButton("삭제", (dialog, which) -> {
                                deleteNoteAndUpdateUI(note);  // 삭제 후 UI 갱신
                            })
                            .setNegativeButton("취소", null)
                            .show();
                }
        );
        recyclerView.setAdapter(notesAdapter);

        // 메모 추가 버튼 클릭 시 AddReviewActivity로 이동
        findViewById(R.id.button_add_note).setOnClickListener(v -> {
            Intent intent = new Intent(ReviewListActivity.this, AddReviewActivity.class);
            addNoteLauncher.launch(intent);  // ActivityResultLauncher를 통해 AddReviewActivity 실행
        });

        // Executor 초기화
        executor = Executors.newSingleThreadExecutor();

        // 비동기 작업 실행
        executor.execute(() -> {
            List<Note> notes = noteDao.getAllNotes();  // 백그라운드에서 데이터베이스 조회
            runOnUiThread(() -> notesAdapter.setNotes(notes));  // UI 업데이트
        });
    }

    // 메모 삭제 후 UI 갱신
    private void deleteNoteAndUpdateUI(Note note) {
        executor.execute(() -> {
            // 메모 삭제
            noteDao.delete(note);

            // 삭제 후 새로 갱신된 메모 리스트 가져오기
            List<Note> notes = noteDao.getAllNotes();

            // UI 갱신 (MainThread에서 실행)
            runOnUiThread(() -> {
                notesAdapter.setNotes(notes);  // RecyclerView 갱신
                Toast.makeText(this, "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();  // 삭제 완료 메시지
            });
        });
    }
}







