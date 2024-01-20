package es.upm.etsiinf.freeportablesound.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import es.upm.etsiinf.freeportablesound.R;
import es.upm.etsiinf.freeportablesound.domain.APIUrlService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of how to use the APIUrlService
        Log.d(TAG, "Example URL:" + APIUrlService.getTextQueryURL("test").toString());
    }
}