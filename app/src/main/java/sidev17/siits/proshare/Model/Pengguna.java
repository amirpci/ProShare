package sidev17.siits.proshare.Model;

import java.io.Serializable;

public class Pengguna implements Serializable {
    private String nama;
    private String email;
    private String password;
    private String negara;
    private String bidang;
    private String status;
    private String photoProfile;
    private String keahlian;
    private String rating;
    public Pengguna(){

    }

    public Pengguna(String nama, String email, String password, String negara, String bidang, String status, String photoProfile, String keahlian, String rating) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.negara = negara;
        this.bidang = bidang;
        this.status = status;
        this.photoProfile = photoProfile;
        this.keahlian = keahlian;
        this.rating = rating;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getBidang() {
        return bidang;
    }

    public void setBidang(String bidang) {
        this.bidang = bidang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
