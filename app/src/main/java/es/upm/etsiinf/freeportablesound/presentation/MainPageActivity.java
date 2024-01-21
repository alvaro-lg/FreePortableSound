package es.upm.etsiinf.freeportablesound.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.domain.APIUrlFactory;
import es.upm.etsiinf.freeportablesound.application.DownloadSoundsThread;
import es.upm.etsiinf.shared.SearchResponse;
import es.upm.etsiinf.shared.Sound;

public class MainPageActivity extends AppCompatActivity {

    private static final String TAG = "MainPageActivity";
    private boolean loading = false;
    private URL nextURL = null;
    private SoundAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);

        // Set up SearchView
        SearchView searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Setting up the loading flag
                loading = true;

                // Building up the proper query URL
                URL url = APIUrlFactory.getTextQueryURL(query);

                // Example of how to use the APIUrlFactory
                Log.d(TAG, "Searching via URL:" + url.toString());

                // Launching the background task
                DownloadSoundsThread dTask = new DownloadSoundsThread(MainPageActivity.this, url);
                new Thread(dTask).start();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Not needed
                return true;
            }
        });

        // Set up ListView
        ListView listView = findViewById(R.id.listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Not needed
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Check if the last visible item is close to the end of the list
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (firstVisibleItem > 0 && lastVisibleItem >= totalItemCount - 1 && !loading) {
                    Log.d(TAG, "Loading more sounds...");

                    // Setting up the loading flag
                    loading = true;

                    // Building up the proper query URL
                    URL url = nextURL;

                    // Example of how to use the APIUrlFactory
                    Log.d(TAG, "Searching via URL:" + url.toString());

                    // Launching the background task
                    DownloadSoundsThread dTask = new DownloadSoundsThread(MainPageActivity.this, url);
                    new Thread(dTask).start();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"Item clicked: " + position);

                // Retrieve the data associated with the clicked item
                Sound clickedSound = (Sound) parent.getItemAtPosition(position);

                // Handle item click, show details in the DetailFragment
                Log.d(TAG, "Clicked sound id: " + clickedSound.getId());
                Intent intent = new Intent(MainPageActivity.this, DetailsPageActivity.class);

                // Pass the data as extras to the next activity
                intent.putExtra("soundId", clickedSound.getId());
                startActivity(intent);
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
        Gson gson = gsonBuilder.create();

        // Actual parsing
        SearchResponse parsed_results = gson.fromJson(results, SearchResponse.class);

        // Debugging
        Log.d(TAG, "Number  of retrieved results: " + parsed_results.getCount());

        // Updating the ListView and some variables
        if (nextURL == null) { // First time
            ListView listView = findViewById(R.id.listView);
            adapter = new SoundAdapter(this, parsed_results.getResults());
            listView.setAdapter(adapter);
        } else {
            adapter.addAll(parsed_results.getResults());
            adapter.notifyDataSetChanged();
        }

        // Updating variables
        nextURL = APIUrlFactory.addAuthenticationURL(parsed_results.getNext());
        loading = false;
    }
}