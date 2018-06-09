package com.example.tanushreechaubal.bookmark_searchlist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TanushreeChaubal on 4/30/18.
 */

public final class BookSearchListUtils {

    public static final String LOG_TAG = BookMarkMainActivity.class.getName();


    private BookSearchListUtils(){

    }

    public static List<Book> fetchBookData(String requestURL){

        URL url = createUrl(requestURL);
        Log.d(LOG_TAG, "TEST: fetchBookData............"+ requestURL);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }

        List<Book> books = extractBookDetailsFromJSON(jsonResponse);
        return books;
    }

    public static List<Book> extractBookDetailsFromJSON(String BookJSON){

        if (TextUtils.isEmpty(BookJSON)){
            return null;
        }

        List<Book> books = new ArrayList<>();

        try{
            Log.e(LOG_TAG, "TEST: This is BOOKJSON:  "+BookJSON);
            JSONObject root = new JSONObject(BookJSON);
            JSONArray book_array = root.getJSONArray("items");

            for(int i=0; i<book_array.length();i++){
                JSONObject bookObject = book_array.getJSONObject(i);
                JSONObject propertyVolume = bookObject.getJSONObject("volumeInfo");
                String title = propertyVolume.getString("title");
                String language = propertyVolume.getString("language");
                int pageCount = propertyVolume.getInt("pageCount");
                JSONArray author = propertyVolume.getJSONArray("authors");
                String authors = author.getString(0);
                Book book = new Book(title, authors, pageCount, language);
                books.add(book);
            }

        } catch(JSONException e){
            Log.e("BookSearchListUtils", "Problem parsing the book JSON results", e);
        }
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //Log.e(LOG_TAG, "TEST: this is the URL:    "+url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
