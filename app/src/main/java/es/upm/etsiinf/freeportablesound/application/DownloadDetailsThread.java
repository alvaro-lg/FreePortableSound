package es.upm.etsiinf.freeportablesound.application;

import android.content.Context;

import java.net.URL;

import es.upm.etsiinf.freeportablesound.infrastructure.APIConnector;
import es.upm.etsiinf.freeportablesound.presentation.DetailsPageActivity;
import es.upm.etsiinf.freeportablesound.presentation.MainPageActivity;

public class DownloadDetailsThread implements Runnable {

    private static final String TAG = "DownloadDetailsThread";

    // Own attributes
    private Context ctx;
    private URL url;

    public DownloadDetailsThread(Context ctx, URL url) {
        this.ctx = ctx;
        this.url = url;
    }

    @Override
    public void run() {
        // Start loading data
        turnOnProgressBar();

        // Performing the query itself
        String result = APIConnector.performRequest(url);

        // Finish loading data
        turnOffProgressBar(result);
    }

    private void turnOnProgressBar(){
        ((DetailsPageActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((DetailsPageActivity)ctx).prepareUIStartDownload();
            }
        });
    }

    private void turnOffProgressBar(String result){
        ((DetailsPageActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((DetailsPageActivity)ctx).prepareUIFinishDownload(result);
            }
        });
    }
}
