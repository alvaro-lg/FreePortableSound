package es.upm.etsiinf.shared;

import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SoundDetails {

    @SerializedName("images")
    private Images images;

    @SerializedName("name")
    private String name;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("description")
    private String description;

    @SerializedName("created")
    private String created;

    @SerializedName("license")
    private String license;

    @SerializedName("type")
    private String type;
    @SerializedName("filesize")
    private String filesize;

    @SerializedName("duration")
    private float duration;

    @SerializedName("samplerate")
    private float samplerate;

    @SerializedName("username")
    private String username;

    @SerializedName("num_downloads")
    private int num_downloads;

    @SerializedName("avg_rating")
    private float avg_rating;

    @SerializedName("num_ratings")
    private int num_ratings;

    @SerializedName("download")
    private URL download;

    @SerializedName("url")
    private URL url;

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getSamplerate() {
        return samplerate;
    }

    public void setSamplerate(float samplerate) {
        this.samplerate = samplerate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNum_downloads() {
        return num_downloads;
    }

    public void setNum_downloads(int num_downloads) {
        this.num_downloads = num_downloads;
    }

    public float getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(float avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getNum_ratings() {
        return num_ratings;
    }

    public void setNum_ratings(int num_ratings) {
        this.num_ratings = num_ratings;
    }

    public URL getDownload() {
        return download;
    }

    public void setDownload(URL download) {
        this.download = download;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}