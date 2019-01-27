package sidev17.siits.proshare.Modul.Worker;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import java.util.ArrayList;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ViewTool.GaleriLoader;
import sidev17.siits.proshare.Utils.ScaleGesture;

public class GaleriPreview extends AppCompatActivity {

    private final int POSISI_DI_SINI= 10;
    private final int POSISI_SEBELUM= 11;
    private final int POSISI_SESUDAH= 12;

    public static final int PATH_DEFAULT= 21;
    public static final int PATH_TERTENTU= 22;

    public static final int JENIS_AMBIL_BANYAK= 31;
    public static final int JENIS_AMBIL_SATU= 30;

    private String pathFoto[];
    private ArrayList<String> judul;
    private int batasHalaman= 10;
    private int posisiLoader[];
    private boolean adapterDiAwal= true;
    private boolean adapterDiAkhir= true;
    private boolean bisaDiPerluas= false; //menandakan bahwa halaman cuma 1 / pathFoto tidak lebih panjang dari batasHalaman
    private boolean indikatorDitampilkan= true;
    private int kelompokHalamanSkrg= 0;
    private int posisiFoto;
    private int jenisPath;

    private int jenisPengambilan= JENIS_AMBIL_BANYAK;
    private int posisiTrahir= -1;

    private Array<Integer> posisiAwal= new Array<>();
    private boolean samaDenganAwal= true;
    private int perbedaanDipilih= 0;

//    private ArrayList<Bitmap> foto;
    private ViewPager wadahFoto;
    private TextView judulHalaman;
    private TextView judulFoto;
    private ImageView tmbOk;

    private boolean judulDitampilkan= true;
    private Path pathHeader= new Path();
    private Path pathFooter= new Path();
    private ObjectAnimator animatorHeader;
    private ObjectAnimator animatorFooter;
    private View header;
    private View footer;

    private TextView noUrutDipilih;
    private ImageView centangDipilih;
    private View indikatorDipilih;

    private GaleriLoader loader;
    private boolean zoomIn= false;
    private int jenisFoto;

    private DisplayMetrics metrikUtama;
    private int panjangScreen;
    private int lebarScreen;


    private ScaleGestureDetector detectorSkala;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto);

        judulHalaman = findViewById(R.id.preview_judul);
        judulFoto= findViewById(R.id.preview_foto_judul);
        noUrutDipilih = findViewById(R.id.tambah_cell_centang_no);
        centangDipilih= findViewById(R.id.tambah_cell_centang_gambar);
        indikatorDipilih= findViewById(R.id.cell_centang);
        wadahFoto= findViewById(R.id.preview_foto_wadah);

        initTmbOk();
        initHeaderFooter();
        pengaturanAwal();
        initLoader(pathFoto, jenisFoto);

        metrikUtama= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrikUtama);
        panjangScreen= metrikUtama.widthPixels;
        lebarScreen= metrikUtama.heightPixels;


        indikatorDipilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!noUrutDipilih.isSelected()){
                    loader.pilihFoto(posisiFoto);
                    pasangIndikatorDipilih(true);
                } else{
                    loader.batalPilihFoto(posisiFoto);
                    pasangIndikatorDipilih(false);
                }
            }
        });

        wadahFoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                judulFoto.setText(judul.get(position));
                posisiFoto = position;

                pasangIndikatorDipilih(loader.fotoDipilih(posisiFoto));

