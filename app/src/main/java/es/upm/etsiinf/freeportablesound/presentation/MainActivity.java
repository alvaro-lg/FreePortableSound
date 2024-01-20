package es.upm.etsiinf.freeportablesound.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.domain.APIUrlFactory;
import es.upm.etsiinf.freeportablesound.application.DownloadSoundsThread;
import es.upm.etsiinf.shared.SearchResults;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up SearchView
        SearchView searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    // Building up the proper query URL
                    URL url = APIUrlFactory.getTextQueryURL(query);

                    // Example of how to use the APIUrlFactory
                    Log.d(TAG, "Searching via URL:" + url.toString());

                    // Launching the background task
                    DownloadSoundsThread dTask = new DownloadSoundsThread(MainActivity.this, url);
                    new Thread(dTask).start();
                } else {
                    Log.d(TAG, "Search tried with empty query");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // TODO Perform live updates of search results
                return true;
            }
        });
    }

    public synchronized void prepareUIStartDownload(){
        // Updating interface elements
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setEnabled(false);
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
    }

    public synchronized void prepareUIFinishDownload(String results){
        // Updating interface elements
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setEnabled(true);
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        // Debugging
        Log.d(TAG, "Results: " + results);

        // Parsing the JSON response
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
        Gson gson = gsonBuilder.create();

        // Actual parsing
        SearchResults parsed_results = gson.fromJson(results, SearchResults.class);

        // Debugging
        Log.d(TAG, "Number retrieved results: " + parsed_results.getCount());

        // Updating the ListView
        ListView listView = findViewById(R.id.listView);
        SoundAdapter soundAdapter = new SoundAdapter(this, parsed_results.getResults());
        listView.setAdapter(soundAdapter);
    }
}