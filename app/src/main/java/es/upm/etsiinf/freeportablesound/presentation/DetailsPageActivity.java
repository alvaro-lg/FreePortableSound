package es.upm.etsiinf.freeportablesound.presentation;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.upm.etsiinf.freeportablesound.R;

public class DetailsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page_activity);

        // Getting the sound ID from the intent
        Intent intent = getIntent();
        int soundId = intent.getExtras().getInt("soundId");

        // Parsing data
        TextView textSoundName = findViewById(R.id.textSoundId);
        textSoundName.setText(String.valueOf(soundId));
    }
}
