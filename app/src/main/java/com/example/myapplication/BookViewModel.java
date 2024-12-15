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

        isBookDatabaseEmpty = Transformations.map(bookCountLiveData, count -> count == null || count == 0);
    }

    public LiveData<Boolean> isBookDatabaseEmpty() {
        return isBookDatabaseEmpty;
    }

    public LiveData<Boolean> getCompletedToday() {
        return completedTodayLiveData;
    }

    public LiveData<Boolean> isCompletedToday(long bookId) {
        return bookDao.getCompletedToday((int)bookId);
    }

    public void refreshData() {
        bookDao.countBooks().observeForever(count -> {
            bookCountLiveData.setValue(count);
        });

        bookDao.getCompletedToday(0).observeForever(completed -> {
            completedTodayLiveData.setValue(completed != null && completed);
        });
    }

    public List<Book> getBooks() {
        final List<Book>[] booksHolder = new List[1];
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            booksHolder[0] = bookDao.getAllBooks();
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