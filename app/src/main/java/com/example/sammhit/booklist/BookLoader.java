package com.example.sammhit.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sammhit on 6/3/18.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String searchQ;
    public  BookLoader(Context context, String searchQ){
        super(context);
        this.searchQ =searchQ;
    }


    @Override
    public List<Book> loadInBackground() {
        if (!searchQ.isEmpty()){
            Log.i("BookLoader","loadInbAckground called");
            ArrayList<Book> books = QueryUtils.extractBooks("https://www.googleapis.com/books/v1/volumes?q="+searchQ+"&maxResults=30&orderBy=relevance");
            return books;
        }
        return null;
    }
}
