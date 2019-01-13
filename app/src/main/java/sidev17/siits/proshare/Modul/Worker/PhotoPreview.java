package sidev17.siits.proshare.Modul.Worker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.GaleriLoader;
import sidev17.siits.proshare.Utils.ParcelHolder;
import sidev17.siits.proshare.Utils.ScaleGesture;
import sidev17.siits.proshare.Utils.UnserializableHolder;

public class PhotoPreview extends AppCompatActivity {

    private String pathFoto[];
    private ArrayList<String> judul;
//    private ArrayList<Bitmap> foto;
    private ViewPager wadahFoto;
    private int indDipilih;
    private TextView judulHalaman;
    private TextView judulFoto;
    private DisplayMetrics metrikUtama;
    private int panjangScreen;
    private int lebarScreen;

    private GaleriLoader loader;
    private boolean zoomIn= false;

    private ScaleGestureDetector detectorSkala;

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

//        ParcelHolder<Bitmap> holder= intentSebelumnya.getParcelableExtra("foto");
//        foto= intentSebelumnya.getParcelableArrayListExtra("foto");
        pathFoto= intentSebelumnya.getStringArrayExtra("path");
        judul= intentSebelumnya.getStringArrayListExtra("judul");
        indDipilih= intentSebelumnya.getIntExtra("indDipilih", 0);
    }


    void initLoader(String pathFile[], int jenisFoto){
//        inisiasiArrayDipilih(pathFoto.length);
        loader= new GaleriLoader(this, pathFile, 18, jenisFoto,
                GaleriLoader.ELEMEN_KOSONG, GaleriLoader.ELEMEN_KOSONG);
//        loader.bisaDiScale(true);
        loader.aturUkuranPratinjau(1000);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);
//        loader.aturElemenImg(R.id.tambah_cell_pratinjau);

        loader.aturModeBg(false);

/*
        loader.aturAksiPilihFoto(new GaleriLoader.AksiPilihFoto() {
            @Override
            public void pilihFoto(View v, int posisi) {
                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText(Integer.toString(loader.ambilUrutanDipilih(posisi)));
                noUrut.getBackground().setTint(getResources().getColor(R.color.biruLaut));
                noUrut.getBackground().setAlpha(255);
            }
            @Override
            public void batalPilihFoto(View v, int posisi) {
                int indDipilih[]= loader.ambilUrutanDipilih();
                ArrayList<View> viewDipilih= loader.ambilViewDipilih();
                int indYgDibatalkan= loader.ambilUrutanDipilih(posisi) -1;
                int indBaru= indYgDibatalkan +1;

                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText("");
                noUrut.getBackground().setAlpha(0);

                for(int i= 0; i< indDipilih.length; i++) {
                    if(i > indYgDibatalkan) {
                        noUrut = viewDipilih.get(i).findViewById(R.id.tambah_cell_centang_no);
                        noUrut.setText(Integer.toString(indBaru++));
                    }
                }
            }
        });
*/
/*
        loader.aturAksiBuffer(new GaleriLoader.AksiBuffer() {
            @Override
            public void bufferThumbnail(final int posisi, final int jmlBuffer) {
                View view= loader.ambilView(posisi);
                final int lebar= view.getLayoutParams().width;

                final TextView indDipilih = view.findViewById(R.id.tambah_cell_centang_no);
                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                indDipilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!indDipilih.isSelected()) {
                            loader.pilihFoto(posisi);
                            indDipilih.setSelected(true);
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        } else {
                            loader.batalPilihFoto(posisi);
                            indDipilih.setSelected(false);
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() == 0)
                                aturTinggiGrid(liveQuestion, 0);
                            else if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        }
                    }
                });
            }
            @Override
            public void bufferUtama(int posisi, int jmlBuffer) {

            }
        });
*/
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
/*
            ViewGroup.LayoutParams lpInduk= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            RelativeLayout induk= new RelativeLayout(getBaseContext());
            induk.setLayoutParams(lpInduk);
*/
            RelativeLayout.LayoutParams lpPanel= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView panel= new ImageView(getBaseContext());
            panel.setLayoutParams(lpPanel);
//            panel.setImageBitmap(foto.get(position));
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
/*
            RelativeLayout.LayoutParams lpJudul= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpJudul.setMargins(20, 20, 20, 0);
            TextView judulFoto= new TextView(getBaseContext());
            judulFoto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            judulFoto.setTextColor(Color.parseColor("#FFFFFF"));
            judulFoto.setLayoutParams(lpJudul);
            judulFoto.setText(judul.get(position) +" " +position);
*/

//            induk.addView(panel);

            container.addView(panel, container.getChildCount()-2);

            return panel;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((ImageView) object);
        }
    }
}
