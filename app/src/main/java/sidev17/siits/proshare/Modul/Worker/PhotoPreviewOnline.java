package sidev17.siits.proshare.Modul.Worker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.GaleriLoader;
import sidev17.siits.proshare.Utils.ScaleGesture;

public class PhotoPreviewOnline extends AppCompatActivity {

    private String pathFoto[];
    private ArrayList<String> judul;
    private ViewPager wadahFoto;
    private int indDipilih;
    private TextView judulHalaman;
    private TextView judulFoto;
    private DisplayMetrics metrikUtama;
    private int panjangScreen;
    private int lebarScreen;

    private GaleriLoader loader;
    private boolean zoomIn= false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto);

        ambilIntent();
        initLoader(pathFoto, GaleriLoader.JENIS_FOTO);

        metrikUtama= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrikUtama);
        panjangScreen= metrikUtama.widthPixels;
        lebarScreen= metrikUtama.heightPixels;

        judulHalaman = findViewById(R.id.preview_judul);
        judulFoto= findViewById(R.id.preview_foto_judul);

        AdapterHalaman adpHal= new AdapterHalaman();
        wadahFoto= findViewById(R.id.preview_foto_wadah);
        wadahFoto.setAdapter(adpHal);
        wadahFoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                judulFoto.setText(judul.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        wadahFoto.setCurrentItem(indDipilih);

        wadahFoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return zoomIn;
            }
        });
    }

    private void ambilIntent(){
        Intent intentSebelumnya= getIntent();

        pathFoto= intentSebelumnya.getStringArrayExtra("path");
        judul= intentSebelumnya.getStringArrayListExtra("judul");
        indDipilih= intentSebelumnya.getIntExtra("indDipilih", 0);
    }


    void initLoader(String pathFile[], int jenisFoto){
        loader= new GaleriLoader(this, pathFile, 18, jenisFoto,
                GaleriLoader.ELEMEN_KOSONG, GaleriLoader.ELEMEN_KOSONG);
        loader.aturUkuranPratinjau(1000);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);

        loader.aturModeBg(false);
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

    private class AdapterHalaman extends PagerAdapter{
        @Override
        public int getCount() {
            return judul.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            RelativeLayout.LayoutParams lpPanel= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView panel= new ImageView(getBaseContext());
            panel.setLayoutParams(lpPanel);

            panel.setScaleType(ImageView.ScaleType.FIT_CENTER);

            panel= (ImageView) loader.buatFoto(panel, position);

            ScaleGesture gestur= new ScaleGesture(getBaseContext());
            gestur.aturAksiZoom(new ScaleGesture.AksiZoom() {
                @Override
                public void zoomIn(View v, float scale) {
                    zoomIn= true;
                }

                @Override
                public void zoomOut(View v, float scale) {
                    zoomIn= false;
                }
            });
            panel.setOnTouchListener(gestur);


            container.addView(panel, container.getChildCount()-2);

            return panel;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((ImageView) object);
        }
    }
}
