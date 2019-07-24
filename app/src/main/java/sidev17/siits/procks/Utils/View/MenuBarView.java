package sidev17.siits.procks.Utils.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.Array;
import sidev17.siits.procks.Utils.Ukuran;
import sidev17.siits.procks.Utils.ViewTool.Aktifitas;
import sidev17.siits.procks.Utils.ViewTool.Batas;
import sidev17.siits.procks.Utils.Warna;


public class MenuBarView extends ImgViewTouch {
    public static final int ARAH_HORIZONTAL= 11;
    public static final int ARAH_VERTIKAL = 12;

    public static final int BAR_DI_ATAS= 511;
    public static final int BAR_DI_BAWAH= 512;
    public static final int BAR_DI_KANAN= 513;
    public static final int BAR_DI_KIRI= 514;
    //
    //untuk #BAR_DI_ATAS dan #BAR_DI_BAWAH
    public static final int BAR_RATA_KANAN= 521;
    public static final int BAR_RATA_KIRI= 522;
    //
    //untuk #BAR_DI_KANAN dan #BAR_DI_KIRI
    public static final int BAR_RATA_ATAS= 524;
    public static final int BAR_RATA_BAWAH= 525;
    //
    //untuk semua letak bar
    public static final int BAR_RATA_TENGAH= 523;

    public static final int WARNA_TERSEDIA= 21;
    public static final int WARNA_TAK_TERSEDIA= 22;
    public static final int WARNA_KUAT= 23;
    public static final int WARNA_LEMAH= 24;

    public static final int LATAR_KOSONG= -30;
    public static final int LATAR_WARNA= -31;
    public static final int LATAR_KOTAK_TUMPUL= -32;

    public static final int ITEM_TERSEDIA= 31;
    public static final int ITEM_TAK_TERSEDIA= 32;

    private Context konteks;
    private DisplayMetrics resolusi;
    private int arahBar;
    private int letakBar;
    private boolean letakBarDisesuaikan= false;
    private int rataanBar;
    private boolean rataanBarDisesuaikan= false;
    private int pjgBar;
    private int lbrBar;
    private float letakBarX = -1;
    private float letakBarY = -1;
    private boolean menuDitampilkan = false;
    private boolean aksiSentuhItemDefault= false;
    private boolean menuBisaDitampilkan= true;
    private boolean aksiKlik= true;
    private int padding;
    private int elevasiBar;

    private int pjgInduk;
    private int lbrInduk;

    private RelativeLayout viewBar;
    private Array<ImgViewTouch> daftarItem= new Array<>();
    private Array<Integer> daftarItemTersedia= new Array<>();
    private int jmlItem= 0;
    private int ukuranItem;
    private int jarakAntarItem= 0;

    private int sumberLatarInduk_Awal= LATAR_KOSONG;
    private int sumberLatarInduk_Akhir= LATAR_KOSONG;
    private int sumberGambarInduk= -1;
    private int sumberGambarIndukAwal= -1;
    private int sumberLatarBar= -1;

    private String warnaKuat;
    private String warnaLemah;
    private String warnaTakTersedia;
    private String warnaTersedia;
    private String warnaLatar;
    private String warnaInduk; //warna item / gambar induk

    private boolean pengaturanAwal= true;

    private PenungguKlik_BarView pngKlikBar;
    private Batas batas;


