package es.upm.etsiinf.freeportablesound.domain;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import es.upm.etsiinf.freeportablesound.BuildConfig;

public class APIUrlFactory {

    // API key for performing requests
    private static final String API_KEY = BuildConfig.API_KEY;

    /**
     * Method that builds up the proper URL for the requested text query.
     * @param query text to be searched.
     * @return URL with properly formatted.
     */
    public static URL getTextQueryURL(String query) {
        try {
            // Building up the url for the required query
            return new URL("https://freesound.org/apiv2/search/text/?query=" + query +
                    "&token=" + API_KEY);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL addAuthenticationURL(String query) {
        try {
            // Building up the url for the required query
            return new URL( query + "&token=" + API_KEY);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public static URL getPreviewURL(int id) {
        try {
            // Building up the url for the required query
            return new URL("https://freesound.org/apiv2/sounds/" + id + "/?fields=previews" +
                    "&token=" + API_KEY);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL getDetailsURL(int id) {
        try {
            // Building up the url for the required query
            return new URL("https://freesound.org/apiv2/sounds/" + id + "/?fields=" +
                    "images,name,tags,description,created,license,type,filesize,duration," +
                    "samplerate,username,num_downloads,avg_rating,num_ratings,download," +
                    "peviews,url" + "&token=" + API_KEY);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
