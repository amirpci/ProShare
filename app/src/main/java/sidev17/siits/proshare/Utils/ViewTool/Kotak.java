package sidev17.siits.proshare.Utils.ViewTool;


import android.view.View;

public class Kotak {
    public float kiri;
    public float kanan;
    public float atas;
    public float bawah;
    private View view;


    public Kotak(){}
    public Kotak(Kotak k){
        if(k == null)
            kiri= kanan= atas= bawah= 0;
        else{
            kiri= k.kiri;
            kanan= k.kanan;
            atas= k.atas;
            bawah= k.bawah;
        }
    }
    public Kotak(float kiri, float atas, float kanan, float bawah){
        this.kiri= kiri;
        this.kanan= kanan;
        this.atas= atas;
        this.bawah= bawah;
        urutkan();
    }
    public Kotak(View v){
        view= v;

        int posisi[]= new int[2];
        view.getLocationOnScreen(posisi);

        this.kiri= posisi[0];
        this.kanan= posisi[0] +view.getWidth();
        this.atas= posisi[1];
        this.bawah= posisi[1] +view.getHeight();
    }


    public void atur(Kotak k){
        if(k == null)
            kiri= kanan= atas= bawah= 0;
        else{
            kiri= k.kiri;
            kanan= k.kanan;
            atas= k.atas;
            bawah= k.bawah;
        }
    }
    public void atur(float kiri, float atas, float kanan, float bawah){
        this.kiri= kiri;
        this.kanan= kanan;
        this.atas= atas;
        this.bawah= bawah;
        urutkan();
    }

    public void dariView(View v){
        view= v;

        int posisi[]= new int[2];
        view.getLocationOnScreen(posisi);

        this.kiri= posisi[0];
        this.kanan= posisi[0] +view.getWidth();
        this.atas= posisi[1];
        this.bawah= posisi[1] +view.getHeight();
    }

    private void urutkan(){
        if(kiri > kanan){
            float smtr= kiri;
            kiri= kanan;
            kanan= smtr;
        }
        if(atas > bawah){
            float smtr= atas;
            atas= bawah;
            bawah= smtr;
        }
    }

    public boolean menampung(float kiri, float atas, float kanan, float bawah){
        if(kiri > kanan){
            float smtr= kiri;
            kiri= kanan;
            kanan= smtr;
        }
        if(atas > bawah){
            float smtr= atas;
            atas= bawah;
            bawah= smtr;
        }
        perbarui();
        return (this.kiri < this.kanan && this.atas < this.bawah) && //jika kosong, maka selalu SALAH (false)
                (kiri >= this.kiri && kanan <= this.kanan) &&
                (atas >= this.atas && bawah <= this.bawah);
    }
    public boolean menampung(float x, float y){
        perbarui();
        return (kiri < kanan && atas < bawah) && //jika kosong, maka selalu SALAH (false)
                (x >= kiri && x <= kanan) && (y >= atas && y <= bawah);
    }

    public void perbarui(){
        if(view != null){
            int posisi[]= new int[2];
            view.getLocationOnScreen(posisi);

            this.kiri= posisi[0];
            this.kanan= posisi[0] +view.getWidth();
            this.atas= posisi[1];
            this.bawah= posisi[1] +view.getHeight();
        }
    }

    public boolean sama(Kotak k){
        if(this == k) return true;
        else if(k == null) return false;

        return kiri == k.kiri && kanan == k.kanan && atas == k.atas && bawah == k.bawah;
    }

    @Override
    public boolean equals(Object obj) {
        if(getClass() != obj.getClass()) return false;
        return sama((Kotak) obj);
    }
}
