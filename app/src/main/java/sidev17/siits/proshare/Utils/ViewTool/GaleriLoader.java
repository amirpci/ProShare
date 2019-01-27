package sidev17.siits.proshare.Utils.ViewTool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ScaleGesture;

public class GaleriLoader {
    public static final int JENIS_FOTO= 10;
    public static final int JENIS_VIDEO_THUMBNAIL= 11;
    public static final int JENIS_VIDEO= 12;
    public static final int JENIS_CAMPURAN= 13;

    public static final int BENTUK_KOTAK= 21;
    public static final int BENTUK_ASLI= 20;

    public static final int UKURAN_MENYESUAIKAN_PJG_INDUK= -1;
    public static final int UKURAN_MENYESUAIKAN_LBR_INDUK= -3;

    public static final int ELEMEN_KOSONG= -10;
    public static final int ELEMEN_TAK_DIPILIH= -11;
    public static final int ELEMEN_DIPILIH= -12;

    private int bentukFoto= BENTUK_ASLI;


    private int jenisFoto;
    private boolean bisaDiScale= false;

    private Context konteks;
//    private Activity activity;

    private AksiBuffer aksiBuffer;
    private AksiPilihFoto aksiPilihFoto;
    private AksiProsesBitmap aksiProsesBitmap;

    private String pathFoto[];
    private Bitmap bufferBitmap[];
    private View bufferView[];
    private int batasBuffer= 0;
    private int batasMaksDipilih= 10;
    private int indKelihatan[];
//    private long batasMemori[];

    private int ukuranThumbnail;
    private int ukuranPratinjau;
    private int jmlItemPerGaris; //jml kolom atau baris tergantung konstanta ukuran di atas
    private int offsetItem; //margin+padding

    private int sumberImgWadah= -1;
    private ViewGroup.LayoutParams lpImgWadah;

    private boolean modeBg= true;
    private int sumberBg= -1;
    private String warnaTintBg= "#A3A3A3";
    private String warnaBg= "#ABABAB";
    private int sumberBgTakBisa= -1;
    private String warnaTintBgTakBisa= "#A3A3A3";

    private Array<Integer> sumberAksesoris;
    private Array<RelativeLayout.LayoutParams> lpAksesoris;
    private Array<Integer> idAksesoris;

    private int idElemenImg= -1;
    private boolean elemenImg= true;

    private AsyncTask<Integer, Integer, Bitmap> loaderThumbnail[];
    private AsyncTask<Integer, Integer, Bitmap> loader[];
    private AsyncTask<Integer, Integer, Bitmap> loaderDipilih[];

    private int dipilih[];
    private Array<View> viewDipilih = new Array<View>();
    private Array<BitmapHandler> bitmapDipilih= new Array<BitmapHandler>();
//    private Array<Integer> urutanDipilih= new Array<Integer>(true);
    private int cursorDipilih= 0;
    private int jmlDipilihTertunda= 0;
    private int jmlUdahDiload= 0;

    private boolean dipilihLengkap= true;



    public GaleriLoader(Context k, String pathFoto[], int jmlBuffer, int jenisFoto,
                        @LayoutRes int wadahTiapKotak, @IdRes int idElemenImg){
        konteks= k;
        this.jenisFoto= jenisFoto;
        this.pathFoto= pathFoto;
//        batasBuffer= jmlBuffer;
        aturElemenImg(wadahTiapKotak, idElemenImg);
        perbaruiBatasBuffer(jmlBuffer);
        ukuranPratinjau = 400;
        ukuranThumbnail = 100;
        sumberAksesoris= new Array<Integer>();
        lpAksesoris= new Array<RelativeLayout.LayoutParams>();
        idAksesoris= new Array<Integer>();
    } public GaleriLoader(Context k, String pathFoto[], int jmlBuffer, int ukuranPratinjau, int jenisFoto,
                          @LayoutRes int wadahTiapKotak, @IdRes int idElemenImg){
        konteks= k;
        this.jenisFoto= jenisFoto;
        this.pathFoto= pathFoto;
//        batasBuffer= jmlBuffer;
        aturElemenImg(wadahTiapKotak, idElemenImg);
        perbaruiBatasBuffer(jmlBuffer);
        this.ukuranPratinjau = ukuranPratinjau;
        ukuranThumbnail = 100;
        sumberAksesoris= new Array<Integer>();
        lpAksesoris= new Array<RelativeLayout.LayoutParams>();
        idAksesoris= new Array<Integer>();
    } public GaleriLoader(Context k, String pathFoto[], int jmlBuffer, int ukuranPratinjau, int jenisFoto, int bentukFoto,
                          @LayoutRes int wadahTiapKotak, @IdRes int idElemenImg){
        konteks= k;
        this.jenisFoto= jenisFoto;
        this.bentukFoto= bentukFoto;
        this.pathFoto= pathFoto;
//        batasBuffer= jmlBuffer;
        aturElemenImg(wadahTiapKotak, idElemenImg);
        perbaruiBatasBuffer(jmlBuffer);
        this.ukuranPratinjau = ukuranPratinjau;
        ukuranThumbnail = 100;
        sumberAksesoris= new Array<Integer>();
        lpAksesoris= new Array<RelativeLayout.LayoutParams>();
        idAksesoris= new Array<Integer>();
    }


