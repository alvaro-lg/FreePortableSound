package es.upm.etsiinf.shared;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Images {

    @SerializedName("waveform_m")
    private URL waveform_m;

    @SerializedName("waveform_l")
    private URL waveform_l;

    @SerializedName("spectral_m")
    private URL spectral_m;

    @SerializedName("spectral_l")
    private URL spectral_l;

    @SerializedName("waveform_bw_m")
    private URL waveform_bw_m;

    @SerializedName("waveform_bw_l")
    private URL waveform_bw_l;

    @SerializedName("spectral_bw_m")
    private URL spectral_bw_m;

    @SerializedName("spectral_bw_l")
    private URL spectral_bw_l;

    public URL getWaveform_m() {
        return waveform_m;
    }

    public void setWaveform_m(URL waveform_m) {
        this.waveform_m = waveform_m;
    }

    public URL getWaveform_l() {
        return waveform_l;
    }

    public void setWaveform_l(URL waveform_l) {
        this.waveform_l = waveform_l;
    }

    public URL getSpectral_m() {
        return spectral_m;
    }

    public void setSpectral_m(URL spectral_m) {
        this.spectral_m = spectral_m;
    }

    public URL getSpectral_l() {
        return spectral_l;
    }

    public void setSpectral_l(URL spectral_l) {
        this.spectral_l = spectral_l;
    }

    public URL getWaveform_bw_m() {
        return waveform_bw_m;
    }

    public void setWaveform_bw_m(URL waveform_bw_m) {
        this.waveform_bw_m = waveform_bw_m;
    }

    public URL getWaveform_bw_l() {
        return waveform_bw_l;
    }

    public void setWaveform_bw_l(URL waveform_bw_l) {
        this.waveform_bw_l = waveform_bw_l;
    }

    public URL getSpectral_bw_m() {
        return spectral_bw_m;
    }

    public void setSpectral_bw_m(URL spectral_bw_m) {
        this.spectral_bw_m = spectral_bw_m;
    }

    public URL getSpectral_bw_l() {
        return spectral_bw_l;
    }

    public void setSpectral_bw_l(URL spectral_bw_l) {
        this.spectral_bw_l = spectral_bw_l;
    }
}
