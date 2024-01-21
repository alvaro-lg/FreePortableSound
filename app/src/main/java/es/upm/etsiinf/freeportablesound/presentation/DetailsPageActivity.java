package es.upm.etsiinf.freeportablesound.presentation;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.text.SimpleDateFormat;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.application.DownloadDetailsThread;
import es.upm.etsiinf.freeportablesound.application.PlayPreviewThread;
import es.upm.etsiinf.freeportablesound.domain.APIUrlFactory;
import es.upm.etsiinf.shared.SoundDetails;

public class DetailsPageActivity extends AppCompatActivity {

    private static int soundId;
    private String TAG = "DetailsPageActivity (id: " + String.valueOf(soundId) + ")";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page_activity);

        // Getting the sound ID from the intent
        Intent intent = getIntent();
        soundId = intent.getExtras().getInt("soundId");

        // Building up the proper query URL
        URL url = APIUrlFactory.getDetailsURL(soundId);

        // Example of how to use the APIUrlFactory
        Log.d(TAG, "Getting details for id:" + soundId + " via URL:" + url.toString());

        // Launching the background task
        DownloadDetailsThread dTask = new DownloadDetailsThread(DetailsPageActivity.this, url);
        new Thread(dTask).start();
    }

    public synchronized void prepareUIStartDownload(){
        // Updating interface elements
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
    }

    public synchronized void prepareUIFinishDownload(String details){
        // Updating interface elements
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        // Debugging
        Log.d(TAG, "Results:" + details);

        // Parsing the JSON response
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        // Actual parsing
        SoundDetails parsed_details = gson.fromJson(details, SoundDetails.class);

        // Updating the fields in the interface
        ImageView imageView = findViewById(R.id.imageHeader); // Image header
        Glide.with(this).load(Uri.parse(parsed_details.getImages().getSpectral_l()
                .toString())).into(imageView);
        TextView name = findViewById(R.id.textSoundName); // Name
        name.setText(parsed_details.getName());

        TextView tags = findViewById(R.id.textTags); // Tags
        tags.setText(parsed_details.getTags().toString());

        TextView description = findViewById(R.id.textDescription); // Description
        description.setText(parsed_details.getDescription());

        TextView dateOfCreation = findViewById(R.id.textDateOfCreation); // Date of creation
        dateOfCreation.setText(parsed_details.getCreated()
                .replace("T", " "));

        TextView license = findViewById(R.id.textLicense); // License
        license.setText(parsed_details.getLicense());

        TextView type = findViewById(R.id.textType); // Type
        type.setText(parsed_details.getType());

        TextView fileSize = findViewById(R.id.textFileSize); // File size
        fileSize.setText(parsed_details.getFilesize());

        TextView duration = findViewById(R.id.textDuration); // Duration
        duration.setText(String.valueOf(parsed_details.getDuration()));

        TextView samplerate = findViewById(R.id.textSampleRate); // Samplerate
        samplerate.setText(String.valueOf(parsed_details.getSamplerate()));

        TextView username = findViewById(R.id.textUsername); // Username
        username.setText(parsed_details.getUsername());

        TextView numDownloads = findViewById(R.id.textNumDownloads); // Number of downloads
        numDownloads.setText(String.valueOf(parsed_details.getNum_downloads()));

        TextView avgRating = findViewById(R.id.textAvgRating); // Average rating
        avgRating.setText(String.valueOf(parsed_details.getAvg_rating()));

        TextView numRatings = findViewById(R.id.textNumRatings); // Number of ratings
        numRatings.setText(String.valueOf(parsed_details.getNum_ratings()));

        // Handle play button click
        ImageButton playButton = findViewById(R.id.btnPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Building up the proper URL
                URL url = APIUrlFactory.getPreviewURL(soundId);

                // Debugging
                Log.d(TAG, "Playing sound preview from URL: " + url);

                PlayPreviewThread dTask = new PlayPreviewThread(url, playButton);
                new Thread(dTask).start();
            }
        });

        // Set a click listener for the button
        Button shareButton = findViewById(R.id.btnShare);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the sharing intent
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have a cool sound for you: " +
                        parsed_details.getUrl().toString());
                sendIntent.setType("text/plain");

                // Create a chooser to let the user pick the sharing app
                Intent shareIntent = Intent.createChooser(sendIntent, null);

                // Start the sharing activity
                startActivity(shareIntent);
            }
        });
    }
}
