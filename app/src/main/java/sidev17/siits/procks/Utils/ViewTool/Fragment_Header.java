package sidev17.siits.procks.Utils.ViewTool;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class Fragment_Header extends Fragment {
    protected MainAct_Header actInduk;
    protected String judulHeader= "";

    @Override
    public void onAttach(Context k) {
        if(k instanceof MainAct_Header)
            actInduk= (MainAct_Header) k;
        super.onAttach(k);
    }

    public Activity ambilAktifitas(){
        return actInduk;
    }
    public String judulHeader(){
        return judulHeader;
    }

    public void daftarkanAksiTombolUtama(MainAct_Header.AksiTombolUtama a){
        actInduk.daftarkanAksiTombolUtama(a, this);
    }
    public void daftarkanPenampilTombolUtama(MainAct_Header.PenampilTombolUtama p){
        actInduk.daftarkanPenampilTombolUtama(p, this);
    }
    public void daftarkanAksiBackPress(Aktifitas.AksiBackPress a){
        actInduk.daftarkanAksiBackPress(a, this);
    }

    public abstract void initHeader();
}
