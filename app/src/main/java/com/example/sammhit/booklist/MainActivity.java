package com.example.sammhit.booklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    EditText searchText;
    ListView bookListView;
    BookListArrayAdapter bookListArrayAdapter;
    String searchQuery = "";
    TextView emptyView;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchText = (EditText) findViewById(R.id.SearchText);
        Button searchButton = (Button) findViewById(R.id.SearchButton);
        bookListView = (ListView) findViewById(R.id.listView);
        emptyView = (TextView) findViewById(R.id.emptyView);
        getLoaderManager().initLoader(1, null, this).forceLoad();

        bookListArrayAdapter = new BookListArrayAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(bookListArrayAdapter);
        bookListView.setEmptyView(emptyView);

        //check for internet connectivity
        isConnected = checkForInternetConnectivity () ;


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchQuery = searchText.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                Log.i("Loader :", searchQuery);
                Log.i("Loader :", "called");
                getLoaderManager().restartLoader(1, null, MainActivity.this).forceLoad();

            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book currentBook = (Book) adapterView.getItemAtPosition(i);
                String previewURL = currentBook.getPreviewUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(previewURL));
                startActivity(intent);
            }
        });


    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.i("Loader", "Is created");
        return new BookLoader(MainActivity.this, searchQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        bookListArrayAdapter.clear();
        Log.i("Loader", "Finished bringing data");
        if (isConnected) {
            if (books != null && !books.isEmpty()) {
                bookListArrayAdapter.addAll(books);
                bookListArrayAdapter.notifyDataSetChanged();
            } else {
                emptyView.setText("No results found");
            }
        }
        else{
            emptyView.setText("No internet Connection");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookListArrayAdapter.addAll(new ArrayList<Book>());
    }


    public boolean checkForInternetConnectivity(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork!=null && activeNetwork.isConnectedOrConnecting());

    }

}
