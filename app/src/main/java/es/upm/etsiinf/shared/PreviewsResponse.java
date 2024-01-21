package es.upm.etsiinf.shared;

import com.google.gson.annotations.SerializedName;

public class PreviewsResponse {

    @SerializedName("previews")
    private Previews previews;

    public Previews getPreviews() {
        return previews;
    }

    public void setPreviews(Previews previews) {
        this.previews = previews;
    }
}