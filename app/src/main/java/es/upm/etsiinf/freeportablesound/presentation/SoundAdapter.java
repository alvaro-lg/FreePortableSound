package es.upm.etsiinf.freeportablesound.presentation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.appcompat.widget.AppCompatImageButton;

import java.net.URL;
import java.util.List;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.application.PlayPreviewThread;
import es.upm.etsiinf.freeportablesound.domain.APIUrlFactory;
import es.upm.etsiinf.shared.Sound;

public class SoundAdapter extends ArrayAdapter<Sound> {

    private static final String TAG = "SoundAdapter";

    private final Context context;
    private final List<Sound> soundList;

    public SoundAdapter(Context context, List<Sound> soundList) {
        super(context, R.layout.sound_list_item, soundList);
        this.context = context;
        this.soundList = soundList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.sound_list_item, parent, false);

        // Parsing data
        TextView textSoundName = rowView.findViewById(R.id.textSoundName);
        textSoundName.setText(soundList.get(position).getName());

        TextView textSoundUsername = rowView.findViewById(R.id.textSoundUsername);
        textSoundUsername.setText(soundList.get(position).getUsername());

        // Handle play button click
        AppCompatImageButton playButton = rowView.findViewById(R.id.btnPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Building up the proper URL
                URL url = APIUrlFactory.getPreviewURL(soundList.get(position).getId());

                // Debugging
                Log.d(TAG, "Playing sound preview from URL: " + url);

                PlayPreviewThread dTask = new PlayPreviewThread(url, playButton);
                new Thread(dTask).start();
            }
        });

        return rowView;
    }
}