    public MenuBarView(Context kntk){
        this(kntk, null);
/*
        konteks= kntk;

        resolusi= getResources().getDisplayMetrics();
        aturDefault();

        aturPanjang(ViewGroup.LayoutParams.WRAP_CONTENT);
        aturLebar(ViewGroup.LayoutParams.WRAP_CONTENT);
        aturWarnaKuat("#000000");
        aturWarnaLemah("#BFBFBF");
        aturWarnaLatar("#FFFFFF");
//        aturUkuranItem(Ukuran.DpKePx(30));
        initInduk();

        aturArahBar(ARAH_HORIZONTAL);

        inisiasiViewBar();
        aturWarnaTakTersedia("#888888");
        aturWarnaTersedia("#000000");
        aturLatarBar(R.drawable.latar_kotak_tumpul);

        aturLetakBarRelatif(BAR_DI_KANAN);
        setElevation(elevasiBar);
*/
    }
    /*
    public MenuBarView(Context kntk, int jmlItem){
        konteks= kntk;
        this.jmlItem= jmlItem;

        aturPanjang(ViewGroup.LayoutParams.WRAP_CONTENT);
        aturLebar(ViewGroup.LayoutParams.WRAP_CONTENT);
        aturWarnaKuat("#000000");
        aturWarnaLemah("#BFBFBF");
        aturWarnaLatar("#FFFFFF");

        inisiasiDaftarItem();
        aturUkuranItem(30);
        aturArahBar(ARAH_HORIZONTAL);

        inisiasiViewBar();
        aturWarnaTakTersedia("#888888");
        aturWarnaTersedia("#000000");
        aturLatarBar(R.drawable.latar_kotak_tumpul);
        pasangItem();
    }
    /*
    public MenuBarView(Context kntk, int item[]){
        konteks= kntk;
        jmlItem= item.length;

        aturPanjang(ViewGroup.LayoutParams.WRAP_CONTENT);
        aturLebar(ViewGroup.LayoutParams.WRAP_CONTENT);
        aturWarnaKuat("#000000");
        aturWarnaLemah("#BFBFBF");
        aturWarnaLatar("#FFFFFF");

        inisiasiDaftarItem();
        aturUkuranItem(30);
        aturArahBar(ARAH_HORIZONTAL);

        inisiasiViewBar();
        aturWarnaTakTersedia("#888888");
        aturWarnaTersedia("#000000");
        aturLatarBar(R.drawable.latar_kotak_tumpul);
        pasangItem();

        for(int i= 0; i<jmlItem; i++)
            aturGmbItem(i, item[i]);
    }
    */
    public MenuBarView(Context kntk, @Nullable AttributeSet attrs){
        this(kntk, attrs, 0);
    } public MenuBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    public MenuBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                       int defStyleRes) {
        super(context, attrs);

        konteks= context;

        resolusi= getResources().getDisplayMetrics();
        aturDefault();

        aturPanjangInternal(ViewGroup.LayoutParams.WRAP_CONTENT);
        aturLebarInternal(ViewGroup.LayoutParams.WRAP_CONTENT);
//        sesuaikanUkuranBar();
        aturWarnaKuat("#000000");
        aturWarnaLemah("#BFBFBF");
        aturWarnaLatar("#FFFFFF", false);
        initInduk();

        inisiasiViewBar();
        aturArahBar(ARAH_HORIZONTAL);
        aturWarnaTakTersedia("#888888");
        aturWarnaTersedia("#000000");
        aturLatarBar(R.drawable.latar_kotak_tumpul);

        aturLetakBarRelatif(BAR_DI_KANAN);
        aturRataanBarRelatif(BAR_RATA_ATAS);
        setElevation(elevasiBar);

        batasDefault();

        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuBarView);
            aturWarnaKuat(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_kuat, Color.parseColor(warnaKuat))));
            aturWarnaLemah(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_lemah, Color.parseColor(warnaLemah))));
            aturWarnaInduk(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_induk, Color.parseColor(warnaKuat))));

            String warnaTersedia= Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_tersedia, Color.parseColor(this.warnaTersedia)));
            String warnaTakTersedia= Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_tak_tersedia, Color.parseColor(this.warnaTakTersedia)));
            String warnaLatar= Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_latar, Color.parseColor(warnaKuat)));

            if(warnaTersedia.equals(this.warnaTersedia) && warnaTakTersedia.equals(this.warnaTakTersedia))
                aturWarnaLatar(warnaLatar);
            else{
                aturWarnaTersedia(warnaTersedia);
                aturWarnaTakTersedia(warnaTakTersedia);
                aturWarnaLatar(warnaLatar, false);
            }

            aturLatarInduk_Akhir(a.getResourceId(R.styleable.MenuBarView_latarInduk_AKHIR, -1));
            aturLatarInduk_Awal(a.getResourceId(R.styleable.MenuBarView_latarInduk_AWAL, LATAR_KOSONG));
            aturGambarInduk(a.getResourceId(R.styleable.MenuBarView_gambarInduk_AKHIR, -1));
            aturGambarIndukDefault(a.getResourceId(R.styleable.MenuBarView_gambarInduk_AWAL, -1));

            aturLatarBar(a.getResourceId(R.styleable.MenuBarView_BAR_latarInduk, -1));
        }
        penungguInternal();
        sesuaikanUkuranInduk();
    }
/*
    @Override
    protected void onDraw(Canvas canvas) {
        canvas= new Canvas(bitmapDariView(inflate(getContext(), R.layout.model_cob_custom, null)));
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
    }
*/
    public static Bitmap bitmapDariView(View v){
/*
//        if(!v.isDrawingCacheEnabled())
            v.setDrawingCacheEnabled(true);
        v.destroyDrawingCache();
        v.buildDrawingCache();
        Bitmap bm= Bitmap.createBitmap(v.getDrawingCache());
        v.destroyDrawingCache();
        v.setDrawingCacheEnabled(false);
*/
        Bitmap bm;
        if(v.getMeasuredHeight() <= 0) {
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bm = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        } else{
            bm= Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        }
        Canvas c = new Canvas(bm);
        v.draw(c);
        return bm;
    }