    public void aturBentukFoto(int bentukFoto){
        this.bentukFoto= bentukFoto;
    }
    public void bisaDiScale(boolean bisa){
        bisaDiScale= bisa;
    }

    public interface AksiBuffer{
        void bufferThumbnail(int posisi, int jmlBuffer);
        void bufferUtama(int posisi, int jmlBuffer);
    }
    public void aturAksiBuffer(AksiBuffer a){
        aksiBuffer= a;
    }

    public interface AksiPilihFoto{
        void pilihFoto(View v, int posisi);
        void batalPilihFoto(View v, int posisi);
    }
    public void aturAksiPilihFoto(AksiPilihFoto a){
        aksiPilihFoto= a;
    }

    public interface AksiProsesBitmap{
        void proses(BitmapHandler handler);
        void selesai(Bitmap bm, boolean tuntas);
    }
    public void aturAksiProsesBitmap(AksiProsesBitmap a){
        aksiProsesBitmap= a;
    }

    public void perbaruiBatasBuffer(int jmlBuffer){
        int batasAwal= batasBuffer;
        batasBuffer= jmlBuffer;
        bufferBitmap = /*(bufferBitmap == null) ?*/ new Bitmap[jmlBuffer]; //: ArrayMod.ubahArray(bufferBitmap, jmlBuffer);
        bufferView = /*(bufferView == null) ?*/ new View[jmlBuffer]; // : ArrayMod.ubahArray(bufferView, jmlBuffer);
        loaderThumbnail = /*(loaderThumbnail == null) ?*/ new AsyncTask[jmlBuffer]; // : ArrayMod.ubahArray(loaderThumbnail, jmlBuffer);
        loader = /*(loader == null) ?*/ new AsyncTask[jmlBuffer]; // : ArrayMod.ubahArray(loader, jmlBuffer);
//        if(loaderDipilih == null)
        loaderDipilih =  new AsyncTask[batasMaksDipilih];
        dipilih= /*(dipilih == null) ?*/ new int[pathFoto.length]; //: ArrayMod.ubahArray(dipilih, pathFoto.length);

        perbaruiBufferView(0, jmlBuffer);
        perbaruiIndKelihatan(0, jmlBuffer);

//        batasBuffer= jmlBuffer;
//        batasMemori= new long[jmlBuffer];
    }
    private void perbaruiIndKelihatan(int mulai, int jmlBuffer){
        indKelihatan= /*(indKelihatan == null) ?*/ new int[jmlBuffer]; // : ArrayMod.ubahArray(indKelihatan, batasBuffer);
//        int mulai= (batasBuffer == -1) ? 0 : batasBuffer;
        for(int i= mulai; i<jmlBuffer; i++)
            indKelihatan[i]= -1;
    }
    private void perbaruiBufferView(int mulai, int jmlBuffer){
        bufferView= new View[jmlBuffer];//ArrayMod.ubahArray(bufferView, jmlBuffer);
//        int posisi= (batasBuffer == -1) ? 0 : batasBuffer;
        isiBufferView(mulai, jmlBuffer -mulai);
    }

//    == 4 Jan 2019
    private void perbaruiDipilih(){
        int jmlDihapus= viewDipilih.ukuran();
        for(int i= 0; i< dipilih.length; i++)
            if(dipilih[i] > 0){
                dipilih[i]= 0;
                jmlDihapus--;
            } else if(jmlDihapus == 0)
                break;
    }

    public void aturJmlBuffer(int jmlBuffer){
        batasBuffer= jmlBuffer;
    }
    public int ambilJmlBuffer(){
        return batasBuffer;
    }
    public void aturJmlBatasDipilih(int jmlDipilih){
        batasMaksDipilih= jmlDipilih;
    }
    public int ambilJmlBatasDipilih(){
        return batasMaksDipilih;
    }

