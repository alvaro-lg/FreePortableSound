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

        // Variable initialization
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.reset();
                // Changing the play button icon again
                playButton.setImageResource(R.drawable.ic_play_button);
                isCurrentlyPlaying = false;
                currentlyPlayingButton = null;
            }
        });
    }

    @Override
    public void run() {

        // Variable initialization
        boolean was_i_playing = currentlyPlayingButton == playButton;

        if (isCurrentlyPlaying) { // Someone is playing, stop it
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            // Variable Updating
            currentlyPlayingButton.setImageResource(R.drawable.ic_play_button);
            currentlyPlayingButton = null;
            isCurrentlyPlaying = false;
        }

        if (!was_i_playing) { // If I wasn't playing, play my sound
            // Play sound
            isCurrentlyPlaying = true;
            currentlyPlayingButton = playButton;
            playButton.setImageResource(R.drawable.ic_pause_button);

            // Performing the request and getting the URI for the preview audio
            String result = APIConnector.performRequest(url);
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            String preview_uri = jsonObject.getAsJsonObject("previews")
                    .get("preview-hq-mp3")
                    .getAsString();

            // Debugging
            Log.d(TAG, "Preview URI: " + preview_uri);

            // Playing the sound
            try {
                // Reset MediaPlayer to play a new sound
                mediaPlayer.setDataSource(preview_uri);
                mediaPlayer.prepare();
                if (currentlyPlayingButton == playButton) { // Last minute check
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error playing the requested sound: " + e.getMessage());
            }
        }
    }
}