/*
=========================
Overrides - Bawahan
=========================
*/
    public void aturDefault(){
        padding= Ukuran.DpKePx(5, resolusi);
        elevasiBar = Ukuran.DpKePx(4, resolusi);
        ukuranItem= Ukuran.DpKePx(30, resolusi);
    }
    /*
=========================
Overrides - Bawahan
=========================
*/
    private void penungguInternal(){
        final MenuBarView superView= this;
        aturPenungguKlik_Internal(new PenungguKlik_Internal() {
            @Override
            public void klik_Int(View v) {
                boolean menuBisaDitampilkanAwal= menuBisaDitampilkan;
                if(pngKlikBar != null && aksiKlik)
                    pngKlikBar.klik(superView, menuDitampilkan);
                klikInduk(menuBisaDitampilkanAwal);
            }
        });
    }
    public final boolean klik(){
        return super.performClick();
    } public final boolean klik(boolean aksiKlik){
        this.aksiKlik= aksiKlik;
        return klik();
    }
    public final boolean klikTampilkan(boolean tampilkan){
        menuDitampilkan= !tampilkan;
        return klik();
    }
    private final void klikInduk(boolean menuBisaDitampilkan){
        if(menuBisaDitampilkan){
            if(jmlItem > 0 && !menuDitampilkan) {
                if(!letakBarDisesuaikan){
                    aturLetakBarRelatif(letakBar, true);
                    sesuaikanLetakBarRelatif();
                }
                if(!rataanBarDisesuaikan){
                    aturRataanBarRelatif(rataanBar, true);
                    sesuaikanRataanBarRelatif();
                }
                if(!aksiSentuhItemDefault)
                    aturAksiSentuhItemDefault();
                tampilkanBar();
                if(sumberLatarInduk_Awal == -1)
                    tampilkanLatar();
                latarIndukAkhir();
            }
            else if(menuDitampilkan) {
                hilangkanBar();
//            hilangkanBatas();
                if(sumberLatarInduk_Awal == -1)
                    sembunyikanLatar();
                latarIndukAwal();
            }
        }
    }

    public void menuBisaDitampilkan(boolean bisa){
        menuBisaDitampilkan= bisa;
    }
    public boolean menuBisaDitampilkan(){
        return menuBisaDitampilkan;
    }
    public boolean menuDitampilkan(){
        return menuDitampilkan;
    }

/*
=========================
Penunggu
=========================
*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    public void aturPenungguKlikBar(PenungguKlik_BarView png){
        pngKlikBar= png;
    }

    public void aturAksiSentuhItem(int posisiItem, PenungguSentuhan png){
        if(daftarItem.ambil(posisiItem) != null && posisiItem < jmlItem)
            daftarItem.ambil(posisiItem).aturPenungguSentuhan(png);
    }
    public void aturAksiKlikItem(int posisiItem, PenungguKlik png){
        if(daftarItem.ambil(posisiItem) != null && posisiItem < jmlItem)
            daftarItem.ambil(posisiItem).aturPenungguKlik(png);
    }
/*
=========================
Interface - Penunggu
=========================
*/
    public interface BarViewAdapter{
        String IdItem(int posisi);

    }
    public interface PenungguKlik_BarView{
        void klik(MenuBarView v, boolean menuDitampilkan);
    }

/*
=========================
Batas View
=========================
*/
    public void batasDefault(){
        batas= new Batas(this, konteks);
        batas.tambahBatas(viewBar);
        batas.aturPenungguSentuh_Internal(new Aktifitas.PenungguSentuh_Internal() {

            @Override
            public boolean sentuh_Int(Array<View> view, MotionEvent event, boolean diDalam) {
                if(!diDalam && event.getAction() == MotionEvent.ACTION_UP && menuBisaDitampilkan) {
                    ((MenuBarView) view.ambil(0)).klikTampilkan(false);
//                    if(diDalam);
//                        Cek.toast(konteks, "luar!!!");
                }
//                Toast.makeText(konteks, "menuBisaDitampilkan= " +menuBisaDitampilkan, Toast.LENGTH_LONG).show();
                return diDalam;
            }
        });
    }
    public void aturPenungguBantas(Aktifitas.PenungguSentuh png){
        batas.aturPenungguSentuh(png);
    }
    private void hilangkanBatas(){
        batas= null;
    }
    public Batas ambilBatas(){
        return batas;
    }
    public void tambahBatas(View v){
        batas.tambahBatas(v);
    }
    public void kurangiBatas(View v){
        batas.kurangiBatas(v);
    }
    public void gantiBatas(View v){
        batas.gantiBatas(v);
    }

