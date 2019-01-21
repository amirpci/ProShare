package sidev17.siits.proshare.Model.Problem;

public class Solusi {
    String judul;
    String deskripsi;
    String orang;
    String id_solusi;
    String status;
    String statusOrang;
    String fotoOrang;
    String namaOrang;
    String timestamp;

    public Solusi() {
    }

    public String getNamaOrang() {
        return namaOrang;
    }

    public void setNamaOrang(String namaOrang) {
        this.namaOrang = namaOrang;
    }

    public String getStatusOrang() {
        return statusOrang;
    }

    public void setStatusOrang(String statusOrang) {
        this.statusOrang = statusOrang;
    }

    public String getFotoOrang() {
        return fotoOrang;
    }

    public void setFotoOrang(String fotoOrang) {
        this.fotoOrang = fotoOrang;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getOrang() {
        return orang;
    }

    public void setOrang(String orang) {
        this.orang = orang;
    }

    public String getId_solusi() {
        return id_solusi;
    }

    public void setId_solusi(String id_solusi) {
        this.id_solusi = id_solusi;
    }
}
