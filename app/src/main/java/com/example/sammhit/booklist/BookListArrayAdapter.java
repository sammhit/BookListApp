package com.example.sammhit.booklist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sammhit on 6/3/18.
 */

public class BookListArrayAdapter extends ArrayAdapter<Book> {
    private  static final String LOG_TAG = BookListArrayAdapter.class.getSimpleName();
    View listView;
    Book currentBook;

    public BookListArrayAdapter (Context context, List<Book> books) {
        super(context,0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        listView = convertView;

        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout,parent,false);
        }

        //create Book object
        currentBook = getItem(position);
        //set name
        TextView titleTextView = (TextView) listView.findViewById(R.id.titleTextView);
        titleTextView.setText(currentBook.getTitle());



        TextView authorTextView = (TextView) listView.findViewById(R.id.authorTextView);
        authorTextView.setText(currentBook.getAuthor());

        TextView ratingsTextView = (TextView) listView.findViewById(R.id.avgRatingTextView);
        ratingsTextView.setText(currentBook.getAvgRatings());




        class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL url = new URL(currentBook.getImageUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView =(ImageView) listView.findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

        }
    }
        new ImageLoaderTask().execute();
        return listView;
    }
}
