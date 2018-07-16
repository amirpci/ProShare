package sidev17.siits.proshare.Model;

import java.io.Serializable;

public class Permasalahan implements Serializable {
    private Pengguna pengguna;
    private String judul;
    private String isiText;
    private String gambar;
    private String video;
    private String status;

    public Permasalahan(){

    }

    public Permasalahan(Pengguna pengguna, String judul, String isiText, String gambar, String video, String status) {
        this.pengguna = pengguna;
        this.judul = judul;
        this.isiText = isiText;
        this.gambar = gambar;
        this.video = video;
        this.status = status;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsiText() {
        return isiText;
    }

    public void setIsiText(String isiText) {
        this.isiText = isiText;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