    //mengatur array aksesoris dari awal
    public void aturAksesoris(int sumber[], RelativeLayout.LayoutParams lp[]){
        if(sumber != null) {
            sumberAksesoris = new Array<>();
            lpAksesoris = new Array<>();
            for (int i = 0; i < sumber.length; i++)
                tambahAksesoris(sumber[i], lp[i]);
        }
    }
    public void tambahAksesoris(@DrawableRes int sumber, RelativeLayout.LayoutParams lp){
        idAksesoris.tambah(View.generateViewId());
        sumberAksesoris.tambah(sumber);
        lpAksesoris.tambah(lp);
    }
    public void tambahAksesoris(@DrawableRes int sumber){
        tambahAksesoris(sumber, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
    public void kurangiAksesoris(int sumberYgDihilangkan){
        int indDihilangkan= sumberAksesoris.ukuran()-1;
        for(int i= 0; i<sumberAksesoris.ukuran(); i++)
            if(sumberAksesoris.ambil(i) == sumberYgDihilangkan){
                indDihilangkan= i;
                break;
            }
        sumberAksesoris.hapus(indDihilangkan);
        lpAksesoris.hapus(indDihilangkan);
    }
    public int ambilAksesoris(int ind){
        return sumberAksesoris.ambil(ind);
    }
    public int idAksesoris(int ind){
        return idAksesoris.ambil(ind);
    }

    public void aturModeBg(boolean mode){
        modeBg= mode;
    }
    public boolean modeBgKah(){
        return modeBg;
    }

    public void aturSumberBg(@DrawableRes int id){
        sumberBg= id;
        isiBg();
    }
    public int ambilSumberBg(){
        return sumberBg;
    }
    public void aturWarnaTintBg(String warna){
        if(sumberBg > -1)
            warnaTintBg= warna;
    }
    public String ambilWarnaTintBg(){
        return warnaTintBg;
    }

    public void aturSumberBgTakBisa(@DrawableRes int id){
        sumberBgTakBisa= id;
    }
    public int ambilSumberBgTakBisa(){
        return sumberBgTakBisa;
    }
    public void aturWarnaTintBgTakBisa(String warna){
        warnaTintBgTakBisa= warna;
    }
    public String ambilWarnaTintBgTakBisa(){
        return warnaTintBgTakBisa;
    }

    public void aturWarnaBg(String warna){
        warnaBg= warna;
        isiBg();
    }
    public String ambilWarnaBg(){
        return warnaBg;
    }

    private void isiBg(){
        if(modeBg) {
            for (int i = 0; i < batasBuffer; i++)
                isiBg(i);
        }
    }
    private void isiBg(int ind){
        if(modeBg) {
            if (sumberBg == -1 && warnaBg.startsWith("#"))
                bufferView[ind].setBackgroundColor(Color.parseColor(warnaBg));
            else {
                bufferView[ind].setBackgroundResource(sumberBg);
                bufferView[ind].getBackground().setTint(Color.parseColor(warnaTintBg));
            }
        }
    }

    private void isiBgTakBisa(ImageView img){
        if(sumberBgTakBisa== -1 && warnaBg.startsWith("#"))
            img.setColorFilter(Color.parseColor(warnaBg));
        else {
            img.setImageResource(sumberBgTakBisa);
            img.setColorFilter(Color.parseColor(warnaTintBgTakBisa));
        }
    }
    private void isiBgTakBisa(int ind){
        ImageView img;
        if(elemenImg)
            img= (ImageView) bufferView[ind];
        else
            img= bufferView[ind].findViewById(idElemenImg);
        isiBgTakBisa(img);
    }


    private void tambahScaleGesture(View v){
        v.setOnTouchListener(new ScaleGesture(v, konteks));
    } public void tambahScaleGesture(View v, ScaleGesture gesture){
        v.setOnTouchListener(gesture);
    }

    public static final Array<String> ekstensiFoto= new Array<>("jpg", "jpeg", "png", "bmp", "gif");
    public static final Array<String> ekstensiVideo= new Array<>("mp4", "mkv", "3gp");

    private int jenisFoto(int posisi){
        String array[]= pathFoto[posisi].split("\\.");
        String strTrahir= array[array.length -1];
//        array= strTrahir.split(".");
//        strTrahir= array[array.length-1];

        int indekFoto= ekstensiFoto.indekAwal(strTrahir);
        int indekVideo= ekstensiVideo.indekAwal(strTrahir);

        if(indekFoto > -1)
            return JENIS_FOTO;
        else if(indekVideo > -1)
            return jenisFoto == JENIS_VIDEO ? JENIS_VIDEO : JENIS_VIDEO_THUMBNAIL;
        return -1;
    }

    private void isiFileFoto(int mulai, int sebanyak, int ukuranPokok){
        File file;
        int jenisFoto= this.jenisFoto;
        for(int i= mulai; i< mulai +sebanyak && i<pathFoto.length; i++){
            file= new File(pathFoto[/*pathFoto.length-1-*/i]);
            Bitmap bm= null;
            if(this.jenisFoto == JENIS_CAMPURAN)
                jenisFoto= jenisFoto(i);
            if(jenisFoto == JENIS_FOTO) {
                if(file.length() /1024 < (8*1024) && file.length() < (Runtime.getRuntime().freeMemory() - (1024*1)))
                    bm = BitmapFactory.decodeFile(pathFoto[i]);
            }
            else if(jenisFoto == JENIS_VIDEO_THUMBNAIL)
                bm= ThumbnailUtils.createVideoThumbnail(pathFoto[i], MediaStore.Video.Thumbnails.MICRO_KIND);

            if(bm != null) {
                bm = skalaFoto(bm, ukuranPokok);
                if(bentukFoto== BENTUK_KOTAK)
                    bm = kropFotoKotak(bm);
                bufferBitmap[i % batasBuffer] = bm;
            } else
                isiBgTakBisa(i % batasBuffer);
        }
    } private void isiFileFoto(int posisi, int ukuranPokok){
        File file= new File(pathFoto[/*pathFoto.length-1-*/posisi]);
        Bitmap bm= null;
        int jenisFoto= (this.jenisFoto == JENIS_CAMPURAN) ? jenisFoto(posisi) : this.jenisFoto;
        if(jenisFoto == JENIS_FOTO) {
            if(file.length() /1024 < (8*1024) && file.length() < Runtime.getRuntime().totalMemory()/*batasMemori[posisi % batasBuffer]*/)
                bm = BitmapFactory.decodeFile(pathFoto[posisi]);
        }
        else if(jenisFoto == JENIS_VIDEO_THUMBNAIL)
            bm= ThumbnailUtils.createVideoThumbnail(pathFoto[posisi], MediaStore.Video.Thumbnails.MINI_KIND);

        if(bm != null) {
            bm = skalaFoto(bm, ukuranPokok);
            if (bentukFoto == BENTUK_KOTAK)
                bm = kropFotoKotak(bm);
        }
        bufferBitmap[posisi % batasBuffer] = bm;
    }
    private void isiFileFotoDipilih(int posisi, int urutan, int ukuranPokok){
        File file= new File(pathFoto[/*pathFoto.length-1-*/posisi]);
        Bitmap bm= null;
        int jenisFoto= (this.jenisFoto == JENIS_CAMPURAN) ? jenisFoto(posisi) : this.jenisFoto;
        if(jenisFoto == JENIS_FOTO) {
            if(file.length() /1024 < (8*1024) && file.length() < Runtime.getRuntime().totalMemory()/*batasMemori[posisi % batasBuffer]*/)
                bm = BitmapFactory.decodeFile(pathFoto[posisi]);
        }
        else if(jenisFoto == JENIS_VIDEO_THUMBNAIL)
            bm= ThumbnailUtils.createVideoThumbnail(pathFoto[posisi], MediaStore.Video.Thumbnails.MINI_KIND);

        if(bm != null) {
            bm = skalaFoto(bm, ukuranPokok);
            if (bentukFoto == BENTUK_KOTAK)
                bm = kropFotoKotak(bm);
        }
        bitmapDipilih.ambil(urutan).isi(bm);
    }

    public void aturLpWadahImg(ViewGroup.LayoutParams lp){
        lpImgWadah= lp;
        if(bufferView[0] != null)
            for(int i= 0; i< batasBuffer; i++)
                bufferView[i].setLayoutParams(lp);
    }
    public void aturElemenImg(int wadah, int id){
        if(wadah < 0 || id < 0)
            return;
        sumberImgWadah= wadah;
        idElemenImg= id;
        elemenImg= false;
    }
    public void resetElemenImg(){
        idElemenImg= -1;
        sumberImgWadah= -1;
        elemenImg= true;
    }
    public int ambilSumberImgWadah(){
        return sumberImgWadah;
    }
    public int ambilIdElemenImg(){
        return idElemenImg;
    }
    public boolean elemenImgKah(){
        return elemenImg;
    }

    private void isiBufferView(int posisi, int sejumlah){
        for(int i= posisi; i< posisi +sejumlah; i++){
            isiBufferView(i);
        }
    } private View isiBufferView(int posisi){
        View img= (sumberImgWadah == -1) ?
                new ImageView(konteks) :
                ((LayoutInflater)konteks.getSystemService(konteks.LAYOUT_INFLATER_SERVICE))
                        .inflate(sumberImgWadah, null);
        if(lpImgWadah != null)
            img.setLayoutParams(lpImgWadah);
        isiBufferView(img, posisi);
        return img;
    } private void isiBufferView(View v, int posisi){
        if(bisaDiScale)
            tambahScaleGesture(v);
        bufferView[posisi % batasBuffer]= v;
        isiBg(posisi % batasBuffer);
    }

    private void isiBitmapDipilih(int urutan, final ImageView img){
        BitmapHandler bh= new BitmapHandler();
        bh.aturPenungguProses(new BitmapHandler.PenungguProses() {
            @Override
            public void proses(BitmapHandler handler) {
                if(aksiProsesBitmap != null)
                    aksiProsesBitmap.proses(handler);
            }

            @Override
            public void selesai(BitmapHandler handler, Bitmap bm, boolean tuntas) {
                if(tuntas){
//                    img.setImageBitmap(bm);
                    handler.pasangKeImage(img);
                }
                else
                    isiBgTakBisa(img);
                if(aksiProsesBitmap != null)
                    aksiProsesBitmap.selesai(bm, tuntas);
            }
        });
        bitmapDipilih.tambah(bh, urutan);
    }/*
    public boolean pasangBitmapDipilihKeImage(ImageView img, int urutan){
        return bitmapDipilih.ambil(urutan).pasangKeImage(img);
    }*/

    private Bitmap kropFotoKotak(Bitmap bm){
        int pjg= bm.getWidth();
        int lbr= bm.getHeight();

        if(lbr < pjg)
            pjg= lbr;

        int mulaiX= bm.getWidth()/2 - pjg/2;
        int mulaiY= bm.getHeight()/2 - lbr/2;

        return Bitmap.createBitmap(bm, mulaiX, mulaiY, pjg, pjg);
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

    public void aturPathItem(String path[]){
        pathFoto= path;
        perbaruiBatasBuffer(batasBuffer);
    }
    public String[] ambilDaftarPathFoto(){
        return pathFoto;
    }
    public String ambilPathFoto(int posisi){
        return pathFoto[posisi];
    } public String ambilPathFoto(Bitmap bm){
        return pathFoto[cariIndBitmap(bm)];
    }

    public int cariIndBitmap(Bitmap bm){
        Bitmap bmCek;
        for(int i= 0; i<pathFoto.length; i++){
            bmCek= BitmapFactory.decodeFile(pathFoto[i]);
            if(bmCek.sameAs(bm))
                return i;
        }
        return -1;
    }

    public Bitmap[] ambilBufferFoto(){
        return bufferBitmap;
    } public Bitmap ambilBitmap(int posisi){
        return bufferBitmap[posisi % batasBuffer];
    }
    public Array<BitmapHandler> ambilBitmapDipilih(){
        return bitmapDipilih;
    }
    //BitmapHandler hanya untuk bitmap yg dipilih karena untuk membaca Bitmap dari file butuh waktu lama
    public BitmapHandler ambilBitmapHandler(int urutan){
        return bitmapDipilih.ambil(urutan);
    }
    public Bitmap ambilBitmapDipilih(int ke){
        return bitmapDipilih.ambil(ke).bitmap();
    }

    public View ambilView(int posisi){
        return pilahView(posisi);
    } public View[] ambilView(){
        return bufferView;
    }

    public void aturJmlItemPerGaris(int jmlItem){
        jmlItemPerGaris= jmlItem;
    }
    public int ambilJmlItemPerGaris(){
        return jmlItemPerGaris;
    }
    public void aturOffsetItem(int offset){
        offsetItem= offset;
    }
    public int ambilOffsetItem(){
        return offsetItem;
    }
    public void aturUkuranPratinjau(int ukuranPokok){
        if(ukuranPokok < 0) {
            DisplayMetrics display = new DisplayMetrics();
            ((Activity) konteks).getWindowManager().getDefaultDisplay().getMetrics(display);
            if (ukuranPokok == UKURAN_MENYESUAIKAN_LBR_INDUK)
                ukuranPokok = (display.widthPixels /jmlItemPerGaris) -offsetItem;
            else if (ukuranPokok == UKURAN_MENYESUAIKAN_PJG_INDUK)
                ukuranPokok = (display.heightPixels /jmlItemPerGaris) -offsetItem;
        }
        ukuranPratinjau = ukuranPokok;
    }
    public float ambilUkuranPratinjau(){
        return ukuranPratinjau;
    }

    private void pasangAksesoris(View v){
        if(sumberAksesoris.ukuran() > 0) {
            ViewGroup vg = (ViewGroup) v;
            for(int i= 0; i<sumberAksesoris.ukuran(); i++) {
                ImageView imgAksesoris = new ImageView(konteks);
                imgAksesoris.setLayoutParams(lpAksesoris.ambil(i));
                imgAksesoris.setImageResource(sumberAksesoris.ambil(i));
                imgAksesoris.setId(idAksesoris.ambil(i));
                vg.addView(imgAksesoris);
            }
        }
    }
/*
    public View buatFoto(int posisi){
        return buatFoto(new ImageView(konteks), posisi);
    }
*/
    public View buatFoto(final int posisi){
        return buatFoto(null, posisi);
    }
    public View buatFoto(View v, final int posisi){
        int ind= posisi % batasBuffer;
        if(posisi /batasBuffer != indKelihatan[ind] && dipilih[posisi]== 0 /*jmlDipilihTertunda == 0*/){
            if(v == null)
                v= isiBufferView(posisi);
            else
                isiBufferView(v, posisi);
            indKelihatan[ind]= posisi /batasBuffer;
            ImageView img;
            if(elemenImg)
                img= (ImageView) v;
            else {
                img= v.findViewById(idElemenImg);
                pasangAksesoris(v);
            }
            pasangThumbnail(img, posisi);
        }
        return pilahView(posisi);
    }
    private View pilahView(final int posisi){
        if(dipilih[posisi] == 0)
            return bufferView[posisi % batasBuffer];
        else
            return viewDipilih.ambil(dipilih[posisi] -1);
    }
    private void pasangThumbnail(final ImageView img, final int posisi){
        final int ind= posisi % batasBuffer;
        if(loaderThumbnail[ind] != null) {
            loaderThumbnail[ind].cancel(true);
        }
        loaderThumbnail[ind]= new AsyncTask<Integer, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Integer... integers) {
                int posisi= integers[0].intValue();

//                batasMemori[ind]= Runtime.getRuntime().totalMemory() /*+(1024 *1024 *1)*/;
                isiFileFoto(posisi, ukuranThumbnail);
                if(aksiBuffer != null)
                    aksiBuffer.bufferThumbnail(posisi, batasBuffer);
                return bufferBitmap[ind];
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(bitmap != null) {
                    img.setImageBitmap(bitmap);
                    pasangFoto(img, posisi);
                }
                else
                    isiBgTakBisa(ind);
                super.onPostExecute(bitmap);
            }
        };
        loaderThumbnail[ind].execute(posisi);
    }
    private void pasangFoto(final ImageView img, final int posisi){
        final int ind= posisi % batasBuffer;
        if(loader[ind] != null) {
            loader[ind].cancel(true);
        }
        loader[ind]= new AsyncTask<Integer, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Integer... integers) {
                int posisi= integers[0].intValue();

                isiFileFoto(posisi, ukuranPratinjau);
                if(aksiBuffer != null)
                    aksiBuffer.bufferUtama(posisi, batasBuffer);
                updateJmlFotoDiload(posisi);
                return bufferBitmap[ind];
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(bitmap != null)
                    img.setImageBitmap(bitmap);
                else
                    isiBgTakBisa(ind);
                super.onPostExecute(bitmap);
            }
        };
        loader[ind].execute(posisi);
    }
    private void pasangFotoDipilih(final ImageView img, final int posisi, final int urutan){
//        final int ind= posisi % batasMaksDipilih;
            if(loaderDipilih[urutan] != null) {
            loaderDipilih[urutan].cancel(true);
        }
        loaderDipilih[urutan]= new AsyncTask<Integer, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Integer... integers) {
                int posisi= integers[0].intValue();

                bitmapDipilih.ambil(urutan).isiBitmap(pathFoto[posisi], ukuranPratinjau, jenisFoto, bentukFoto);
//                isiFileFotoDipilih(posisi, urutan, ukuranPratinjau);
                if(aksiBuffer != null)
                    aksiBuffer.bufferThumbnail(posisi, batasBuffer);
//                updateJmlFotoDiload(posisi);
                jmlUdahDiload++;
                return bitmapDipilih.ambil(urutan).bitmap();
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(bitmap != null){
//                    img.setImageBitmap(bitmap);
                    if(posisi == jmlDipilihTertunda-1){
                        dipilihLengkap= true;
                        jmlDipilihTertunda= 0;
                    }
                }
/*
                else
                    isiBgTakBisa(ind);
*/
/*
                if(posisi == jmlDipilihTertunda-1)
                    Toast.makeText(konteks, "bolehRumpang= " +viewDipilih.bolehRumpang(), Toast.LENGTH_LONG).show();
*/
                super.onPostExecute(bitmap);
            }
        };
        loaderDipilih[urutan].execute(posisi);
    }