/*
=========================
Arah - Ukuran - Letak Latar
=========================
*/
    public void aturArahBar(int kategoriArah){
        if(kategoriArah != ARAH_HORIZONTAL && kategoriArah != ARAH_VERTIKAL)
            return;
        arahBar= kategoriArah;
        RelativeLayout.LayoutParams lp;
        for(int i= 0; i< jmlItem; i++){
            lp= (RelativeLayout.LayoutParams) daftarItem.ambil(i).getLayoutParams();
            if(arahBar == ARAH_HORIZONTAL){
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                lp.addRule(RelativeLayout.BELOW, 0);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                if(i > 0)
                    lp.addRule(RelativeLayout.RIGHT_OF, daftarItem.ambil(i - 1).getId());
            }
            else if(arahBar == ARAH_VERTIKAL){
                lp.addRule(RelativeLayout.CENTER_VERTICAL, 0);
                lp.addRule(RelativeLayout.RIGHT_OF, 0);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                if(i > 0)
                    lp.addRule(RelativeLayout.BELOW, daftarItem.ambil(i - 1).getId());
            }
//            daftarItem.ambil(i).setLayoutParams(lp);
        }
        sesuaikanUkuranBar();
    }

    public void aturLetakBarRelatif(int letak){
        if(letak != BAR_DI_ATAS && letak != BAR_DI_BAWAH
                && letak != BAR_DI_KANAN && letak != BAR_DI_KIRI)
            return;
        aturLetakBarRelatif(letak, false);
    }
    //untuk verifikasi letak relatif bar karena membutuhkan #getWidth atau #getHeight
    //yang didapat setelah view tergambar di layout.
    //Hanya dipanggil secara internal
    private void aturLetakBarRelatif(int letak, boolean udahDigambar){
        if(udahDigambar){
            Point layar= new Point();
            //ambil ukuran view sebelum dirender
            ((Activity) konteks).getWindowManager().getDefaultDisplay().getSize(layar);
            int pjgLayar= layar.x;
            int lbrLayar= layar.y;

            //ambil letak absolut view induk
            int letakAbs[]= new int[2];
            getLocationOnScreen(letakAbs);
            int x= letakAbs[0];
            int y= letakAbs[1];

            //measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layout(getLeft(), getTop(), getRight(), getBottom());

            switch(letak) {
                case BAR_DI_BAWAH:
                    aturArahBar(ARAH_VERTIKAL);
                    if (y +getHeight() +lbrBar > lbrLayar)
                        letak = BAR_DI_ATAS;
                    break;
                case BAR_DI_KANAN:
                    aturArahBar(ARAH_HORIZONTAL);
                    if (x +getWidth() +pjgBar > pjgLayar)
                        letak = BAR_DI_KIRI;
                    break;
                case BAR_DI_ATAS:
                    aturArahBar(ARAH_VERTIKAL);
                    if(y -lbrBar <= 0)
                        letak= BAR_DI_BAWAH;
                    break;
                case BAR_DI_KIRI:
                    aturArahBar(ARAH_HORIZONTAL);
                    if(x -pjgBar <= 0)
                        letak= BAR_DI_KANAN;
                    break;
            }
//            Toast.makeText(konteks, "letak= " +letak, Toast.LENGTH_SHORT).show();
        }
        letakBar= letak;
        letakBarDisesuaikan= false;
    }
    //untuk penyesuaian letak relatif bar setelah kategori letak relatif sudah ditetapkan.
    //Menyesuaikan x dan y bar.
    private void sesuaikanLetakBarRelatif(){
        //letak absolut view induk di layar
        int letakAbs[]= new int[2];
        getLocationOnScreen(letakAbs);

        int x= letakAbs[0], y= letakAbs[1];
        switch(letakBar){
            case BAR_DI_BAWAH:
                y+= getHeight();
                break;
            case BAR_DI_KANAN:
                x+= getWidth();
                break;
            case BAR_DI_ATAS:
                y-= lbrBar;
                break;
            case BAR_DI_KIRI:
                x-= pjgBar;
                break;
        }
        aturLetakBar(x, y);
        letakBarDisesuaikan= true;
    }
    public int letakBarRelatif(){
        return letakBar;
    }

    public void aturRataanBarRelatif(int rataan){
        if(rataan != BAR_RATA_ATAS && rataan != BAR_RATA_BAWAH
                && rataan != BAR_RATA_KANAN && rataan != BAR_RATA_KIRI)
            return;
        aturRataanBarRelatif(rataan, false);
    }
    private void aturRataanBarRelatif(int rataan, boolean udahDigambar){
        if(letakBar == BAR_DI_ATAS || letakBar == BAR_DI_BAWAH){
            if(rataan == BAR_RATA_ATAS || rataan == BAR_RATA_BAWAH)
                rataan= BAR_RATA_KIRI;
        } else if(letakBar == BAR_DI_KIRI || letakBar == BAR_DI_KANAN){
            if(rataan == BAR_RATA_KIRI || rataan == BAR_RATA_KANAN)
                rataan= BAR_RATA_ATAS;
        }
        if(udahDigambar){
            Point layar= new Point();
            //ambil ukuran view sebelum dirender
            ((Activity) konteks).getWindowManager().getDefaultDisplay().getSize(layar);
            int pjgLayar= layar.x;
            int lbrLayar= layar.y;

            //ambil letak absolut view induk
            int letakAbs[]= new int[2];
            getLocationOnScreen(letakAbs);
            int x= letakAbs[0];
            int y= letakAbs[1];

            //measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layout(getLeft(), getTop(), getRight(), getBottom());

            switch(rataan) {
                case BAR_RATA_KIRI:
                    if(x +pjgBar > pjgLayar)
                        rataan= BAR_RATA_KANAN;
                    break;
                case BAR_RATA_ATAS:
                    if(y +lbrBar > lbrLayar)
                        rataan= BAR_RATA_BAWAH;
                    break;
                case BAR_RATA_KANAN:
                    if (x +getWidth() -pjgBar < 0)
                        rataan= BAR_RATA_KIRI;
                    break;
                case BAR_RATA_BAWAH:
                    if (y +getHeight() -lbrBar < 0)
                        rataan= BAR_RATA_ATAS;
                    break;
            }
//            Toast.makeText(konteks, "rataan= " +rataan, Toast.LENGTH_SHORT).show();
        }
        rataanBar= rataan;
        rataanBarDisesuaikan= false;
    }
    //harus dipanggil secara internal tepat setelah #sesuaikanLetakBarRelatif
    private void sesuaikanRataanBarRelatif(){
        //ambil letak absolut view induk di layar
        int letakAbs[]= new int[2];
        getLocationOnScreen(letakAbs);
        int x= letakAbs[0];
        int y= letakAbs[1];

        //hanya perlu menyesuaikan #BAR_RATA_KANAN dan #BAR_RATA_BAWAH karena secara default
        //rataan bar sudah #BAR_RATA_KIRI untuk #BAR_DI_ATAS dan #BAR_DI_BAWAH
        //sedangkan #BAR_RATA_ATAS untuk #BAR_DI_KIRI dan #BAR_DI_KANAN
        switch(rataanBar){
            case BAR_RATA_KANAN:
                x+= getWidth() -pjgBar;
                aturLetakBarX(x);
                break;
            case BAR_RATA_BAWAH:
                y+= getHeight() -lbrBar;
                aturLetakBarY(y);
                break;
        }
        rataanBarDisesuaikan= true;
    }
    public int rataanBarRelatif(){
        return rataanBar;
    }

    private void sesuaikanUkuranBar(){
        int ukuran[]= Ukuran.ukuranView(viewBar);

        int panjang= (ukuran[0] > ukuran[1]) ? ukuran[0] : ukuran[1];
        int pendek= (ukuran[0] > ukuran[1]) ? ukuran[1] : ukuran[0];

        if(arahBar == ARAH_HORIZONTAL){
            pjgBar= panjang;
            lbrBar= pendek;
        } else if(arahBar == ARAH_VERTIKAL){
            pjgBar= pendek;
            lbrBar= panjang;
        }
    }
    private void sesuaikanUkuranInduk(){
        int ukuran[]= Ukuran.ukuranView(this);

        pjgInduk= ukuran[0];
        lbrInduk= ukuran[1];
    }
    public void aturUkuranBar(int pjg, int lbr){
        this.pjgBar = pjg;
        this.lbrBar = lbr;
        RelativeLayout.LayoutParams lp= ambilLpBar();
        lp.width= pjg;
        lp.height= lbr;
        sesuaikanUkuranBar();
    }
    public void aturPanjang(int pjg){
        aturUkuranBar(pjg, lbrBar);
    }
    private void aturPanjangInternal(int pjg){
        this.pjgBar = pjg;
    }
    public int ambilPanjangInduk(){
        return viewBar.getWidth();
    }

    public void aturLebar(int lbr){
        aturUkuranBar(pjgBar, lbr);
    }
    private void aturLebarInternal(int lbr){
        this.lbrBar = lbr;
    }
    public int ambilLebarInduk(){
        return viewBar.getHeight();
    }

    public void aturLetakBar(float x, float y){
        aturLetakBarX(x);
        aturLetakBarY(y);
    }
    public void aturLetakBarX(float x){
        letakBarX = x;
        viewBar.setX(letakBarX);
//        viewBar.setTranslationX(letakBarX);
//        viewBar.setScrollX((int)-x);
//        Cek.toast(konteks, "" +viewBar.getX());
    }
    public float ambilLetakBarX(){
        return letakBarX;
    }
    public void aturLetakBarY(float y){
        letakBarY = y;
        viewBar.setY(letakBarY);
//        viewBar.setTranslationY(y);
    }
    public float ambilLetakBarY(){
        return letakBarY;
    }

