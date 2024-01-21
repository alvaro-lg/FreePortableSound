package es.upm.etsiinf.shared;

import com.google.gson.annotations.SerializedName;

public class Previews {

    @SerializedName("preview-hq-mp3")
    private String preview_hq_mp3;

    @SerializedName("preview-hq-ogg")
    private String preview_hq_ogg;

    @SerializedName("preview-lq-mp3")
    private String preview_lq_mp3;

    @SerializedName("preview-lq-ogg")
    private String preview_lq_ogg;

    public String getPreview_hq_mp3() {
        return preview_hq_mp3;
    }

    public void setPreview_hq_mp3(String preview_hq_mp3) {
        this.preview_hq_mp3 = preview_hq_mp3;
    }

    public String getPreview_hq_ogg() {
        return preview_hq_ogg;
    }

    public void setPreview_hq_ogg(String preview_hq_ogg) {
        this.preview_hq_ogg = preview_hq_ogg;
    }

    public String getPreview_lq_mp3() {
        return preview_lq_mp3;
    }

    public void setPreview_lq_mp3(String preview_lq_mp3) {
        this.preview_lq_mp3 = preview_lq_mp3;
    }

    public String getPreview_lq_ogg() {
        return preview_lq_ogg;
    }

    public void setPreview_lq_ogg(String preview_lq_ogg) {
        this.preview_lq_ogg = preview_lq_ogg;
    }
}
