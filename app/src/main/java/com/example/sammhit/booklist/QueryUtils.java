package com.example.sammhit.booklist;

import android.annotation.SuppressLint;
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

/**
 * Created by sammhit on 6/3/18.
 */

public class QueryUtils {
    private static final String LOG_TAG =  QueryUtils.class.getSimpleName();

    private QueryUtils() throws JSONException{}

    public static ArrayList<Book> extractBooks(String requestUrl) {
        Log.i("extractbooks","called");
        ArrayList<Book> books = new ArrayList<>();
        String title=null;
        String authName=null;
        String ratings=null;
        String SAMPLE_JSON_RESPONSE = null ;
        String imageUrl=null;
        String previewUrl=null;


        URL url = createUrl(requestUrl);
        SAMPLE_JSON_RESPONSE = makeHTTPRequest(url);

        try{
            JSONObject JSONroot = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray JSONitems = JSONroot.optJSONArray("items");
            for (int i=0 ; i<30; i++){
                JSONObject eachBook = JSONitems.optJSONObject(i);
                JSONObject volumeInfo =eachBook.optJSONObject("volumeInfo");
                title =volumeInfo.optString("title");
                Log.i("Title parsed",title);
                authName = volumeInfo.optJSONArray("authors").getString(0);
                Log.i("Authorname parsed",authName);
                ratings=volumeInfo.optString("averageRating");
                Log.i("ratings parsed",ratings);
                JSONObject imageObject = volumeInfo.optJSONObject("imageLinks");
                if (imageObject!=null) {
                    imageUrl = imageObject.optString("smallThumbnail");
                    Log.i("imageurl parsed", imageUrl);
                }
                previewUrl = volumeInfo.optString("previewLink");
                Log.i("PREVIEW parsed",previewUrl);
                books.add(new Book(title,authName,ratings,imageUrl,previewUrl));
            }
        } catch (JSONException e) {
            Log.e("Problem Parsing JSON", e.getMessage());
        }
        return books;

    }

    private static URL createUrl(String stringUrl){
        Log.i("createUrl function","called");
        URL url = null ;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Url Error",e);

        }
        return url;

    }


    @SuppressLint("LongLogTag")
    private static String makeHTTPRequest(URL url){
        Log.i("makeHTTPRequest function","called");
        String jsonResponse= "";
        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try{
            httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode()==200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"Error in response code:"+httpURLConnection.getResponseCode());

            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Error getting message from JSON",e);
        }
        finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(inputStream!=null){
                try{
                    inputStream.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }

        }
        return jsonResponse;

    }
    private static String readFromInputStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line!=null){
                output.append(line);
                line=bufferedReader.readLine();
            }

        }
        return  output.toString();
    }
}
