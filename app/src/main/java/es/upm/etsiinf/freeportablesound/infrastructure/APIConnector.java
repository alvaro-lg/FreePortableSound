package es.upm.etsiinf.freeportablesound.infrastructure;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class APIConnector {

    private static final String TAG = "APIConnector";

    public static String performRequest(URL url) {
        // Variable initialization (warning avoidance)
        InputStream response_data = null;
        HttpURLConnection urlConnection = null;

        // Performing the query itself
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            response_data = urlConnection.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to the API: " + e.getMessage());
        }

        if (response_data != null) {
            // Reading the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(response_data));
            StringBuilder result = new StringBuilder();
            String line;

            // Building up the response string
            try {
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                urlConnection.disconnect();
            } catch (IOException e) {
                Log.e(TAG, "Error reading the response: " + e.getMessage());
            }
            return result.toString();
        } else {
            return "";
        }
    }
}