/*
    !!!CATETAN:
    jika:
    posisi = posisi img saat ditampilkan di galeri
    urutan = urutan img di galeri dipilih
    posisi% = posisi img di array (arrayList)


    maka:
    dipilih[posisi]= urutan
    viewDipilih.get(urutan)  ->  ditambahkan sesuai urutan
    mappingDipilih[urutan]= posisi%
*/

    private class PelaksanaIndTerpilihTertunda{
        private int indTerahir;
    }

//    ==BARU UPDATE!!!
    //posisi = index pathFoto
    private void pilihFoto(int posisi, int urutan, boolean awal){
//        urutanDipilih.tambah(posisi, urutan);
        int urutanTertera= urutan+1;
        dipilih[posisi]= urutanTertera;
        View v;
        Bitmap b;
        int indek= posisi % batasBuffer;
        if(!awal /*posisi /batasBuffer == indKelihatan[indek]*/ ) {
            v = bufferView[indek];
            b= bufferBitmap[indek];
            bitmapDipilih.tambah(new BitmapHandler(b), urutan);
            viewDipilih.tambah(v, urutan);
        } else{
            v= isiBufferView(posisi);
            ImageView img;
            if(elemenImg)
                img= (ImageView) v;
            else
                img= v.findViewById(idElemenImg);
            viewDipilih.tambah(v, urutan);
            isiBitmapDipilih(urutan, img);
            pasangFotoDipilih(img, posisi, urutan);
        }

        cursorDipilih++;
        if(aksiPilihFoto != null)
            aksiPilihFoto.pilihFoto(v, posisi);
    }
    public void pilihFoto(int posisi){
        pilihFoto(posisi, cursorDipilih, false);
    }
