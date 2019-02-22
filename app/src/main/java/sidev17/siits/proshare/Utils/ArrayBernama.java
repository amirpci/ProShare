package sidev17.siits.proshare.Utils;

public class ArrayBernama<Tipe>{
    private Array<Tipe> array= new Array<>(true);
    private Array<String> nama= new Array<>(true);
    //    private Array<Class> kelas= new Array<>();

    private boolean caseSensitive= true;


    public ArrayBernama(){}
    public ArrayBernama(boolean caseSensitive){
        this.caseSensitive= caseSensitive;
    }

    public void tambah(String nama, Tipe isi){
        this.array.tambah(isi);
        if(!caseSensitive)
            nama= nama.toUpperCase();
        this.nama.tambah(nama);
    }
    public void tambah(int ke, Tipe isi){
        array.tambah(isi, ke);
        sesuaikanNama(ke);
    }
    public void tambah(Tipe isi){
        sesuaikanNama(array.indekIsiPertama());
        array.tambah(isi);
    }

    private void sesuaikanNama(int ke){
        String nama= ((Integer)ke).toString();
        this.nama.tambah(nama);
    }

    public boolean isiNull(int ke){
        return array.isiNull(ke);
    }
    public Tipe ambil(String nama){
        if(!caseSensitive)
            nama= nama.toUpperCase();
        int indek= this.nama.indekAwal(nama);
        if(indek < 0) throw new RuntimeException("Tidak ada isi dengan String nama pada ArrayBernama!");
        return (Tipe) array.ambil(indek);
    }
    public Tipe ambil(int ke){
        return (Tipe) array.ambil(ke);
    }
}
