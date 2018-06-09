package com.example.tanushreechaubal.bookmark_searchlist;

/**
 * Created by TanushreeChaubal on 4/30/18.
 */

public class Book {

    private String title;
    private String author;
    private String language;
    private int pageCount;

    public Book(String title, String author, int pageCount, String language) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }
}
