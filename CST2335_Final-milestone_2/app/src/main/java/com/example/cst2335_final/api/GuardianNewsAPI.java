package com.example.cst2335_final.api;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.cst2335_final.R;
import com.example.cst2335_final.beans.SearchItem;
import com.example.cst2335_final.observer.Observable;
import com.example.cst2335_final.observer.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Uses Guardian News API to get search results from server
 */

public class GuardianNewsAPI extends AsyncTask<String, Integer, ArrayList<SearchItem>> implements Observable {

    ArrayList<Observer> observers;

    public GuardianNewsAPI(Observer o){
        observers = new ArrayList<>();
        registerObserver(o);
    }

    /**
     *
     * @param searchString search string from user
     * @return returns a JSONObject of result list
     * @throws IOException if url isn't correct
     * @throws JSONException if JSON isn't correct
     */
    public static JSONArray searchGuardianNews(String searchString) throws IOException, JSONException, URISyntaxException {
        URI uri = new URI("https", "content.guardianapis.com","/search","api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=" + searchString, null);
        HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
        InputStream response = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line + "\n");
        }
        String result = stringBuilder.toString();
        JSONObject jObject = new JSONObject(result);
        return jObject.getJSONObject("response").getJSONArray("results");
    }

    @Override
    protected ArrayList<SearchItem> doInBackground(String... strings) {
        ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
        try {
            JSONArray items = searchGuardianNews(strings[0]);

            for(int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                SearchItem searchItem = new SearchItem();

                searchItem.setId(i);
                searchItem.setTitle(item.getString("webTitle"));
                searchItem.setSection(item.getString("sectionName"));
                searchItem.setUrl(new URL(item.getString("webUrl")));

                searchItems.add(searchItem);
            }

            return searchItems;

        } catch (IOException | JSONException | URISyntaxException e) {
            Log.e("doInBackground", e.getMessage());
            return searchItems;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        for (Observer o: observers){
            o.update(values[0]);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<SearchItem> searchItems) {
        notifyObservers(searchItems);
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(ArrayList<SearchItem> searchItems) {
        for (Observer o: observers){
            o.update(searchItems);
        }
    }
}
