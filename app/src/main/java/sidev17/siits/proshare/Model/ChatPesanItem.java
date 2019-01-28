package sidev17.siits.proshare.Model;

public class ChatPesanItem {
    private String id;
    private String pesan;
    private String waktu;
    private String pengirim;
    private String penerima;
    public  ChatPesanItem(){}
    public ChatPesanItem(String pesan, String waktu, String pengirim, String penerima){
        this.pesan = pesan;
        this.waktu = waktu;
        this.pengirim = pengirim;
        this.penerima = penerima;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getWaktu(){
        return waktu;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }
}
