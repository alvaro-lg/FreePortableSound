package es.upm.etsiinf.freeportablesound.application;

import android.content.Context;

import java.net.URL;

import es.upm.etsiinf.freeportablesound.presentation.MainActivity;
import es.upm.etsiinf.freeportablesound.infrastructure.APIConnector;

public class DownloadSoundsThread implements Runnable {

    private static final String TAG = "DownloadSoundsThread";

    // Own attributes
    private Context ctx;
    private URL url;

    public DownloadSoundsThread(Context ctx, URL url){
        this.ctx = ctx;
        this.url = url;
    }

    @Override
    public void run() {
        // Start searching
        turnOnProgressBar();

        // Performing the query itself
        String result = APIConnector.performRequest(url);

        // Finish searching
        turnOffProgressBar(result);
    }

    private void turnOnProgressBar(){
        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIStartDownload();
            }
        });
    }

    private void turnOffProgressBar(String result){
        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIFinishDownload(result);
            }
        });
    }
}