/*
=========================
Warna
=========================
*/
    public void aturWarnaInduk(String warnaInduk){
        if(Warna.cekWarnaSah(warnaInduk)) {
            this.warnaInduk = warnaInduk;
            setColorFilter(Color.parseColor(warnaInduk));
        }
    }
    public void aturWarnaKuat(String warnaKuat){
        if(Warna.cekWarnaSah(warnaKuat))
            this.warnaKuat= warnaKuat;
    }
    public void aturWarnaLemah(String warnaLemah){
        if(Warna.cekWarnaSah(warnaLemah))
            this.warnaLemah= warnaLemah;
    }
    public void aturWarnaLatar(String warnaLatar){
        aturWarnaLatar(warnaLatar, pengaturanAwal);
    }
    public void aturWarnaLatar(String warnaLatar, boolean sesuaikanWarnaItem){
        if(Warna.cekWarnaSah(warnaLatar)) {
            this.warnaLatar = warnaLatar;
            sesuaikanWarnaLatar();
            if(sesuaikanWarnaItem) {
                aturWarnaTersedia(false);
                aturWarnaTakTersedia(false);
                sesuaikanKetersediaanItem();
                pengaturanAwal= false;
            }
        }
    }

    public void aturWarnaTersedia(){
        aturWarnaTersedia(true);
    } private void aturWarnaTersedia(boolean sesuaikan){
        warnaTersedia= Warna.ambilStringWarna(Warna.sesuaikanKegelapan(warnaLatar));
        if(sesuaikan)
            sesuaikanKetersediaanItem();
    } public void aturWarnaTersedia(String warnaTersedia){
        if(Warna.cekWarnaSah(warnaTersedia)) {
            this.warnaTersedia = warnaTersedia;
            sesuaikanKetersediaanItem();
        }
    }

    public void aturWarnaTakTersedia(){
        aturWarnaTakTersedia(true);
    } private void aturWarnaTakTersedia(boolean sesuaikan){
        int tingkatIntensitas;
        if(Warna.cekWarnaGelap(warnaLatar))
            tingkatIntensitas= -40;
        else
            tingkatIntensitas= 40;
        warnaTakTersedia= Warna.ambilStringWarna(Warna.aturIntensitasWarna(warnaLatar, tingkatIntensitas));
        if(sesuaikan)
            sesuaikanKetersediaanItem();
    } public void aturWarnaTakTersedia(String warnaTakTersedia){
        if(Warna.cekWarnaSah(warnaTakTersedia)) {
            this.warnaTakTersedia = warnaTakTersedia;
            sesuaikanKetersediaanItem();
        }
    }

    public void aturItemWarnaKuat(int posisiItem){
        if(daftarItem.ambil(posisiItem) != null)
            daftarItem.ambil(posisiItem).setColorFilter(Color.parseColor(warnaKuat));
    }
    public void aturItemWarnaLemah(int posisiItem){
        if(daftarItem.ambil(posisiItem) != null)
            daftarItem.ambil(posisiItem).setColorFilter(Color.parseColor(warnaLemah));
    }

    public void aturItemTersedia(int indek[]){
        if(indek.length == daftarItemTersedia.ukuran() && indek.length > 0) {
            daftarItemTersedia = new Array<>(indek);
            sesuaikanKetersediaanItem();
        }
    }
    public void aturItemTersedia(View v){
        for(int i= 0; i<daftarItem.ukuran(); i++)
            if(daftarItem.ambil(i) == v){
                aturItemTersedia(i);
                break;
            }
    }
    public void aturItemTersedia(int posisiItem){
        if(daftarItem.ambil(posisiItem) != null) {
            daftarItem.ambil(posisiItem).setColorFilter(Color.parseColor(warnaTersedia));
            daftarItemTersedia.ganti(posisiItem, new Integer(ITEM_TERSEDIA));
        }
    }

    public void aturItemTakTersedia(View v){
        for(int i= 0; i<daftarItem.ukuran(); i++)
            if(daftarItem.ambil(i) == v){
                aturItemTakTersedia(i);
                break;
            }
    }
    public void aturItemTakTersedia(int posisiItem){
        if(daftarItem.ambil(posisiItem) != null) {
            daftarItem.ambil(posisiItem).setColorFilter(Color.parseColor(warnaTakTersedia));
            daftarItemTersedia.ganti(posisiItem, new Integer(ITEM_TAK_TERSEDIA));
        }
    }
    public void aturItemHanyaTersedia(int posisiItem){
        for(int i= 0; i< jmlItem; i++)
            if(daftarItem.ambil(i) != null)
                aturItemTakTersedia(i);

        if(daftarItem.ambil(posisiItem) != null)
            aturItemTersedia(posisiItem);
    }

    private void sesuaikanKetersediaanItem(){
        for(int i= 0; i<jmlItem; i++) {
            if(daftarItemTersedia.ambil(i) == ITEM_TERSEDIA)
                aturItemTersedia(i);
            else if(daftarItemTersedia.ambil(i) == ITEM_TAK_TERSEDIA)
                aturItemTakTersedia(i);
        }
    }

    public void aturItemKuat(View v){
        for(int i= 0; i<daftarItem.ukuran(); i++)
            if(daftarItem.ambil(i) == v) {
                aturItemKuat(i);
                break;
            }
    }
    public void aturItemKuat(int posisi){
        daftarItem.ambil(posisi).setColorFilter(Color.parseColor(warnaKuat));
    }

    public void aturItemLemah(View v){
        for(int i= 0; i<daftarItem.ukuran(); i++)
            if(daftarItem.ambil(i) == v) {
                aturItemLemah(i);
                break;
            }
    }
    public void aturItemLemah(int posisi){
        daftarItem.ambil(posisi).setColorFilter(Color.parseColor(warnaLemah));
    }

    private void sesuaikanWarnaLatar(){
        if(viewBar != null){
            viewBar.getBackground().setTint(Color.parseColor(warnaLatar));
            viewBar.getBackground().setAlpha(255);
        }
        if(getBackground() != null){
            getBackground().setTint(Color.parseColor(warnaLatar));
            getBackground().setAlpha(255);
        }
    }


