package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity {

    private EditText bookTitleInput;
    private RecyclerView searchResultsRecyclerView;
    private SearchResultAdapter searchResultAdapter;
    private List<SearchResult> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_);
        setTitle("도서 검색");

        bookTitleInput = findViewById(R.id.bookTitleInput);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        searchResults = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(searchResults);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(searchResultAdapter);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            String bookTitle = bookTitleInput.getText().toString().trim();
            if (!bookTitle.isEmpty()) {
                searchBook(bookTitle);
            } else {
                Toast.makeText(SearchBookActivity.this, "도서 제목을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchBook(String title) {
        new Thread(() -> {
            try {
                String apiKey = "3d77a12fb20a9fb068c8dc0b7b4f476ad4f8ed045750a0dfae323eee4f06b0c4"; // 국립 중앙도서관 API 키
                String encodedTitle = URLEncoder.encode(title, "UTF-8");
                String apiUrl = "https://www.nl.go.kr/NL/search/openApi/search.do?key=" + apiKey + "&kwd=" + encodedTitle;

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                List<SearchResult> results = parseXml(inputStream);

                runOnUiThread(() -> {
                    searchResults.clear();
                    searchResults.addAll(results);
                    searchResultAdapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(SearchBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private List<SearchResult> parseXml(InputStream inputStream) throws Exception {
        List<SearchResult> results = new ArrayList<>();
        SearchResult currentBook = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, "UTF-8");

        int eventType = parser.getEventType();
        String currentTag = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    currentTag = parser.getName();
                    if ("item".equals(currentTag)) {
                        currentBook = new SearchResult("", "", "");
                    }
                    break;

                case XmlPullParser.TEXT:
                    if (currentBook != null && currentTag != null) {
                        switch (currentTag) {
                            case "title_info":
                                currentBook.setTitle(parser.getText());
                                break;
                            case "author_info":
                                currentBook.setAuthor(parser.getText());
                                break;
                            case "pub_info":
                                currentBook.setPublisher(parser.getText());
                                break;
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("item".equals(parser.getName())) {
                        results.add(currentBook);
                        currentBook = null;
                    }
                    currentTag = null;
                    break;
            }
            eventType = parser.next();
        }

        return results;
    }
}