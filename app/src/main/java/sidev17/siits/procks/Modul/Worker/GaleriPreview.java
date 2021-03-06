package sidev17.siits.procks.Modul.Worker;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import sidev17.siits.procks.Interface.BitmapServerListener;
import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.Array;
import sidev17.siits.procks.Utils.Utilities;
import sidev17.siits.procks.Utils.ViewTool.GaleriLoader;
import sidev17.siits.procks.Utils.ScaleGesture;

public class GaleriPreview extends AppCompatActivity {

    private final int POSISI_DI_SINI= 10;
    private final int POSISI_SEBELUM= 11;
    private final int POSISI_SESUDAH= 12;

    public static final int PATH_DEFAULT= 21;
    public static final int PATH_TERTENTU= 22;

    public static final int AMBIL_BANYAK = 31;
    public static final int AMBIL_SATU = 30;

//    public static final int SUMBER_FOTO_PENYIMPANAN= 40;
//    public static final int SUMBER_FOTO_SERVER= 41;

    public static final String TANDA_DARI_SERVER= "\"server\":";
    public static final int PANJANG_TANDA_DARI_SERVER= TANDA_DARI_SERVER.length();


    private String pathFoto[];
    private ArrayList<String> judul;
//    private Bitmap bitmapFoto[];
//    private int jenisFoto[];

    private int batasHalaman= 10;
    private int posisiLoader[];
    private boolean adapterDiAwal= true;
    private boolean adapterDiAkhir= true;
    private boolean bisaDiPerluas= false; //menandakan bahwa halaman cuma 1 / pathFoto tidak lebih panjang dari batasHalaman
    private boolean indikatorDitampilkan= true;
    private int kelompokHalamanSkrg= 0;
    private int posisiFotoSebelumnya= -1;
    private int posisiFoto= -1;
    private int jenisPath;

    private int jenisPengambilan= AMBIL_BANYAK;
    private int posisiTrahir= -1;

    private Array<Integer> posisiAwal= new Array<>();
    private boolean samaDenganAwal= true;
    private int perbedaanDipilih= 0;

//    private ArrayList<Bitmap> foto;
    private ViewPager wadahFoto;
//    private TextView judulHalaman;
    private TextView judulFoto;
    private ImageView tmbOk;

    private Path pathHeader= new Path();
    private Path pathFooter= new Path();
    private ObjectAnimator animatorHeader;
    private ObjectAnimator animatorFooter;
    private View header;
    private View footer;
    private boolean judulDitampilkan= true;
    private boolean videoControlDipasang= false;
    private int indekViewControl= -1;
//    private View videoControl;

    private int batasBuffer;
    private Object bufferView[]; //tipe Object karena bisa berupa RelativeLayout atau VideoPreview
    private int indekKeliatan[]; //= new int[batasBuffer];

    private TextView noUrutDipilih;
    private ImageView centangDipilih;
    private View indikatorDipilih;

    private GaleriLoader loader;
    private boolean zoomIn= false;
//    private int jenisFoto;

    private DisplayMetrics metrikUtama;
    private int panjangScreen;
    private int lebarScreen;


    private ScaleGestureDetector detectorSkala;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto);

