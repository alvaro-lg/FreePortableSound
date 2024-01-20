package es.upm.etsiinf.freeportablesound.application;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageButton;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.infrastructure.APIConnector;

public class PlayPreviewThread implements Runnable {

    private static final String TAG = "PlayPreviewThread";
    private URL url;
    private AppCompatImageButton playButton;

    // Variables employed for audio synchronization
    private static final MediaPlayer mediaPlayer = new MediaPlayer();
    private static boolean isCurrentlyPlaying = false;
    private static AppCompatImageButton currentlyPlayingButton;

    public PlayPreviewThread(URL url, AppCompatImageButton playButton){
        this.url = url;
        this.playButton = playButton;
    }

    @Override
    public void run() {
        if (isCurrentlyPlaying) {
            if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();
            // Variable Updating
            isCurrentlyPlaying = false;
            currentlyPlayingButton.setImageResource(R.drawable.ic_play_button);
        }

        if (currentlyPlayingButton != playButton) {
            // Variable Updating
            isCurrentlyPlaying = true;
            currentlyPlayingButton = playButton;

            // Performing the request
            String result = APIConnector.performRequest(url);

            // Changing the play button icon
            playButton.setImageResource(R.drawable.ic_pause_button);

            // Debugging
            Log.d(TAG, "Preview results: " + result);

            // Getting the URI for the preview audio
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            String preview_uri = jsonObject.getAsJsonObject("previews")
                    .get("preview-hq-mp3")
                    .getAsString();

            // Debugging
            Log.d(TAG, "Preview URI: " + preview_uri);

            // Playing the sound
            try {
                // Reset MediaPlayer to play a new sound
                mediaPlayer.reset();
                mediaPlayer.setDataSource(preview_uri);
                mediaPlayer.prepare();
                mediaPlayer.start();

                // Release the MediaPlayer after playing the sound
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        // Changing the play button icon again
                        playButton.setImageResource(R.drawable.ic_play_button);
                        isCurrentlyPlaying = false;
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error playing the requested sound: " + e.getMessage());
            }
        } else {
            // Variable Updating
            isCurrentlyPlaying = false;
            currentlyPlayingButton = null;
        }
    }
}