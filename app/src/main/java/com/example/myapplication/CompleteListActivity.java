package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompleteListActivity extends AppCompatActivity {

    private RecyclerView completeRecyclerList;
    private CompletedBookAdapter adapter;
    private CompletedBookDatabase completedBookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_list);
        setTitle("완독 도서 목록");

        completeRecyclerList = findViewById(R.id.completeRecyclerList);
        completeRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        completedBookDatabase = CompletedBookDatabase.getInstance(this);
        loadCompletedBooks();
    }

    private void loadCompletedBooks() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<CompletedBook> completedBooks = completedBookDatabase.completedBookDao().getAllCompletedBooks();
            runOnUiThread(() -> {
                adapter = new CompletedBookAdapter(completedBooks);
                completeRecyclerList.setAdapter(adapter);
            });
            executor.shutdown();
        });
    }
}
