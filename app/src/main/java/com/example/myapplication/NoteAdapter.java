package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private NoteDao noteDao;
    private List<Note> notes;
    private OnNoteClickListener onNoteClickListener;
    private OnItemClickListener listener;
    private OnItemDeleteClickListener deleteListener;  // 삭제 클릭 리스너 추가

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.onNoteClickListener = listener;
    }
    // 클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    // 삭제 클릭 리스너 인터페이스 정의
    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(Note note);  // 삭제 버튼 클릭 시 호출
    }

    public NoteAdapter(Context context, List<Note> notes, OnItemClickListener listener, OnItemDeleteClickListener deleteListener) {
        this.context = context;
        this.notes = notes;
        this.listener = listener;
        this.deleteListener = deleteListener;  // 삭제 클릭 리스너 설정
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.titleTextView.setText(note.getTitle());
        holder.authorTextView.setText(note.getAuthor());

        // 날짜 포맷 변경
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(new Date(note.getDate()));
        holder.dateTextView.setText(formattedDate);  // 날짜와 시간 표시

        // 항목 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(note);
            }
        });

        // 삭제 버튼 클릭 리스너 설정
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onItemDeleteClick(note);  // 삭제 호출
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, dateTextView;
        Button deleteButton;  // 삭제 버튼 추가

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            authorTextView = itemView.findViewById(R.id.text_view_author);
            dateTextView = itemView.findViewById(R.id.text_view_date);
            deleteButton = itemView.findViewById(R.id.button_delete_note);  // 삭제 버튼 연결
        }
    }
}

