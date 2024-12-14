package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<SearchResult> searchResults;

    public SearchResultAdapter(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchResult result = searchResults.get(position);
        holder.titleTextView.setText("제목: " + result.getTitle());
        holder.authorTextView.setText("저자: " + result.getAuthor());
        holder.publisherTextView.setText("출판사: " + result.getPublisher());
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, publisherTextView;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            publisherTextView = itemView.findViewById(R.id.publisherTextView);
        }
    }
}
