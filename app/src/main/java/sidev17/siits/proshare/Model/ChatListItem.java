package sidev17.siits.proshare.Model;

public class ChatListItem {
    private String nama;
    private long jam;
    private String prewMessage;
    private String idPesan;
    private Pengguna orang;
    public ChatListItem(){

    }
    public ChatListItem(String nama, long jam, String prewMessage, String idPesan, Pengguna orang){
        this.nama = nama;
        this.jam = jam;
        this.prewMessage = prewMessage;
        this.idPesan = idPesan;
        this.orang = orang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public long getJam() {
        return jam;
    }

    public void setJam(long jam) {
        this.jam = jam;
    }

    public String getPrewMessage() {
        return prewMessage;
    }

    public void setPrewMessage(String prewMessage) {
        this.prewMessage = prewMessage;
    }

    public String getIdPesan() {
        return idPesan;
    }

    public void setIdPesan(String idPesan) {
        this.idPesan = idPesan;
    }

    public Pengguna getOrang() {
        return orang;
    }

    public void setOrang(Pengguna orang) {
        this.orang = orang;
    }
}
