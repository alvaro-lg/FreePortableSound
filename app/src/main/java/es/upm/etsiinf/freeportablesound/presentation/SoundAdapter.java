package es.upm.etsiinf.freeportablesound.presentation;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.widget.AppCompatImageButton;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.application.DownloadSoundsThread;
import es.upm.etsiinf.freeportablesound.application.PlayPreviewThread;
import es.upm.etsiinf.freeportablesound.domain.APIUrlFactory;
import es.upm.etsiinf.freeportablesound.infrastructure.APIConnector;
import es.upm.etsiinf.shared.Sound;

public class SoundAdapter extends ArrayAdapter<Sound> {

    private static final String TAG = "SoundAdapter";

    private final Context context;
    private final Sound[] soundList;

    public SoundAdapter(Context context, Sound[] soundList) {
        super(context, R.layout.item_sound, soundList);
        this.context = context;
        this.soundList = soundList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_sound, parent, false);

        // Parsing data
        TextView textSoundName = rowView.findViewById(R.id.textSoundName);
        textSoundName.setText(soundList[position].getName());

        TextView textSoundUsername = rowView.findViewById(R.id.textSoundUsername);
        textSoundUsername.setText(soundList[position].getUsername());

        // Handle play button click
        AppCompatImageButton playButton = rowView.findViewById(R.id.btnPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Building up the proper URL
                URL url = APIUrlFactory.getPreviewURL(soundList[position].getId());

                // Debugging
                Log.d(TAG, "Playing sound preview from URL: " + url);

                PlayPreviewThread dTask = new PlayPreviewThread(url, playButton);
                new Thread(dTask).start();
            }
        });

        return rowView;
    }
}