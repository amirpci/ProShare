package sidev17.siits.proshare.Utils.ViewTool;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import sidev17.siits.proshare.Utils.Array;

public class Batas {
    protected Array<Kotak> batas= new Array<>();
    protected Array<View> view= new Array<>();
    protected Aktifitas.PenungguSentuh pngSentuh;
    protected Aktifitas.PenungguSentuh_Internal pngSentuhInt;

    public Context konteks;

    public Batas(View view, Context c){
        konteks= c;
        tambahBatas(view);
    }

    public void tambahBatas(View v){
        view.tambah(v);
        Kotak k= new Kotak(v);
        batas.tambah(k);
//        Cek.toastRect(konteks, r);
    }
    public void kurangiBatas(View v){
        int indek= view.indekAwal(v);
        view.hapus(indek);
        batas.hapus(indek);
    }
    public void gantiBatas(View v){
        int indek= view.indekAwal(v);
        gantiBatas(v, indek);
    } public void gantiBatas(View v, int indek){
        view.ganti(indek, v);
        Kotak k= new Kotak(v);
        batas.ganti(indek, k);
    }
    public boolean menampung(float x, float y){
        boolean menampung= false;
        for(int i= 0; i<batas.ukuran(); i++)
            menampung |= batas.ambil(i).menampung(x, y);
/*
        float kiri = batas.ambil(1).kiri;
        float kanan = batas.ambil(1).kanan;
        float atas = batas.ambil(1).atas;
        float bawah = batas.ambil(1).bawah;

        Cek.toast(konteks, "diDalam= " +menampung +"\nkiri= " +kiri +" kanan= " +kanan +"\natas= " +atas +" bawah= " +bawah);
*/
//        Cek.toast(konteks, "diDalam= " +menampung +"\nrawX= " +x +" rawY= " +y);
        return menampung;
    }

    public int banyakBatas(){
        return view.ukuran();
    }

    public Array<View> ambilView(){
        return view;
    }

    public int indekView(View v){
        return view.indekAwal(v);
    }

    public void aturPenungguSentuh(Aktifitas.PenungguSentuh png){
        pngSentuh = png;
    }
    public void aturPenungguSentuh_Internal(Aktifitas.PenungguSentuh_Internal png){
        //hanya bisa sekali
        if(pngSentuhInt == null)
            pngSentuhInt= png;
    }

    public boolean sentuh(MotionEvent ev){
        if(pngSentuh != null)
            return pngSentuh.sentuh(view, ev, menampung(ev.getRawX(), ev.getRawY()));
        return false;
    }

    protected boolean sentuhInt(MotionEvent ev){
        if(pngSentuhInt != null)
            return pngSentuhInt.sentuh_Int(view, ev, menampung(ev.getRawX(), ev.getRawY()));
        return false;
    }

}