/*
=========================
ImageView Induk
=========================
*/
    public void tampilkanLatar(){
        getBackground().setAlpha(255);
        setColorFilter(Warna.sesuaikanKegelapan(warnaLatar));
    }
    public void sembunyikanLatar(){
        getBackground().setAlpha(0);
        setColorFilter(Color.parseColor(warnaInduk));
    }
    private void initInduk(){
        setBackgroundResource(R.drawable.latar_kotak_tumpul);
        getBackground().setTint(Color.parseColor(warnaLatar));
        getBackground().setAlpha(0);
    }

    public void aturGambarIndukDefault(int sumber){
        if(sumber != -1)
            sumberGambarIndukAwal= sumber;
    }
    public void aturGambarInduk(int sumber){
        if(sumber != -1){
            sumberGambarInduk= sumber;
            setImageResource(sumber);
        }
        else if(sumberGambarIndukAwal != -1)
            setImageResource(sumberGambarIndukAwal);
    }
    public void aturLatarInduk_Awal(int sumber){
        if(sumber != -1){
            sumberLatarInduk_Awal= sumber;
            latarIndukAwal();
        }
    }
    public void aturLatarInduk_Akhir(int sumber) {
        if(sumber != -1)
            sumberLatarInduk_Akhir = sumber;
    }
