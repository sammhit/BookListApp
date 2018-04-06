package com.example.sammhit.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sammhit on 6/3/18.
 */

public class Book {
    private String title, avgRatings;
    private String author;
    private String imageUrl, previewUrl;

    public Book(String titleBook, String authorBook, String avgRatingsBook, String imageUrlBook, String previewUrlBook) {
        title = titleBook;
        author = authorBook;
        avgRatings = avgRatingsBook;
        imageUrl = imageUrlBook;
        previewUrl =previewUrlBook;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvgRatings() {
        return avgRatings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPreviewUrl(){
        return previewUrl;
    }


}