//    ==Batal pilih masih error!!!
    public void batalPilihFoto(final int posisi){
//        Integer objPosisi= new Integer(posisi);
        int urutan= dipilih[posisi]-1;//urutanDipilih.indekAwal(objPosisi);

        if(aksiPilihFoto != null) {
            View v= viewDipilih.ambil(urutan);
            aksiPilihFoto.batalPilihFoto(v, posisi);
        }
        viewDipilih.hapus(urutan);
        bitmapDipilih.hapus(urutan);
//        bitmapDipilih.remove(null);

//        !!!belum selesai;
//        urutanDipilih.hapus(urutan);
/*
        int ygBerkurang= cursorDipilih -urutan;
        for(int i= 0; i< dipilih.length; i++)
            if(dipilih[i] > urutan){
                dipilih[i]--;
                ygBerkurang--;
            } else if(ygBerkurang== 0)
                break;
*/
        updateFotoDipilih(dipilih[posisi]);
        dipilih[posisi]= 0;
        cursorDipilih--;
    }
    //update ind foto dipilih saat yg dipilih berkurang
    private void updateFotoDipilih(int urutan){
        int ygBerkurang= cursorDipilih -urutan;
        for(int i= 0; i< dipilih.length; i++)
            if(dipilih[i] > urutan){
                dipilih[i]--;
                ygBerkurang--;
            } else if(ygBerkurang== 0)
                break;
/*
        int hitungan= cursor;
        for(int i= 0; i< jmlUdahDiload; i++)
            if(hitungan == cursorDipilih)
                return;
            else if(dipilih[i] > cursor){
                dipilih[i]--;
                hitungan++;
            }
*/
    }

    public boolean dipilihUdahLengkap(){
        return dipilihLengkap;
    }
