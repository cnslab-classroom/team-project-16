package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CompletedBookAdapter extends RecyclerView.Adapter<CompletedBookAdapter.CompletedBookViewHolder> {

    private final List<CompletedBook> completedBooks;

    public CompletedBookAdapter(List<CompletedBook> completedBooks) {
        this.completedBooks = completedBooks;
    }

    @NonNull
    @Override
    public CompletedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_item, parent, false);
        return new CompletedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedBookViewHolder holder, int position) {
        CompletedBook completedBook = completedBooks.get(position);
        holder.textViewTitle.setText(completedBook.getBookTitle());
        holder.textViewAuthor.setText(completedBook.getBookAuthor());

        LocalDate completionDate = completedBook.getCompletionDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = completionDate.format(formatter);

        holder.textViewDate.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return completedBooks.size();
    }

    public static class CompletedBookViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewAuthor;
        private final TextView textViewDate;

        public CompletedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewAuthor = itemView.findViewById(R.id.text_view_author);
            textViewDate = itemView.findViewById(R.id.text_view_date);
        }
    }
}


