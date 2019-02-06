package sidev17.siits.proshare.Utils.ViewTool;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ViewTool.Aktifitas;
import sidev17.siits.proshare.ViewPagerAdapter;

public class MainAct_Header extends Aktifitas {

    protected ViewPager mvPager;

    private TextView judulHeader;
    private TextView tambahanJudulHeader;
    private ImageView opsiHeader[]= new ImageView[3];
    private String warnaOpsiHeader[]= new String[3];


    public boolean bolehInitHeader= true;

/*
===============================
Khusus untuk kustomasi header
===============================
*/
    //dipanggil setelah onCreate() dan setContentView()
    public void initHeader(){
        judulHeader= findViewById(R.id.header_judul);
        judulHeader.setText("");
        tambahanJudulHeader= findViewById(R.id.header_tambahan);
        tambahanJudulHeader.setText("");
        opsiHeader[0]= findViewById(R.id.opsi_header_1);
        opsiHeader[1]= findViewById(R.id.opsi_header_2);
        opsiHeader[2]= findViewById(R.id.opsi_header_3);
        aturWarnaOpsiHeader_Default(0);
        aturGambarOpsiHeader_Null(0);
    }
    public void aturJudulHeader(String judul){
//        Toast.makeText(this, "judul= " +judul, Toast.LENGTH_SHORT).show();
        judulHeader.setText(judul);
    }
    public void aturTambahanHeader(String teks){
        tambahanJudulHeader.setText(teks);
    }
    public void aturGambarOpsiHeader(int opsiKe, int resId){
        opsiHeader[opsiKe].setImageResource(resId);
        opsiHeader[opsiKe].getDrawable().setTint(Color.parseColor(warnaOpsiHeader[opsiKe]));
    }
    public void aturGambarOpsiHeader(int resId[]){
        int batas= resId.length;
        if(batas >= 3)
            batas= 3;
        int i;
        for(i= 0; i< batas; i++)
            aturGambarOpsiHeader(i, resId[i]);
        aturGambarOpsiHeader_Null(i);
    }
    public void aturGambarOpsiHeader_Null(int mulai){
        aturKlikOpsiHeader_Null(mulai);
        while(mulai < opsiHeader.length)
            opsiHeader[mulai++].setImageDrawable(null);
    }

    public void aturWarnaOpsiHeader(int opsiKe, String warna){
        warnaOpsiHeader[opsiKe]= warna;
    }
    public void aturWarnaOpsiHeader(String warna){
        for(int i= 0; i< warnaOpsiHeader.length; i++)
            warnaOpsiHeader[i]= warna;
    }
    public void aturWarnaOpsiHeader(String warna[]){
        int batas= warna.length;
        if(batas >= 3)
            batas= 3;
        int i;
        for(i= 0; i< batas; i++)
            warnaOpsiHeader[i]= warna[i];
        aturGambarOpsiHeader_Null(i);
    }
    public void aturWarnaOpsiHeader_Default(int mulai){
        while(mulai < opsiHeader.length)
            warnaOpsiHeader[mulai++]= "#000000";
    }

    public void aturKlikOpsiHeader(int opsiKe, View.OnClickListener l){
        opsiHeader[opsiKe].setOnClickListener(l);
    }
    public void aturKlikOpsiHeader(View.OnClickListener l[]){
        int batas= l.length;
        if(batas >= 3)
            batas= 3;
        int i;
        for(i= 0; i< batas; i++)
            aturKlikOpsiHeader(i, l[i]);
        aturKlikOpsiHeader_Null(i);
    }
    public void aturKlikOpsiHeader_Null(int mulai){
        while(mulai < opsiHeader.length)
            aturKlikOpsiHeader(mulai++, null);
    }

    public int halamanSekarang(){
        if(mvPager != null)
            return mvPager.getCurrentItem();
        return -1;
    }
/*
===============================
-AKHIR-Khusus untuk kustomasi header
===============================
*/
}
