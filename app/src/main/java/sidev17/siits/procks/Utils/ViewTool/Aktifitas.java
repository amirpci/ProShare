package sidev17.siits.procks.Utils.ViewTool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import sidev17.siits.procks.Utils.Array;

public class Aktifitas extends AppCompatActivity {
    private PengelolaBatas pengResponsif;

    //PenungguSentuh yang bisa mendeteksi sentuhan diluar view
    public interface PenungguSentuh{
        boolean sentuh(Array<View> view, MotionEvent event, boolean diDalam);
    }
    public interface PenungguSentuh_Internal{
        boolean sentuh_Int(Array<View> view, MotionEvent event, boolean diDalam);
    }

    //Batas milik View yang terdaftar
    protected class PengelolaBatas {
        protected Array<Batas> batas= new Array<>();
//        protected Array<PenungguSentuh> pngSentuh= new Array<>();

        protected void tambahBatas(Batas b){
            batas.tambah(b);
        }
        protected void kurangiBatas(Batas b){
            batas.hapus(b);
        }
        protected void kurangiBatas(int indek){
            batas.hapus(indek);
        }
        protected int banyakBatas(){
            return batas.ukuran();
        }
        protected Array<Batas> ambilBatas(){
            return batas;
        }

        protected void aturPenungguSentuh(int indek, PenungguSentuh png){
            batas.ambil(indek).aturPenungguSentuh(png);
        }

        protected int indekBatas(View v){
            for(int i= 0; i< banyakBatas(); i++)
                if(batas.ambil(i).indekView(v) > -1)
                    return i;
            return -1;
        }
        protected int indekBatas(Batas b){
            return batas.indekAwal(b);
        }

        protected boolean sentuhBatas(int indek, MotionEvent event){
            return batas.ambil(indek).sentuh(event);
        }
        protected boolean sentuhBatasInt(int indek, MotionEvent event){
            return batas.ambil(indek).sentuhInt(event);
        }
    }

    public void daftarkanBatas(Batas b){
        if(pengResponsif == null)
            pengResponsif = new PengelolaBatas();
        pengResponsif.tambahBatas(b);
    } public void daftarkanBatas(View v){
        if(pengResponsif == null)
            pengResponsif = new PengelolaBatas();
        Batas b= new Batas(v, v.getContext());
        pengResponsif.tambahBatas(b);
    }

    public void aturPenungguSentuhBatas(View v, PenungguSentuh png){
        int indek= pengResponsif.indekBatas(v);
        if(indek == -1)
            return;
        pengResponsif.batas.ambil(indek).aturPenungguSentuh(png);
    } public void aturPenungguSentuhBatas(Batas b, PenungguSentuh png){
        int indek= pengResponsif.indekBatas(b);
        if(indek == -1)
            return;
        pengResponsif.batas.ambil(indek).aturPenungguSentuh(png);
    }

    public void hapusBatas(View v){
        for(int i= 0; i< pengResponsif.banyakBatas(); i++)
            if(pengResponsif.batas.ambil(i).indekView(v) > -1){
                pengResponsif.kurangiBatas(i);
                break;
            }
    } public void hapusBatas(Batas b){
        if(pengResponsif != null)
            pengResponsif.kurangiBatas(b);
    } public void hapusBatas(int indek){
        if(pengResponsif != null)
            pengResponsif.kurangiBatas(indek);
    }

    public int banyakBatas(){
        if(pengResponsif != null)
            return pengResponsif.banyakBatas();
        return 0;
    }

    public Array<Batas> ambilBatas(){
        if(pengResponsif != null)
            return pengResponsif.ambilBatas();
        return null;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean diSentuh= super.dispatchTouchEvent(ev);
        if(pengResponsif != null){
            int banyak= pengResponsif.banyakBatas();
            for(int i = 0; i< banyak; i++){
                diSentuh |= pengResponsif.sentuhBatas(i, ev);
                diSentuh |= pengResponsif.sentuhBatasInt(i, ev);
            }
        }
        return diSentuh;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("requestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }

/*
================================
AksiBackPress
================================
*/
    public interface AksiBackPress{
        boolean backPress();
    }
    private Array<AksiBackPress> aksiBackPress;
    protected void initAksiBackPress(){
        aksiBackPress= new Array<>();
        aksiBackPress.aturPenungguTraverse(buat_penungguBackPress());
    }
    protected final Array.PenungguTraverse<Void, Boolean, Boolean> buat_penungguBackPress(){
        return new Array.PenungguTraverse<Void, Boolean, Boolean>(){
            @Override
            public Boolean traverse(Array array, int indek, Object isiArray) {
                return ((AksiBackPress)isiArray).backPress();
            }

            @Override
            public Boolean akhirTraverse(Boolean... hasilTraverse) {
                boolean hasil= false;
                for(int i= 0; i< hasilTraverse.length; i++)
                    hasil |= hasilTraverse[i];
                return hasil;
            }
        };
    }
    public void daftarkanAksiBackPress(AksiBackPress a){
        if(aksiBackPress == null)
            initAksiBackPress();
        aksiBackPress.tambah(a);
    }
    public void hapusAksiBackPress(AksiBackPress a){
        aksiBackPress.hapus(a);
    }
    public void bersihkanAksiBackPress(){
        aksiBackPress.hapusSemua();
    }


    protected interface AksiBackPress_Internal{
        void backPress_Int();
    }
    private AksiBackPress_Internal aksiBackPress_Int;
    protected void aturAksiBackPress_Int(AksiBackPress_Internal a){
        aksiBackPress_Int= a;
    }

    @Override
    public void onBackPressed() {
        boolean keAnak= false;
        if(aksiBackPress != null)
            keAnak |= (Boolean) aksiBackPress.traverse();
        if(!keAnak)
            backPress_int();
    }

    protected final void backPress_int(){
        if(aksiBackPress_Int != null)
            aksiBackPress_Int.backPress_Int();
        else
            super.onBackPressed();
    }

}
