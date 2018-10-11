package sidev17.siits.proshare.Modul.Worker;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.TextView;

import sidev17.siits.proshare.R;

public class PhotoPreview extends AppCompatActivity {

    private ImageView wadahFoto;
    private TextView judulFoto;
    private DisplayMetrics metrikUtama;
    private int panjangScreen;
    private int lebarScreen;

    private ScaleGestureDetector detectorSkala;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto);

        wadahFoto= findViewById(R.id.preview_foto_gambar);
        judulFoto= findViewById(R.id.preview_foto_judul);
        getWindowManager().getDefaultDisplay().getMetrics(metrikUtama);
        panjangScreen= metrikUtama.widthPixels;
        lebarScreen= metrikUtama.heightPixels;

        detectorSkala= new ScaleGestureDetector(getBaseContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
    }

    private Bitmap sesuaikanUkuranFoto(Bitmap bm){
        int pjgAwal= bm.getWidth();
        int lbrAwal= bm.getHeight();

        int selisihPjg= (pjgAwal > panjangScreen) ? (pjgAwal/2 - panjangScreen/2) : 0;
        int selisihLbr= (lbrAwal > lebarScreen) ? (lbrAwal/2 - lebarScreen/2) : 0;

        float perbandinganPjg= (float) selisihPjg /panjangScreen;
        float perbandinganLbr= (float) selisihLbr /lebarScreen;

        float perbandinganUtama= (perbandinganPjg > perbandinganLbr) ? perbandinganPjg : perbandinganLbr;
        return skalaFoto(bm, perbandinganUtama);
    }
    private Bitmap skalaFoto(Bitmap bm, int ukuranPokok){
        int pjg= bm.getWidth();
        int lbr= bm.getHeight();

        int ukuranTerpanjang= (pjg < lbr) ? pjg : lbr;
        if(ukuranTerpanjang < ukuranPokok)
            ukuranPokok = ukuranTerpanjang;
        float skala= (float) ukuranPokok /ukuranTerpanjang;

        int pjgBaru= Math.round(pjg *skala);
        int lbrBaru= Math.round(lbr *skala);

        Bitmap fotoBaru= Bitmap.createScaledBitmap(bm, pjgBaru, lbrBaru, false);

        return fotoBaru;
    }
    private Bitmap skalaFoto(Bitmap bm, float skala){
        int pjg= bm.getWidth();
        int lbr= bm.getHeight();

        int pjgBaru= Math.round(pjg *skala);
        int lbrBaru= Math.round(lbr *skala);

        Bitmap fotoBaru= Bitmap.createScaledBitmap(bm, pjgBaru, lbrBaru, false);

        return fotoBaru;
    }
}