//                Toast.makeText(GaleriPreview.this, "posisi= " + posisiFoto, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        pilihHalaman(posisiFoto);
        AdapterHalaman adpHal= new AdapterHalaman();
        wadahFoto.setAdapter(adpHal);
        wadahFoto.setCurrentItem(posisiFoto);

        wadahFoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return zoomIn;
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void ambilIntent(){
        Intent intentSebelumnya= getIntent();

//        ParcelHolder<Bitmap> holder= intentSebelumnya.getParcelableExtra("foto");
//        foto= intentSebelumnya.getParcelableArrayListExtra("foto");
        pathFoto= intentSebelumnya.getStringArrayExtra("path");
        jenisPath= (pathFoto== null) ? PATH_DEFAULT
                    : intentSebelumnya.getIntExtra("jenisPath", PATH_TERTENTU);

        jenisPengambilan= intentSebelumnya.getIntExtra("jenisPengambilan", JENIS_AMBIL_BANYAK);
        Toast.makeText(this, "jenisPengambilan= " +jenisPengambilan, Toast.LENGTH_SHORT).show();

        jenisFoto= intentSebelumnya.getIntExtra("jenisFoto", GaleriLoader.JENIS_FOTO);

        if(jenisPath == PATH_DEFAULT) {
            pathFoto= ambilPathGambar();
            judul= ambilDaftarJudul();
        } else if(jenisPath == PATH_TERTENTU){
            judul= intentSebelumnya.getStringArrayListExtra("judul");
            if(judul == null)
                judul= ambilDaftarJudul();
        }

        posisiFoto = intentSebelumnya.getIntExtra("posisiFoto", 0);
        indikatorDitampilkan= intentSebelumnya.getBooleanExtra("indikatorDitampilkan", true);
    }

    private void pengaturanAwal(){
        ambilIntent();
        if(pathFoto.length > batasHalaman) {
            adapterDiAwal= true;
            adapterDiAkhir= false;
            bisaDiPerluas= true;
        }
        posisiLoader= (bisaDiPerluas) ? new int[batasHalaman] : new int[pathFoto.length];
        if(!indikatorDitampilkan){
            ((ViewGroup) noUrutDipilih.getParent()).setVisibility(View.GONE);
            tmbOk.setVisibility(View.GONE);
        }
    }

    private void initHeaderFooter(){
        header= findViewById(R.id.preview_header);
        footer= findViewById(R.id.preview_footer);
    }

    private void initTmbOk(){
        tmbOk= findViewById(R.id.preview_ok);
        tmbOk(false);
        tmbOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!samaDenganAwal){
                    Intent hasil= new Intent();

                    int indDipilih[][]= loader.ambilUrutanDipilih();
                    hasil.putExtra("posisiDipilih", indDipilih[0]);
                    hasil.putExtra("urutanDipilih", indDipilih[1]);
                    hasil.putExtra("posisiFoto", posisiFoto);

                    setResult(RESULT_OK, hasil);
                    finish();
                }
            }
        });
    }
    private void tmbOk(boolean bisa){
        if(bisa){
            samaDenganAwal= false;
            tmbOk.getDrawable().setTint(getResources().getColor(R.color.biruLaut));
            tmbOk.getDrawable().setAlpha(255);
        } else{
            samaDenganAwal= true;
            tmbOk.getDrawable().setTint(Color.parseColor("#777777"));
            tmbOk.getDrawable().setAlpha(100);
        }
    }

    public void aturJumlahHalaman(int jml){
        batasHalaman= jml;
        posisiLoader= new int[batasHalaman];
        perbaruiPosisiLoader(POSISI_DI_SINI);
    }

    public void pilihHalaman(int posisi){
        pilihHalaman(posisi, true);
    }
    private void pilihHalaman(int posisi, boolean awal){
        if(awal)
            perbaruiPosisiLoader(POSISI_DI_SINI, (posisi /batasHalaman));

        posisiFoto = posisi %batasHalaman;

        AdapterHalaman adpHal= new AdapterHalaman();
        wadahFoto.setAdapter(adpHal);

        if(!adapterDiAwal)
            posisiFoto++;
        wadahFoto.setCurrentItem(posisiFoto);
//        Toast.makeText(this, "posisi= " + posisiFoto, Toast.LENGTH_SHORT).show();
    }
    private boolean perbaruiPosisiLoader(int arah){
        return perbaruiPosisiLoader(arah, kelompokHalamanSkrg);
    }
    //(#param) kelompokKe -> dimulai dari 0
    private boolean perbaruiPosisiLoader(int arah, int kelompokKe){
        if(bisaDiPerluas && ((arah == POSISI_SEBELUM && adapterDiAwal) || (arah == POSISI_SESUDAH && adapterDiAkhir)))
            return false;
        int posisiBaru= 0;
        if(arah == POSISI_SESUDAH)
            ++kelompokKe;
        else if(arah == POSISI_SEBELUM)
            --kelompokKe;
        posisiBaru= kelompokKe *batasHalaman;

//            posisiBaru= posisiLoader[0] -panjang;
//        else if(arah == POSISI_DI_SINI)

/*
        if(posisiBaru < 0 || posisiBaru >= pathFoto.length)
            return false;
*/
        adapterDiAwal= false;
        adapterDiAkhir= false;
        if(posisiBaru == 0)
            adapterDiAwal= true;
        if(pathFoto.length /batasHalaman == posisiBaru /batasHalaman)
            adapterDiAkhir= true;

        bisaDiPerluas= !(adapterDiAwal && adapterDiAkhir);

        for(int i= 0; i< batasHalaman; i++)
            posisiLoader[i]= posisiBaru++;
        kelompokHalamanSkrg= kelompokKe;
        return true;
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

        Intent intentSebelumnya= getIntent();
        int posisiDipilih[]= intentSebelumnya.getIntArrayExtra("posisiDipilih");
        int urutanDipilih[]= intentSebelumnya.getIntArrayExtra("urutanDipilih");

        if(posisiDipilih != null && posisiDipilih.length > 0) {
            posisiTrahir= posisiDipilih[0];
            loader.aturIndDipilih(posisiDipilih, urutanDipilih);

            for(int i= 0; i< urutanDipilih.length; i++)
                for(int u= i+1; u< urutanDipilih.length; u++)
                    if(urutanDipilih[i] > urutanDipilih[u]){
                        int urutanSmtr= urutanDipilih[i];
                        urutanDipilih[i]= urutanDipilih[u];
                        urutanDipilih[u]= urutanSmtr;

                        int posisiSmtr= posisiDipilih[i];
                        posisiDipilih[i]= posisiDipilih[u];
                        posisiDipilih[u]= posisiSmtr;
                    }
            posisiAwal.dariArrayPrimitif(posisiDipilih);
//            Toast.makeText(this, "ATUR!!!", Toast.LENGTH_SHORT).show();
        }

        loader.aturAksiPilihFoto(new GaleriLoader.AksiPilihFoto() {
            @Override
            public void pilihFoto(View v, final int posisi) {
                if(jenisPengambilan == JENIS_AMBIL_SATU){
                    if(posisiTrahir != -1)
                        loader.batalPilihFoto(posisiTrahir);
                    if(posisiAwal.ukuran() == 0
                            || (posisiAwal.ukuran() > 0 && posisiAwal.ambil(0) != posisi)){
                        tmbOk(true);
                    } else if(posisiAwal.ukuran() > 0 && posisi == posisiAwal.ambil(0))
                        tmbOk(false);
                    posisiTrahir= posisi;
                } else if(jenisPengambilan == JENIS_AMBIL_BANYAK){
                    if(samaDenganAwal){
                        tmbOk(true);
                    }
                    else{
                        if((posisiAwal.indekAwal(posisi) +1) == loader.ambilUrutanDipilih(posisi))
                            perbedaanDipilih--;
                        if(perbedaanDipilih == 0 && posisiAwal.ukuran() == loader.ambilUrutanDipilih()[0].length) {
                            tmbOk(false);
                        }
                    }
                }
            }
            @Override
            public void batalPilihFoto(View v, int posisi) {
                if(jenisPengambilan == JENIS_AMBIL_SATU){
                    if(posisi == wadahFoto.getCurrentItem()){
                        if(posisiAwal.ukuran() > 0 && posisiAwal.ambil(0) == posisi){
                            posisiTrahir = -1;
                            tmbOk(true);
                        }
                    }
                } else if(jenisPengambilan == JENIS_AMBIL_BANYAK){
                    if(posisiAwal.indekAwal(posisi) != -1) {
                        perbedaanDipilih++;
                        tmbOk(true);
                    } else if(posisiAwal.ukuran() == loader.ambilUrutanDipilih()[0].length){
                        tmbOk(false);

//                        Toast.makeText(GaleriPreview.this, "BATASL!!! " +posisiAwal.ukuran(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void pasangIndikatorDipilih(boolean dipilih){
        if(dipilih){
            if(jenisPengambilan == JENIS_AMBIL_BANYAK){
                noUrutDipilih.setText(Integer.toString(loader.ambilUrutanDipilih(posisiFoto)));
//                Toast.makeText(this, "BANYAK!!!", Toast.LENGTH_SHORT).show();
            }
            else if(jenisPengambilan == JENIS_AMBIL_SATU)
                centangDipilih.setImageResource(R.drawable.obj_centang);
            noUrutDipilih.getBackground().setTint(getResources().getColor(R.color.biruLaut));
            noUrutDipilih.getBackground().setAlpha(255);
        } else{
            if(jenisPengambilan == JENIS_AMBIL_BANYAK)
                noUrutDipilih.setText("");
            else if(jenisPengambilan == JENIS_AMBIL_SATU)
                centangDipilih.setImageDrawable(null);
            noUrutDipilih.getBackground().setAlpha(0);
        }
        noUrutDipilih.setSelected(dipilih);
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


    private String[] ambilPathGambar(){
        ArrayList<String> arrayList= getImagesPath();
        String array[]= new String[arrayList.size()];
        int indJalan= 0;
        for(int i= array.length-1; i >= 0; i--)
            array[indJalan++]= arrayList.get(i);
        return array;
    } private ArrayList<String> getImagesPath() {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ThumbnailUtils aka= new ThumbnailUtils();
        Bitmap bm= ThumbnailUtils.createVideoThumbnail("asas", MediaStore.Video.Thumbnails.MICRO_KIND);

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }

    public ArrayList<String> ambilDaftarJudul(){
        ArrayList<String> judul= new ArrayList<String>();
//        String pathDipilih[]= ambilPathDipilih();
        for(int i= 0; i<pathFoto.length; i++){
            String array[]= pathFoto[i].split("/");
            judul.add(array[array.length-1]);
        }
        return judul;
    }


    private class AdapterHalaman extends PagerAdapter{
        int jmlHalaman;

        @Override
        public int getCount() {
/*
            if(bisaDiPerluas){
                if(adapterDiAkhir)
                    jmlHalaman= pathFoto.length %batasHalaman +1;
                else if(adapterDiAwal)
                    jmlHalaman= batasHalaman +1;
                else
                    jmlHalaman= batasHalaman +2;
            } else
                jmlHalaman= pathFoto.length;
            return jmlHalaman;
*/
            return pathFoto.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
/*
            if(!awalInit && bisaDiPerluas){
                if(position == jmlHalaman -1 && !adapterDiAkhir){
//                    Toast.makeText(GaleriPreview.this, "awal= " +adapterDiAwal +" akhir= " +adapterDiAkhir +" kelHal= " +kelompokHalamanSkrg
//                            +"\njmlHalaman= " +jmlHalaman +" diperluas= " +bisaDiPerluas, Toast.LENGTH_LONG).show();

                    perbaruiPosisiLoader(POSISI_SESUDAH);
                    pilihHalaman(0, false);
                    return null;
                } else if(position == 0 && !adapterDiAwal){
//                    Toast.makeText(GaleriPreview.this, "AWAS!!!!", Toast.LENGTH_LONG).show();

                    perbaruiPosisiLoader(POSISI_SEBELUM);
                    pilihHalaman(jmlHalaman -2, false);
                    return null;
                }
                if(!adapterDiAwal)
                    position--;
            }
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

            panel= (ImageView) loader.buatFoto(panel, position/*posisiLoader[position]*/);

            ScaleGesture gestur= new ScaleGesture(panel, getBaseContext());
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

            panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!judulDitampilkan){
                        pathHeader.moveTo(header.getX(), header.getY());
                        pathHeader.lineTo(header.getX(), header.getY()-(header.getHeight()));
                        animatorHeader= ObjectAnimator.ofFloat(header, View.X, View.Y, pathHeader);
                        animatorHeader.setDuration(500);

                        pathFooter.moveTo(footer.getX(), footer.getY());
                        pathFooter.lineTo(footer.getX(), footer.getHeight());
                        animatorFooter= ObjectAnimator.ofFloat(footer, View.X, View.Y, pathFooter);
                        animatorFooter.setDuration(500);

                        animatorHeader.start();
                        animatorFooter.start();
                        judulDitampilkan= true;
                    } else{
                        if(animatorHeader.isRunning())
                            animatorHeader.cancel();
                        if(animatorFooter.isRunning())
                            animatorFooter.cancel();

                        header.setY(header.getY()+header.getHeight());
                        footer.setY(footer.getY()-footer.getHeight());
                        judulDitampilkan= false;
                    }
                }
            });
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
