package es.upm.etsiinf.shared;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResults {

    @SerializedName("count")
    private int count;

    @SerializedName("previous")
    private String previous;

    @SerializedName("next")
    private String next;

    @SerializedName("results")
    private List<Sound> results;

    // Getters and setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Sound> getResults() {
        return results;
    }

    public void setResults(List<Sound> results) {
        this.results = results;
    }
}
