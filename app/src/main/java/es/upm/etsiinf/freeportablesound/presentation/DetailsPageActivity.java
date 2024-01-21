package es.upm.etsiinf.freeportablesound.presentation;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.application.DownloadDetailsThread;
import es.upm.etsiinf.freeportablesound.application.DownloadSoundFileThread;
import es.upm.etsiinf.freeportablesound.application.PlayPreviewThread;
import es.upm.etsiinf.freeportablesound.domain.APIUrlFactory;
import es.upm.etsiinf.shared.SoundDetails;

public class DetailsPageActivity extends AppCompatActivity {

    private static int soundId;
    private static String TAG = "DetailsPageActivity (id: " + String.valueOf(soundId) + ")";

    // For synchronization with the download thread
    public AtomicLong download_id = new AtomicLong();

    // For notifications
    private NotificationHandler handler;
    private static int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page_activity);

        // Getting the sound ID from the intent
        Intent intent = getIntent();
        soundId = intent.getExtras().getInt("soundId");
        handler = new NotificationHandler(this);

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
        tags.setText(parsed_details.getTags().toString().replace("[", "")
                .replace("]", ""));

        TextView description = findViewById(R.id.textDescription); // Description
        description.setText(parsed_details.getDescription());

        TextView dateOfCreation = findViewById(R.id.textDateOfCreation); // Date of creation
        dateOfCreation.setText(parsed_details.getCreated()
                .replace("T", " "));

        TextView license = findViewById(R.id.textLicense); // License
        license.setText(parsed_details.getLicense());

        TextView type = findViewById(R.id.textType); // Type
        type.setText("File type: " + parsed_details.getType());

        TextView fileSize = findViewById(R.id.textFileSize); // File size
        fileSize.setText("File size:" + String.valueOf((float) parsed_details.getFilesize() /
                1024.0) + " kB");

        TextView duration = findViewById(R.id.textDuration); // Duration
        duration.setText("Duration:" + String.valueOf(parsed_details.getDuration()) + " s");

        TextView samplerate = findViewById(R.id.textSampleRate); // Samplerate
        samplerate.setText("Sample rate: " + String.valueOf(parsed_details.getSamplerate() / 1000)
                + " kHz");

        TextView username = findViewById(R.id.textUsername); // Username
        username.setText(parsed_details.getUsername());

        TextView numDownloads = findViewById(R.id.textNumDownloads); // Number of downloads
        numDownloads.setText("Number of downloads: " + String.valueOf(parsed_details.getNum_downloads()));

        TextView avgRating = findViewById(R.id.textAvgRating); // Average rating
        avgRating.setText("Average rating: " + String.valueOf(parsed_details.getAvg_rating()));

        TextView numRatings = findViewById(R.id.textNumRatings); // Number of ratings
        numRatings.setText("Number of ratings: " + String.valueOf(parsed_details.getNum_ratings()));

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

        Button downloadButton = findViewById(R.id.btnDownload);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Implement download of full sound (requires Oauth2)
                URL download_url = APIUrlFactory.getPreviewURL(soundId);

                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                BroadcastReceiver receiver = new Receiver();
                registerReceiver(receiver, filter);

                DownloadSoundFileThread dTask = new DownloadSoundFileThread(
                        DetailsPageActivity.this, download_url,
                        (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE));
                new Thread(dTask).start();

                // Display a Toast message
                Toast.makeText(DetailsPageActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (reference == download_id.get()) {
                //Toast.makeText(context, "...download finished", Toast.LENGTH_SHORT).show();

                // Get the URI of the downloaded file
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri fileUri = downloadManager.getUriForDownloadedFile(reference);

                Log.d(TAG, "Downloading file finished: " + fileUri);

                Notification.Builder nBuilder =  handler.createNotification("FreePortableSound",
                        "Finished downloading " + fileUri.toString(), true, fileUri);
                handler.getManager().notify(count++, nBuilder.build());
                handler.publishGroup(true);
            }
        }
    }
}
