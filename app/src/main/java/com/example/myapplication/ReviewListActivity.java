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
                    executor.execute(() -> {
                        List<Note> notes = noteDao.getAllNotes();
                        runOnUiThread(() -> notesAdapter.setNotes(notes));
                    });
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);
        getSupportActionBar().hide();

        noteDatabase = NoteDatabase.getInstance(this);
        noteDao = noteDatabase.noteDao();

        recyclerView = findViewById(R.id.reviewRecyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesAdapter = new NoteAdapter(this, new ArrayList<>(),
                note -> {
                    Intent intent = new Intent(ReviewListActivity.this, AddReviewActivity.class);
                    intent.putExtra("noteId", note.getId());
                    addNoteLauncher.launch(intent);
                },
                note -> {
                    new AlertDialog.Builder(this)
                            .setMessage("정말 삭제하시겠습니까?")
                            .setPositiveButton("삭제", (dialog, which) -> {
                                deleteNoteAndUpdateUI(note);
                            })
                            .setNegativeButton("취소", null)
                            .show();
                }
        );
        recyclerView.setAdapter(notesAdapter);

        findViewById(R.id.button_add_note).setOnClickListener(v -> {
            Intent intent = new Intent(ReviewListActivity.this, AddReviewActivity.class);
            addNoteLauncher.launch(intent);
        });

        executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            List<Note> notes = noteDao.getAllNotes();
            runOnUiThread(() -> notesAdapter.setNotes(notes));
        });
    }

    private void deleteNoteAndUpdateUI(Note note) {
        executor.execute(() -> {
            noteDao.delete(note);

            List<Note> notes = noteDao.getAllNotes();

            runOnUiThread(() -> {
                notesAdapter.setNotes(notes);
                Toast.makeText(this, "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();  // 삭제 완료 메시지
            });
        });
    }
}







