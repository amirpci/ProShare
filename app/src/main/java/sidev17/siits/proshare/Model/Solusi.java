package sidev17.siits.proshare.Model;

import java.io.Serializable;

public class Solusi implements Serializable {
    private Pengguna pengguna;
    private String isiText;
    private String gambar;
    private String verifikator;

    public Solusi(){

    }

    public Solusi(Pengguna pengguna, String isiText, String gambar, String verifikator) {
        this.pengguna = pengguna;
        this.isiText = isiText;
        this.gambar = gambar;
        this.verifikator = verifikator;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
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

    public String getVerifikator() {
        return verifikator;
    }

    public void setVerifikator(String verifikator) {
        this.verifikator = verifikator;
    }
}
