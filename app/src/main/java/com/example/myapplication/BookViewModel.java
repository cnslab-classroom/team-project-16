package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookViewModel extends AndroidViewModel {
    private BookDao bookDao;
    private MutableLiveData<Integer> bookCountLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> completedTodayLiveData = new MutableLiveData<>();
    private LiveData<Boolean> isBookDatabaseEmpty;

    public BookViewModel(Application application) {
        super(application);
        BookDatabase bookDatabase = Room.databaseBuilder(application, BookDatabase.class, "book_database").build();
        bookDao = bookDatabase.bookDao();

        // Transformations.map을 사용하여 책 데이터가 비어있는지 확인
        isBookDatabaseEmpty = Transformations.map(bookCountLiveData, count -> count == null || count == 0);
    }

    public LiveData<Boolean> isBookDatabaseEmpty() {
        return isBookDatabaseEmpty;
    }

    public LiveData<Boolean> getCompletedToday() {
        return completedTodayLiveData;
    }

    public LiveData<Boolean> isCompletedToday(long bookId) {
        return bookDao.getCompletedToday((int)bookId); // 특정 책의 completedToday 값을 LiveData로 반환
    }

    // 책의 읽은 페이지 수와 완료 상태를 갱신하는 메서드
    public void refreshData() {
        // DB에서 책의 개수를 가져와서 bookCountLiveData를 업데이트
        bookDao.countBooks().observeForever(count -> {
            bookCountLiveData.setValue(count);
        });

        // 완료 여부 상태를 가져와서 completedTodayLiveData를 업데이트
        bookDao.getCompletedToday(0).observeForever(completed -> {
            completedTodayLiveData.setValue(completed != null && completed);
        });
    }

    // 책 목록 가져오기
    public List<Book> getBooks() {
        final List<Book>[] booksHolder = new List[1];
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            booksHolder[0] = bookDao.getAllBooks(); // 책 목록 가져오기
        });
        executor.shutdown();

        try {
            executor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return booksHolder[0];
    }
}