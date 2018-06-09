package com.example.tanushreechaubal.bookmark_searchlist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookMarkMainActivity extends AppCompatActivity {

    public String enteredText;
    public EditText search_editText;
    public Button searchBooks_button;
    private BookAdapter bAdapter;
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_main);

        ListView bookListView = findViewById(R.id.books_listView);
        bAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(bAdapter);

        emptyStateTextView = (TextView) findViewById(R.id.emptyState_TextView);

            searchBooks_button = findViewById(R.id.search_button);
            searchBooks_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            search_editText = findViewById(R.id.search_editText);
                            enteredText = search_editText.getText().toString();
                            emptyStateTextView.setText("");
                            BooksAsyncTask task = new BooksAsyncTask();
                            String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
                            final String QUERY_PARAM = "q";
                            final String MAX_RESULTS = "maxResults";
                            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                                    .appendQueryParameter(QUERY_PARAM, enteredText)
                                    .appendQueryParameter(MAX_RESULTS, "10")
                                    .build();

                            URL requestURL = new URL(builtURI.toString());
                            String queryBooks = requestURL.toString();
                            task.execute(queryBooks);
                        } else {
                            bAdapter.clear();
                            emptyStateTextView.setText("No internet connection.");
                        }
                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    }
                }
            });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){

            Log.e("On Config Change","LANDSCAPE");
        }else{

            Log.e("On Config Change","PORTRAIT");
        }
    }

    private class BooksAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            if(urls.length < 1 || urls[0] == null){
                return null;
            }
            List<Book> result = BookSearchListUtils.fetchBookData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            bAdapter.clear();
            if(books != null && !books.isEmpty()){
                emptyStateTextView.setText("");
                bAdapter.addAll(books);
            } else {
                bAdapter.clear();
                emptyStateTextView.setText("No data to display currently. Please try later, or try a new search term!");
            }
        }
    }
}