//    ==Belum Diupdate
    public void aturIndDipilih(int indPosisiDipilih[], int indUrutanDipilih[]){
        if(indPosisiDipilih == null || indUrutanDipilih == null)
            throw new RuntimeException("\"int indPosisiDipilih[]\" dan \"int indUrutanDipilih[]\" gak boleh kosong!");
        Array<Integer> indPosisi= new Array<>();
        Array<Integer> indUrutan= new Array<>();
        for(int i= 0; i< indPosisiDipilih.length; i++)
            if(indPosisiDipilih[i] != ELEMEN_TAK_DIPILIH && indUrutanDipilih[i] != ELEMEN_TAK_DIPILIH){
                indPosisi.tambah(indPosisiDipilih[i]);
                indUrutan.tambah(indUrutanDipilih[i]);
            }
        aturIndDipilih(indPosisi, indUrutan);
    }
    public void aturIndDipilih(Array<Integer> indPosisiDipilih, Array<Integer> indUrutanDipilih){
/*
        1. ambil indek item dipilih yang terbesar
        2. urutkan urutan dipilih
        3. cek kelengkapan view yang dipilih
        4. lakukan (pilihFoto()) sesuai urutan yang sudah diurutkan
*/
        if(indPosisiDipilih.ukuran() != indUrutanDipilih.ukuran())
            throw new RuntimeException("Ukuran indPosisiDipilih (" +indPosisiDipilih.ukuran()
                    +") tidak sama dengan ukurang indUkuranDipilih (" +indUrutanDipilih.ukuran() +")");
        if(indPosisiDipilih.ukuran() == 0)
            return;

        dipilihLengkap= false;

        jmlDipilihTertunda= indPosisiDipilih.ukuran();
        int jmlDipilihJalan= 0;

//        Toast.makeText(konteks, "jmlDipilihTertunda= " +jmlDipilihTertunda, Toast.LENGTH_SHORT).show();

        cursorDipilih= 0;
        perbaruiIndKelihatan(0, batasBuffer);
        perbaruiDipilih();

        viewDipilih.hapusSemua(); //new Array<>(true);
        viewDipilih.aturBolehRumpang(true);
        bitmapDipilih.hapusSemua(); //new Array<>(true);
        bitmapDipilih.aturBolehRumpang(true);

        for(int i= 0; i< indPosisiDipilih.ukuran(); i++)
            if(indPosisiDipilih.ambil(i) != ELEMEN_TAK_DIPILIH) {
                pilihFoto(indPosisiDipilih.ambil(i), indUrutanDipilih.ambil(i)-1, true);
                jmlDipilihJalan++;
            }

        viewDipilih.aturBolehRumpang(false);
        bitmapDipilih.aturBolehRumpang(false);
        if(jmlDipilihJalan== 0){
            jmlDipilihTertunda= 0;
            dipilihLengkap= true;
            Toast.makeText(konteks, "bolehRumpang= GAK MASUK!!!", Toast.LENGTH_SHORT).show();
        }
    }
    public int ambilUrutanDipilih(int posisi){
        return dipilih[posisi];
    }
    public int ambilUrutanDipilih(View v){
        return viewDipilih.indekAwal(v) +1;
    }
    public int[][] ambilUrutanDipilih(){
        int batas= viewDipilih.ukuran();
        int hitung= 0;
        int indDipilih[][]= new int[2][batas]; //indDipilih[0]= posisi; indDipilih[1]= urutan;

        for(int i= 0; i< dipilih.length; i++)
            if(hitung == batas)
                break;
            else if(dipilih[i] > 0) {
                indDipilih[0][hitung] = i;
                indDipilih[1][hitung++] = dipilih[i];
            }
/*
        batas--;
        for(int i= 0; i< batas; i++)
            if(indDipilih[1][i] > indDipilih[1][i+1]){
                int smtr= indDipilih[0][i];
                indDipilih[0][i]= indDipilih[0][i+1];
                indDipilih[0][i+1]= smtr;

                smtr= indDipilih[1][i];
                indDipilih[1][i]= indDipilih[1][i+1];
                indDipilih[1][i+1]= smtr;
            }
*/
        return indDipilih;
    }

    public boolean fotoDipilih(int posisi){
        return dipilih[posisi] > 0;
    }
    public boolean fotoDipilih(View v){
        int indek= viewDipilih.indekAwal(v);
        return indek > -1;
    }

    private void updateJmlFotoDiload(int posisi){
        if(posisi > jmlUdahDiload -1)
            jmlUdahDiload = posisi+1;
    }

    public Array<View> ambilViewDipilih(){
        return viewDipilih;
    }
    public View ambilViewDipilih(int indKe){
        return viewDipilih.ambil(indKe);
    }
