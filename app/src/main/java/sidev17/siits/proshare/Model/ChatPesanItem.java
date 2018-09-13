package sidev17.siits.proshare.Model;

public class ChatPesanItem {
    private String pesan;
    private String waktu;
    private String pengirim;
    public  ChatPesanItem(){}
    public ChatPesanItem(String pesan, String waktu, String pengirim){
        this.pesan = pesan;
        this.waktu = waktu;
        this.pengirim = pengirim;
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
}