//        judulHalaman = findViewById(R.id.preview_judul);
        judulFoto= findViewById(R.id.preview_foto_judul);
        noUrutDipilih = findViewById(R.id.tambah_cell_centang_no);
        centangDipilih= findViewById(R.id.tambah_cell_centang_gambar);
        indikatorDipilih= findViewById(R.id.cell_centang);
        wadahFoto= findViewById(R.id.preview_foto_wadah);

        initTmbOk();
        initHeaderFooter();
        pengaturanAwal();
        initLoader(pathFoto);

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

        AdapterHalaman adpHal= new AdapterHalaman();
        wadahFoto.setAdapter(adpHal);

        wadahFoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return zoomIn;
            }
        });
        wadahFoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(videoControlDipasang && posisiFotoSebelumnya != -1)
                    ((VideoPreview)bufferView[posisiFotoSebelumnya %batasBuffer]).hentikan();
            }

            @Override
            public void onPageSelected(int position) {
                judulFoto.setText(judul.get(position));

                pasangIndikatorDipilih(loader.fotoDipilih(posisiFoto));

                posisiFoto = position;

                if(bufferView[posisiFoto %batasBuffer] != null)
                    pasangVideoControl_Induk();
//                Toast.makeText(GaleriPreview.this, "posisi= " + posisiFoto, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        wadahFoto.setCurrentItem(posisiFoto);
//        pilihHalaman(posisiFoto);
    }

    private void pasangVideoControl_Induk(){
        if(!(GaleriLoader.Galeri.jenisFoto(pathFoto[posisiFoto]) == -1)){
            if(!(GaleriLoader.Galeri.jenisFoto(pathFoto[posisiFoto]) == GaleriLoader.JENIS_FOTO)){
                if(videoControlDipasang && posisiFotoSebelumnya != -1)
                    ((VideoPreview)bufferView[posisiFotoSebelumnya %batasBuffer]).lepasVideoControl();
                ((VideoPreview)bufferView[posisiFoto %batasBuffer]).pasangVideoControl();
                posisiFotoSebelumnya= posisiFoto;
            } else if(videoControlDipasang)
                ((VideoPreview)bufferView[posisiFotoSebelumnya %batasBuffer]).lepasVideoControl();
        }
    }

    @Override
    public void onBackPressed() {
        if(videoControlDipasang)
            ((VideoPreview)bufferView[wadahFoto.getCurrentItem() %batasBuffer]).hentikan();
        else{
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    private void initBuffer(int batasBuffer){
        bufferView= new Object[batasBuffer];
        indekKeliatan= new int[batasBuffer];
        this.batasBuffer= batasBuffer;
        for(int i= 0; i< batasBuffer; i++)
            indekKeliatan[i]= -1;
    }

    private void ambilIntent(){
        Intent intentSebelumnya= getIntent();

//        ParcelHolder<Bitmap> holder= intentSebelumnya.getParcelableExtra("foto");
//        foto= intentSebelumnya.getParcelableArrayListExtra("foto");

        pathFoto= intentSebelumnya.getStringArrayExtra("path");
//        bitmapFoto= intentSebelumnya.getStringArrayExtra("bitmap");

        jenisPath= (pathFoto== null) ? PATH_DEFAULT
                    : intentSebelumnya.getIntExtra("jenisPath", PATH_TERTENTU);

        jenisPengambilan= intentSebelumnya.getIntExtra("jenisPengambilan", AMBIL_BANYAK);
//        Toast.makeText(this, "jenisPengambilan= " +jenisPengambilan, Toast.LENGTH_SHORT).show();

//        jenisFoto= intentSebelumnya.getIntExtra("jenisFoto", GaleriLoader.JENIS_FOTO);

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
        initBuffer(4);
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
//        videoControl= findViewById(R.id.video_control);
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

    void initLoader(String pathFile[]){
//        inisiasiArrayDipilih(pathFoto.length);
        loader= new GaleriLoader(this, pathFile, 18, GaleriLoader.JENIS_FOTO,
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
                if(jenisPengambilan == AMBIL_SATU){
                    if(posisiTrahir != -1)
                        loader.batalPilihFoto(posisiTrahir);
                    if(posisiAwal.ukuran() == 0
                            || (posisiAwal.ukuran() > 0 && posisiAwal.ambil(0) != posisi)){
                        tmbOk(true);
                    } else if(posisiAwal.ukuran() > 0 && posisi == posisiAwal.ambil(0))
                        tmbOk(false);
                    posisiTrahir= posisi;
                } else if(jenisPengambilan == AMBIL_BANYAK){
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
                if(jenisPengambilan == AMBIL_SATU){
                    if(posisi == wadahFoto.getCurrentItem()){
                        if(posisiAwal.ukuran() > 0 && posisiAwal.ambil(0) == posisi){
                            posisiTrahir = -1;
                            tmbOk(true);
                        }
                    }
                } else if(jenisPengambilan == AMBIL_BANYAK){
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
            if(jenisPengambilan == AMBIL_BANYAK){
                noUrutDipilih.setText(Integer.toString(loader.ambilUrutanDipilih(posisiFoto)));
//                Toast.makeText(this, "BANYAK!!!", Toast.LENGTH_SHORT).show();
            }
            else if(jenisPengambilan == AMBIL_SATU)
                centangDipilih.setImageResource(R.drawable.obj_centang);
            noUrutDipilih.getBackground().setTint(getResources().getColor(R.color.biruLaut));
            noUrutDipilih.getBackground().setAlpha(255);
        } else{
            if(jenisPengambilan == AMBIL_BANYAK)
                noUrutDipilih.setText("");
            else if(jenisPengambilan == AMBIL_SATU)
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

    private ArrayList<String> ambilDaftarJudul(){
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

            RelativeLayout indukPanel= new RelativeLayout(GaleriPreview.this);
            RelativeLayout.LayoutParams lpIndukPanel= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lpIndukPanel.addRule(RelativeLayout.CENTER_IN_PARENT);

            int indekGrup= position /indekKeliatan.length;
            int indekBuffer= position %batasBuffer;

            if(indekKeliatan[indekBuffer] != indekGrup){
                indekKeliatan[indekBuffer] = indekGrup;

                indukPanel.setLayoutParams(lpIndukPanel);
                indukPanel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
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
                        } else if(animatorFooter != null){
                            if(animatorHeader.isRunning())
                                animatorHeader.cancel();
                            if(animatorFooter.isRunning())
                                animatorFooter.cancel();

                            header.setY(header.getY()+header.getHeight());
                            footer.setY(footer.getY()-footer.getHeight());
                            judulDitampilkan= false;
                        }
                        return true;
                    }
                });

                View panel;
                if(!pathFoto[position].startsWith(TANDA_DARI_SERVER)){
                    if(GaleriLoader.Galeri.jenisFoto(pathFoto[position]) == GaleriLoader.JENIS_FOTO){
//                    videoControl.setVisibility(View.GONE);
                        ImageView imgPanel= new ImageView(getBaseContext());
                        imgPanel.setScaleType(ImageView.ScaleType.FIT_CENTER);

                        imgPanel= (ImageView) loader.buatFoto(imgPanel, position/*posisiLoader[position]*/);

                        ScaleGesture gestur= new ScaleGesture(imgPanel, getBaseContext());
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
//                    imgPanel.setOnTouchListener(gestur);
/*
                    imgPanel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
*/
                        panel= imgPanel;
                        indukPanel.addView(panel);
                        bufferView[indekBuffer]= indukPanel;
                    } else{
//                    videoControl.setVisibility(View.VISIBLE);
                        VideoPreview vidPrev= new VideoPreview(pathFoto[position]);
                        panel= vidPrev.pasangVideoViewKe(indukPanel);
                        bufferView[indekBuffer]= vidPrev;
//                        Toast.makeText(GaleriPreview.this, "VIDEO!= " +position, Toast.LENGTH_SHORT).show();
                    }
                } else{
//===========CARI!!!=============
                    // jika dari server
                     //   VideoPreview
                    if(urlVideo(pathFoto[position])){
                        Log.d("Video", "true "+pathFoto[position]);
                        VideoPreview vidPrev= new VideoPreview(pathFoto[position]);
                        panel= vidPrev.pasangVideoViewKe(indukPanel);
                        bufferView[indekBuffer]= vidPrev;
                    } else {
                        Log.d("Video", "false "+pathFoto[position]);
                        final ImageView imgPanel= new ImageView(getBaseContext());
                        imgPanel.setScaleType(ImageView.ScaleType.FIT_CENTER);

                        //imgPanel= (ImageView) loader.buatFoto(imgPanel, position/*posisiLoader[position]*/);
                        Utilities.loadBitmap(pathFoto[position].substring(PANJANG_TANDA_DARI_SERVER), new BitmapServerListener() {
                            @Override
                            public void bitmapLoaded(Bitmap bm) {
                                imgPanel.setImageBitmap(bm);
                            }
                        });
                        ScaleGesture gestur= new ScaleGesture(imgPanel, getBaseContext());
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
                        panel = imgPanel;
                        indukPanel.addView(panel);
                        bufferView[indekBuffer]= indukPanel;
                    }
                }
                RelativeLayout.LayoutParams lpPanel= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lpPanel.addRule(RelativeLayout.CENTER_IN_PARENT);
                panel.setLayoutParams(lpPanel);

            } else{
                if(bufferView[indekBuffer] instanceof VideoPreview)
                    indukPanel= ((VideoPreview)bufferView[indekBuffer]).viewWraper();
                else
                    indukPanel= (RelativeLayout) bufferView[indekBuffer];
            }
            container.addView(indukPanel, container.getChildCount()-2);
            return indukPanel;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    boolean urlVideo(String url){
        for(int i = url.length(); i>=4; i --){
            for(int j = 0 ; j < GaleriLoader.Galeri.ekstensiVideo.ukuran(); j ++){
                if(url.substring(i-4, i).equalsIgnoreCase("."+GaleriLoader.Galeri.ekstensiVideo.ambil(j)))
                    return true;
            }
        }
        return false;
    }

    class VideoPreview{
        public final int JALAN= 10;
        public final int JEDA= 11;
        public final int BERHENTI= 12;

        private int status= BERHENTI;

        private RelativeLayout indukPanel; //sebagai wrapper untuk AdapterHalaman
//        private int indekViewControl= -1; //pada wraper
        private View viewInduk; //yg utama
        private View viewControl; //bar yg ngontrol video (play n pause)

        private VideoView vVideo;
        private ImageView vKontrol;
        private ImageView vPlay;

//        private RelativeLayout vBar;
        private ImageView vBarLatar;
        private ImageView vBarProgres;
        private ImageView vPenunjuk;

        private TextView vDurasi;
//        private TextView vDurasiBesar;

        private int durasiVid;
        private int pjgBarFull;
        private AsyncTask<Void, Integer, Void> videoProgres;

//        private ListView listPathVideo;
        private String pathVideo;

//        private int indekDipilih= 1;

        private ScaleGesture sg;



        public VideoPreview(String pathVideo) {
            this.pathVideo= pathVideo;
            viewInduk= getLayoutInflater().inflate(R.layout.model_video_view, null);
            viewControl= getLayoutInflater().inflate(R.layout.model_video_view_control, null);

//            vBar= viewControl.findViewById(R.id.video_control_bar);
            vBarProgres= viewControl.findViewById(R.id.video_control_bar_progres);
            vBarLatar= viewControl.findViewById(R.id.video_control_bar_latar);

            vPenunjuk= viewControl.findViewById(R.id.video_control_bar_penunjuk);
            sg= new ScaleGesture(vPenunjuk, GaleriPreview.this);
            sg.aturAksiGeser(new ScaleGesture.AksiGeser() {

                float xAwal, yAwal, pjgBarAwal;
                @Override
                public void awalGeser(View v, float xAwal, float yAwal) {
                    this.xAwal= xAwal;
                    this.yAwal= yAwal;
                    pjgBarAwal= vBarProgres.getLayoutParams().width;
                }

                @Override
                public void geser(View v, float xGeser, float yGeser) {
                    if(videoProgres != null && videoProgres.getStatus() == AsyncTask.Status.RUNNING){
//                    videoProgres.cancel(true);
//                    Toast.makeText(VideoPrevAct.this, "DIGESER!!!", Toast.LENGTH_SHORT).show();
                    }

                    ViewGroup.LayoutParams lp =vBarProgres.getLayoutParams();
                    lp.width= (int) (pjgBarAwal +xGeser); //((int) v.getX() +(v.getWidth()/2));
                    vBarProgres.setLayoutParams(lp);
//                    Toast.makeText(VideoPrevAct.this, "pjg= " +vBarProgres.getLayoutParams().width, Toast.LENGTH_SHORT).show();
//                mainkan((double) vBarProgres.getWidth() /pjgBarFull);
                }

                @Override
                public void akhirGeser(View v, float xAkhir, float yAkhir) {
                    mainkan((double) vBarProgres.getWidth() /pjgBarFull);
                }
            });
            sg.bisaZoom(false);
            sg.aturModeGeser(ScaleGesture.MODE_GESER_HORIZONTAL);

            vDurasi= viewControl.findViewById(R.id.video_control_durasi);
//            vDurasiBesar= viewInduk.findViewById(R.id.durasi);

//            pathVideo= ambilPathVideo();
//            listPathVideo= viewInduk.findViewById(R.id.video_path);
//            listPathVideo.setAdapter(new AdapterPath());

            vVideo= viewInduk.findViewById(R.id.video_preview);
//        vVideo.setVideoPath(pathDipilih);
            vVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    durasiVid= mp.getDuration();
                    pjgBarFull= vBarLatar.getWidth();
                    vBarProgres.getLayoutParams().width= 0;
                    vPenunjuk.setX(vBarProgres.getX());
                    vVideo.start();
                    videoProgres.execute();
                  //  Toast.makeText(GaleriPreview.this, "panjang: " +(durasiVid), Toast.LENGTH_LONG).show();
                }
            });

            vPlay= viewInduk.findViewById(R.id.video_play);
            vPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!vKontrol.isSelected()){
                        if(status == BERHENTI)
                            mainkan((double) vVideo.getCurrentPosition() /durasiVid);
                        else if(status == JEDA)
                            lanjutkan();
                    } else{
                        jeda();
                    }
                }
            });

            vKontrol= viewControl.findViewById(R.id.video_control_play);
            vKontrol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!v.isSelected()){
                        if(status == BERHENTI)
                            mainkan((double) vVideo.getCurrentPosition() /durasiVid);
                        else if(status == JEDA)
                            lanjutkan();
                    } else{
                        jeda();
                    }
                }
            });