/*
    public Array<BitmapHandler> ambilBitmapDipilih(){
        return bitmapDipilih;
    }
    public Bitmap ambilBitmapDipilih(int indKe){
        return bitmapDipilih.ambil(indKe).bitmap();
    }
*/
    public String ambilPathDipilih(int urutan){
        String pathDipilih[]= ambilPathDipilih();
        return pathDipilih[urutan];
    }
    public String[] ambilPathDipilih(){
        String pathDipilih[]= new String[viewDipilih.ukuran()];
        int batas= viewDipilih.ukuran();
        int jalan= 0;

//        int ururtanDipilih[]= new int[viewDipilih.ukuran()];
        for(int i= 0; i< dipilih.length; i++)
            if(dipilih[i] > 0) {
                pathDipilih[dipilih[i] -1]= pathFoto[i];
                jalan++;
            }
            else if(jalan == batas)
                break;
        return pathDipilih;
    }
/*
    ==SAMPAI SINI!!!!
    public int[][] ambilIndDipilih(){
        int batas= viewDipilih.ukuran();
        int hitung= 0;
        int indDipilih[][]= new int[2][batas];
        for(int i= 0; i< urutanDipilih.ukuran(); i++) {
            indDipilih[0][hitung] = i;
            indDipilih[1][hitung++] = dipilih[i];
        }
        return indDipilih;
    }
*/
    public int ambilJmlDipilih(){
        return cursorDipilih;
    }
    public int ambilJmlUdahDiload(){
        return jmlUdahDiload;
    }

    public String ambilJudulDipilih(int urutan){
        String pathDipilih[]= ambilPathDipilih();
        String arrayPath[]= pathDipilih[urutan].split("/");
        return arrayPath[arrayPath.length-1];
    }
    public ArrayList<String> ambilJudulDipilih(){
        ArrayList<String> judul= new ArrayList<String>();
        String pathDipilih[]= ambilPathDipilih();
        for(int i= 0; i<pathDipilih.length; i++){
            String array[]= pathDipilih[i].split("/");
            judul.add(array[array.length-1]);
        }
        return judul;
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
}
