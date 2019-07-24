package sidev17.siits.procks.Model;

import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

import sidev17.siits.procks.R;

public class Pengguna implements Serializable {

    //untuk masalah pemasangan status pengguna
    public static class Status{
        public static final int PENGGUNA_EXPERT_TERVERIFIKASI= 202;
        public static final int PENGGUNA_EXPERT= 201;
        public static final int PENGGUNA_BIASA= 200;

        public static boolean pasangIndikatorStatus(ImageView img, int status){
            if(status == PENGGUNA_BIASA){
                img.setVisibility(View.GONE);
                return true;
            } else if(status == PENGGUNA_EXPERT){
                img.setVisibility(View.VISIBLE);
                img.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                return true;
            } else if(status == PENGGUNA_EXPERT_TERVERIFIKASI){
                img.setVisibility(View.VISIBLE);
                img.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                return true;
            }
            return false;
        }

        public static int statusStr(long status){
            switch ((int) status){
                case PENGGUNA_BIASA :
                    return 0;
                case PENGGUNA_EXPERT :
                    return 1;
                case PENGGUNA_EXPERT_TERVERIFIKASI :
                    return 2;
            }
            return 0;
        }

    }

    private String nama;
    private String email;
    private String password;
    private String negara;
    private String bidang;
    private long status;
    private String photoProfile;
    private String keahlian;
    private String rating;
    public Pengguna(){

    }

    public Pengguna(String nama, String email, String password, String negara, String bidang, long status, String photoProfile, String keahlian, String rating) {
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

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
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
