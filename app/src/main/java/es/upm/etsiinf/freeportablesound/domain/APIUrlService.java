package es.upm.etsiinf.freeportablesound.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import es.upm.etsiinf.freeportablesound.BuildConfig;

public class APIUrlService {

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
}
