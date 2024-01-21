package es.upm.etsiinf.freeportablesound.application;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;

import es.upm.etsiinf.freeportablesound.infrastructure.APIConnector;
import es.upm.etsiinf.freeportablesound.presentation.DetailsPageActivity;
import es.upm.etsiinf.freeportablesound.presentation.MainPageActivity;
import es.upm.etsiinf.shared.PreviewsResponse;

public class DownloadSoundFileThread implements Runnable {

    private static final String TAG = "DownloadDetailsThread";

    // Own attributes
    private Context ctx;
    private URL url;
    private DownloadManager downloadManager;

    public DownloadSoundFileThread(Context ctx, URL url, DownloadManager downloadManager) {
        this.ctx = ctx;
        this.url = url;
        this.downloadManager = downloadManager;
    }

    @Override
    public void run() {

        // Performing the request and getting the URI for the audio file
        String result = APIConnector.performRequest(url);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        // Actual parsing
        PreviewsResponse parsed_result = gson.fromJson(result, PreviewsResponse.class);
        String download_uri = parsed_result.getPreviews().getPreview_hq_mp3();


        // Create a DownloadManager request
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(download_uri));

        Log.d(TAG, "Downloading from URL: " + download_uri.toString());

        // Set the destination directory and file name
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "audio.mp3");

        // Set the title and description visible in the notification
        request.setTitle("Downloading Audio");
        request.setDescription("Please wait...");

        // Enqueue the download and get a download ID
        ((DetailsPageActivity)ctx).download_id.set(downloadManager.enqueue(request));
    }
}