//            pasangVideoControl_Induk(this);
        }

        public void pasangVideoControl(){
            indekViewControl= ((ViewGroup)footer).getChildCount();
            ((ViewGroup)footer).addView(viewControl);
            videoControlDipasang= true;
        }
        public void lepasVideoControl(){
            ((ViewGroup)footer).removeViewAt(indekViewControl);
            videoControlDipasang= false;
        }

        AsyncTask<Void, Integer, Void> initVideoProgres(){
            return new AsyncTask<Void, Integer, Void>() {
                @SuppressLint("WrongThread")
                @Override
                protected Void doInBackground(Void... voids) {
                    float persenProgres, pjgProgres;
                    do{
                        persenProgres= (float) vVideo.getCurrentPosition() /durasiVid *100;
                        pjgProgres= (float) pjgBarFull *persenProgres /100;
                        if(pjgProgres > pjgBarFull || status == JEDA || status == BERHENTI) {
//                        Toast.makeText(VideoPrevAct.this, "KELUAR!!!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(!sg.sedangDigeser())
                            publishProgress((int)pjgProgres, vVideo.getCurrentPosition());
//                    Toast.makeText(VideoPrevAct.this, "MASUK!!! ", Toast.LENGTH_SHORT).show();
                    }while(pjgProgres <= pjgBarFull);
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    int pjg= values[0];
                    int milidetik= values[1];
                    vBarProgres.getLayoutParams().width= pjg;
                    int posisiPenunjuk= vBarProgres.getRight();
                    vPenunjuk.setX(posisiPenunjuk);
                    tampilkanDurasi(vDurasi, milidetik);
//                Toast.makeText(VideoPrevAct.this, "MASUK!!! " +milidetik, Toast.LENGTH_SHORT).show();
                }
            };
        }
/*
        public String[] ambilPathVideo(){
            ArrayList<String> arrayList= getVideoPath();
            String array[]= new String[arrayList.size()];
            int indJalan= 0;
            for(int i= array.length-1; i >= 0; i--)
                array[indJalan++]= arrayList.get(i);
            return array;
        } public ArrayList<String> getVideoPath() {
            Uri uri;
            ArrayList<String> listOfAllVideo = new ArrayList<String>();
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            String PathOfVideo = null;
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            Bitmap bm= ThumbnailUtils.createVideoThumbnail("asas", MediaStore.Video.Thumbnails.MICRO_KIND);

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME };

            cursor = getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                PathOfVideo = cursor.getString(column_index_data);

                listOfAllVideo.add(PathOfVideo);
            }
            return listOfAllVideo;
        }
/*
        class AdapterPath extends BaseAdapter {
            @Override
            public int getCount() {
                return pathVideo.length;
            }

            @Override
            public Object getItem(int position) {
                return pathVideo[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewGroup.LayoutParams lp= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                String arrayJudulVideo[]= pathVideo[position].split("/");
                String judulVideo= arrayJudulVideo[arrayJudulVideo.length-1];

                final TextView vPath= new TextView(GaleriPreview.this);
                vPath.setText(judulVideo);
                vPath.setTextSize(15);
                vPath.setTextColor(Color.parseColor("#000000"));

                vPath.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!v.isSelected()){
                            v.setBackgroundColor(getResources().getColor(R.color.ijoUtama));
                            v.setSelected(true);
                            vPath.setTextColor(Color.parseColor("#FFFFFF"));
                            if(indekDipilih != position){
//                            jeda();
                                vVideo.setVideoPath(pathVideo[indekDipilih = position]);
                                mainkan(0);
                            }
                        } else{
                            v.setBackground(null);
                            v.setSelected(false);
                            vPath.setTextColor(Color.parseColor("#000000"));
                        }
                    }
                });
                return vPath;
            }
        }
*/
        //0 <= persen <= 1
        public void mainkan(double persen){
//            if(status == JALAN) {
//                Toast.makeText(this, "Batal sebelum", Toast.LENGTH_SHORT).show();
//            jeda();
//            }
            vKontrol.setSelected(true);
            vKontrol.setImageResource(R.drawable.icon_pause);
            videoProgres= initVideoProgres();

            MediaController mediaController = new MediaController(GaleriPreview.this);
            mediaController.setAnchorView(vVideo);

            Uri uri= Uri.parse(pathVideo);
            vVideo.setMediaController(mediaController);
            vVideo.setVideoURI(uri);
            vVideo.seekTo((int)(durasiVid *persen));
            vPlay.setVisibility(View.GONE);
            status= JALAN;
        }
        public void lanjutkan(){
            vKontrol.setSelected(true);
            vKontrol.setImageResource(R.drawable.icon_pause);
            vVideo.start();
//        vVideo.seekTo(vVideo.getCurrentPosition());
            videoProgres= initVideoProgres();
            vPlay.setVisibility(View.GONE);
            status= JALAN;
        }
        public void jeda(){
            vKontrol.setSelected(false);
            vKontrol.setImageResource(R.drawable.icon_play);
            vVideo.pause();
            if(videoProgres != null) {
                videoProgres.cancel(false);
//            Toast.makeText(this, "batal= " +batal, Toast.LENGTH_SHORT).show();
            }
//        vPlay.setVisibility(View.VISIBLE);
            status= JEDA;
        }
        public void hentikan(){
            if(status == JALAN)
                jeda();
            vVideo.stopPlayback();
            status= BERHENTI;
        }

        public int status(){
            return status;
        }
/*
        @Override
        public void onBackPressed() {
            if(status != BERHENTI)
                hentikan();
            super.onBackPressed();
        }
*/
        private void tampilkanDurasi(TextView tv, int milidetik){
            int detik= milidetik /1000;
            int menit= detik /60;
            detik%= 60;

            tv.setText(formatDwi(menit)+" : " +formatDwi(detik));
        }
        private String formatDwi(int angka){
            String hasil= Integer.toString(angka);
            if(hasil.length() == 1 )
                hasil= "0" +hasil;
            return hasil;
        }

        public View viewInduk(){
            return viewInduk;
        }

        public View pasangVideoViewKe(RelativeLayout induk){
            indukPanel= induk;
            induk.addView(viewInduk);
            return viewInduk;
        }
        public RelativeLayout viewWraper(){
            return indukPanel;
        }
    }
}
