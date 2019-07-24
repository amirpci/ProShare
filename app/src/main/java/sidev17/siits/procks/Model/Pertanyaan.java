package sidev17.siits.procks.Model;

import java.io.Serializable;

public class Pertanyaan implements Serializable {
    private String pertanyaan;
    private String video;
    private String gambar;
    private String link;

    public Pertanyaan(){}

    public Pertanyaan(String pertanyaan, String video, String gambar, String link) {
        this.pertanyaan = pertanyaan;
        this.video = video;
        this.gambar = gambar;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
