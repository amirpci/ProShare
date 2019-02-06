package sidev17.siits.proshare.Utils.ViewTool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

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

    public abstract void initHeader();
}