/*
            if(sumberLatarInduk_Akhir != LATAR_WARNA && sumberLatarInduk_Akhir != LATAR_KOTAK_TUMPUL
                    && sumberLatarInduk_Akhir != LATAR_KOSONG)
                setBackgroundResource(sumber);

        } else
            latarIndukAwal();
*/


    public int latarIndukAwal(){
        if(sumberLatarInduk_Awal == LATAR_WARNA)
            setBackgroundColor(Color.parseColor(warnaLatar));
        else if(sumberLatarInduk_Awal == LATAR_KOSONG)
            getBackground().setAlpha(0);
        else if(sumberLatarInduk_Awal != -1){
            setBackgroundResource(sumberLatarInduk_Awal);
            getBackground().setAlpha(255);
            getBackground().setTint(Color.parseColor(warnaLatar));
        }
        return sumberLatarInduk_Awal;
    }
    public int latarIndukAkhir(){
        if(sumberLatarInduk_Akhir == LATAR_WARNA)
            setBackgroundColor(Color.parseColor(warnaLatar));
        else if(sumberLatarInduk_Akhir == LATAR_KOSONG)
            getBackground().setAlpha(0);
        else if(sumberLatarInduk_Akhir != -1){
            setBackgroundResource(sumberLatarInduk_Akhir);
            getBackground().setAlpha(255);
            getBackground().setTint(Color.parseColor(warnaLatar));
        }
        return sumberLatarInduk_Akhir;
    }

/*
=========================
View Bar
=========================
*/
    public void aturElevasiBar(int elevasi){
        elevasiBar = elevasi;
        setElevation(elevasiBar);
    }
    private void inisiasiViewBar(){
        RelativeLayout.LayoutParams lpInduk= new RelativeLayout.LayoutParams(pjgBar, lbrBar);
        lpInduk.setMargins(Ukuran.DpKePx(10, resolusi), Ukuran.DpKePx(10, resolusi), Ukuran.DpKePx(10, resolusi), Ukuran.DpKePx(10, resolusi));
        viewBar = new RelativeLayout(konteks);
        viewBar.setLayoutParams(lpInduk);
        viewBar.setPadding(padding, padding, padding, padding);
        viewBar.setId(View.generateViewId());

        viewBar.setElevation(elevasiBar);
    }
    public void aturMarginBar(int kiri, int atas, int kanan, int bawah){
        RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) viewBar.getLayoutParams();
        lp.setMargins(kiri, atas, kanan, bawah);
    }
    public void aturPaddingBar(int kiri, int atas, int kanan, int bawah){
        viewBar.setPadding(kiri, atas, kanan, bawah);
    }

    public void aturLatarBar(int idLatar){
        if(idLatar != -1) {
            sumberLatarBar= idLatar;
            viewBar.setBackgroundResource(idLatar);
            viewBar.getBackground().setAlpha(255);
            viewBar.getBackground().setTint(Color.parseColor(warnaLatar));
        }
    }

    private void pasangItem(){
        for(int i= 0; i<jmlItem; i++)
            viewBar.addView(daftarItem.ambil(i));
    }
    private void lepasItem(int posisi){
        viewBar.removeView(daftarItem.ambil(posisi));
    }

    public RelativeLayout ambilViewBar(){
        return viewBar;
    }

    public void tampilkanBar(){
        ViewGroup vg= (ViewGroup) getRootView();
        vg.addView(viewBar, vg.indexOfChild(this) -1);
        menuDitampilkan = true;
    }
    public void hilangkanBar(){
        ((ViewGroup) getRootView()).removeView(viewBar);
        menuDitampilkan = false;
    }

    public void aturLpBar(RelativeLayout.LayoutParams lp){
        viewBar.setLayoutParams(lp);
    }
    public RelativeLayout.LayoutParams ambilLpBar(){
        return (RelativeLayout.LayoutParams) viewBar.getLayoutParams();
    }

/*
=========================
Item
=========================
*/
    private void aturJmlItem(int jml){
        int jmlItemAwal= jmlItem;
        jmlItem= jml;
        if(jml > jmlItemAwal){
            perbaruiItem(jmlItemAwal);
            perbaruiItemTersedia(jmlItemAwal);
        }
        else if(jml < jmlItemAwal){
//            int jmlJalan= jmlItemAwal;
            while(jmlItemAwal > jml){
                daftarItem.hapus(--jmlItemAwal);
                daftarItemTersedia.hapus(jmlItemAwal);
                lepasItem(jmlItemAwal);
            }
        }
    }
    public int ambilJmlItem(){
        return jmlItem;
    }

    public void aturJarakAntarItem(int jarak){
        jarakAntarItem= jarak;
        RelativeLayout.LayoutParams lp;
        int kiri= 0, atas= 0;
        for(int i= 1; i< jmlItem; i++){
            lp= (RelativeLayout.LayoutParams) daftarItem.ambil(i).getLayoutParams();
            if(arahBar == ARAH_HORIZONTAL)
                kiri= jarakAntarItem;
            else if(arahBar == ARAH_VERTIKAL)
                atas= jarakAntarItem;
            lp.setMargins(kiri, atas, 0, 0);
            daftarItem.ambil(i).setLayoutParams(lp);
        }
    }
    public int ambilJarakAntarItem(){
        return jarakAntarItem;
    }

    public void aturUkuranItem(int ukuran){
        ukuranItem= ukuran;
        RelativeLayout.LayoutParams lp;
        for(int i= 0; i< jmlItem; i++){
            lp= (RelativeLayout.LayoutParams) daftarItem.ambil(i).getLayoutParams();
            lp.width= Ukuran.DpKePx(ukuranItem, resolusi);
            lp.height= Ukuran.DpKePx(ukuranItem, resolusi);
//            daftarItem.ambil(i).setLayoutParams(lp);
        }
    }
    public int ambilUkuranItem(){
        return ukuranItem;
    }

/*
    private void inisiasiDaftarItem(){
//        daftarItem= new ImgViewTouch[jmlItem];
        daftarItemTersedia= new int[jmlItem];
    }
*/
    private void perbaruiItem(int mulai){
        for(int i= mulai; i< jmlItem; i++)
            inisiasiItem(i);
    }
    private ImgViewTouch inisiasiItem(int posisiItem){
        RelativeLayout.LayoutParams lpItem= new RelativeLayout.LayoutParams(Ukuran.DpKePx(ukuranItem, resolusi), Ukuran.DpKePx(ukuranItem, resolusi));
        if(arahBar == ARAH_HORIZONTAL)
            lpItem.addRule(RelativeLayout.CENTER_VERTICAL);
        else if(arahBar == ARAH_VERTIKAL)
            lpItem.addRule(RelativeLayout.CENTER_HORIZONTAL);

        if(posisiItem > 0){
            if(arahBar == ARAH_HORIZONTAL) {
                lpItem.addRule(RelativeLayout.RIGHT_OF, daftarItem.ambil(posisiItem-1).getId());
                lpItem.setMargins(jarakAntarItem, 0, 0 ,0);
            }
            else if(arahBar == ARAH_VERTIKAL) {
                lpItem.addRule(RelativeLayout.BELOW, daftarItem.ambil(posisiItem-1).getId());
                lpItem.setMargins(0, jarakAntarItem, 0, 0);
            }
        }
        ImgViewTouch itemBaru= new ImgViewTouch(konteks);
        daftarItem.tambah(itemBaru, posisiItem);
        itemBaru.setPadding(Ukuran.DpKePx(4, resolusi), Ukuran.DpKePx(4, resolusi), Ukuran.DpKePx(4, resolusi), Ukuran.DpKePx(4, resolusi));
        itemBaru.setId(View.generateViewId());
        itemBaru.setLayoutParams(lpItem);
        itemBaru.setColorFilter(Color.parseColor(warnaLemah));
        itemBaru.aturPenungguKlik_Internal(new PenungguKlik_Internal() {
            @Override
            public void klik_Int(View v) {
                if(!v.isSelected())
                    v.setSelected(true);
                else
                    v.setSelected(false);
            }
        });
        return itemBaru;
    }
    private void perbaruiItemTersedia(int mulai){
        for(int i= mulai; i< jmlItem; i++)
            inisiasiItemTersedia(i);
    }
    private void inisiasiItemTersedia(int posisiItem){
        daftarItemTersedia.tambah(new Integer(ITEM_TAK_TERSEDIA), posisiItem);
    }

/*
    private int cariIdItemSebelum(int posisiItem){
        if(posisiItem > 0)
            return daftarItem.ambil(posisiItem-1).getId();
        return -1;
    }
    private int cariIdItemSelanjut(int posisiItem){
        if(posisiItem < jmlItem-1)
            return daftarItem.ambil(posisiItem+1).getId();
        return -1;
    }
*/

    public void aturGmbItem(int idGmb[]){
        aturJmlItem(idGmb.length);
        for(int i= 0; i<jmlItem; i++)
            aturGmbItem(i, idGmb[i]);
        pasangItem();
    }
    public void aturGmbItem(int posisiItem, int idGmb){
        ImgViewTouch itemBaru= daftarItem.ambil(posisiItem);
        itemBaru.setImageResource(idGmb);
    }

    public void tambahGmbItem(int idGmb){
        if(idGmb < 10)
            return;
        ImgViewTouch itemBaru= inisiasiItem(daftarItem.ukuran());
        itemBaru.setImageResource(idGmb);
        daftarItem.tambah(itemBaru);
        daftarItemTersedia.tambah(ITEM_TAK_TERSEDIA);
        jmlItem++;
    }

    public void aturAksiSentuhItemDefault(){
        for (int i= 0; i<daftarItem.ukuran(); i++){
            if(daftarItemTersedia.ambil(i) == ITEM_TERSEDIA)
                aturAksiSentuhItem(i, new PenungguSentuhan() {
                    @Override
                    public void sentuh(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN)
                            aturItemKuat(v);
                        if(event.getAction() == MotionEvent.ACTION_UP)
                            aturItemTersedia(v);
                    }
                });
        }
        aksiSentuhItemDefault= true;
    }

    public boolean itemDipilihKah(int indek){
        return daftarItem.ambil(indek).isSelected();
    }
    public ImageView itemKe(int indek){
        return daftarItem.ambil(indek);
    }
}
