package sidev17.siits.proshare.Model.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Ukuran;
import sidev17.siits.proshare.Utils.Warna;


public class MenuBarView extends ImgViewTouch {
    public static final int ARAH_HORIZONTAL= 11;
    public static final int ARAH_VERTICAL= 12;

    public static final int BAR_DI_ATAS= 511;
    public static final int BAR_DI_BAWAH= 512;
    public static final int BAR_DI_KANAN= 513;
    public static final int BAR_DI_KIRI= 514;
    public static final int BAR_RATA_KANAN= 521;
    public static final int BAR_RATA_KIRI= 522;
    public static final int BAR_RATA_TENGAH= 523;
    public static final int BAR_RATA_ATAS= 524;
    public static final int BAR_RATA_BAWAH= 525;

    public static final int WARNA_TERSEDIA= 21;
    public static final int WARNA_TAK_TERSEDIA= 22;
    public static final int WARNA_KUAT= 23;
    public static final int WARNA_LEMAH= 24;

    public static final int ITEM_TERSEDIA= 31;
    public static final int ITEM_TAK_TERSEDIA= 32;

    private Context konteks;
    private DisplayMetrics resolusi;
    private int arahBar;
    private int letakBar;
    private int rataanBar;
    private int pjg;
    private int lbr;
    private float letakX= -1;
    private float letakY= -1;
    private boolean menuDitampilkan = false;
    private boolean aksiSentuhItemDefault= false;
    private int padding;
    private int elevasiBar;

    private RelativeLayout viewBar;
    private ImgViewTouch daftarItem[];
    private int daftarItemTersedia[];
    private int jmlItem= 0;
    private int ukuranItem;
    private int jarakAntarItem= 0;

    private int sumberLatarInduk_Awal = -1;
    private int sumberLatarInduk_Akhir = -1;
    private int sumberGambarInduk= -1;
    private int sumberGambarIndukAwal= -1;
    private int sumberLatarBar= -1;

    private String warnaKuat;
    private String warnaLemah;
    private String warnaTakTersedia;
    private String warnaTersedia;
    private String warnaLatar;
    private String warnaInduk;

    private PenungguKlik_BarView pngKlikBar;


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

        aturLetakRelatif(BAR_DI_KANAN);
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
        isiViewBar();
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
        isiViewBar();

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

        aturLetakRelatif(BAR_DI_KANAN);
        setElevation(elevasiBar);

        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuBarView);
            aturWarnaKuat(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_kuat, Color.parseColor(warnaKuat))));
            aturWarnaLemah(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_lemah, Color.parseColor(warnaLemah))));
            aturWarnaInduk(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_induk, Color.parseColor(warnaKuat))));
            aturWarnaLatar(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_latar, Color.parseColor(warnaKuat))));
            aturWarnaTersedia(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_tersedia, Color.parseColor(warnaTersedia))));
            aturWarnaTakTersedia(Warna.ambilStringWarna(a.getColor(R.styleable.MenuBarView_warna_tak_tersedia, Color.parseColor(warnaTakTersedia))));

            aturLatarInduk_Akhir(a.getResourceId(R.styleable.MenuBarView_latarInduk, -1));
            aturLatarInduk_Awal(a.getResourceId(R.styleable.MenuBarView_latarInduk_AWAL, -1));
            aturGambarInduk(a.getResourceId(R.styleable.MenuBarView_gambarInduk, -1));
            aturGambarInduk(a.getResourceId(R.styleable.MenuBarView_gambarInduk_AWAL, -1));

            aturLatarBar(a.getResourceId(R.styleable.MenuBarView_BAR_latarInduk, -1));
        }
        penungguInternal();
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
            public void klik(View v) {
                if(pngKlikBar != null)
                    pngKlikBar.klik(superView, menuDitampilkan);
                klikInduk();
            }
        });
    }
    public final boolean klik(){
        return super.performClick();
    }
    private final void klikInduk(){
        if(jmlItem > 0 && !menuDitampilkan) {
            if(letakX == -1 || letakY == -1)
                sesuaikanLetakRelatif(letakBar);
            if(!aksiSentuhItemDefault)
                aturAksiSentuhItemDefault();
            tampilkanBar();
            if(sumberLatarInduk_Awal == -1)
                tampilkanLatar();
            Log.d("DIKLIK!!!", "Ditampilkan!!!!");
            Toast.makeText(konteks, "DITAMPILKAN!!!", Toast.LENGTH_LONG).show();
            Toast.makeText(konteks, letakBar +" " +Float.toString(letakX) +" " +Float.toString(letakY), Toast.LENGTH_LONG).show();
        }
        else if(menuDitampilkan) {
            hilangkanBar();
            if(sumberLatarInduk_Awal == -1)
                sembunyikanLatar();
            Log.d("DIKLIK!!!", "Tidak ditampilkan!!!!");
            Toast.makeText(konteks, "TIDAK DITAMPILKAN!!!", Toast.LENGTH_LONG).show();
        }
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
        if(daftarItem[posisiItem] != null && posisiItem < jmlItem)
            daftarItem[posisiItem].aturPenungguSentuhan(png);
    }
    public void aturAksiKlikItem(int posisiItem, PenungguKlik png){
        if(daftarItem[posisiItem] != null && posisiItem < jmlItem)
            daftarItem[posisiItem].aturPenungguKlik(png);
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
Arah - Ukuran - Letak Latar
=========================
*/
    public void aturArahBar(int kategoriArah){
        arahBar= kategoriArah;
        inisiasiItem();
    }

    public void aturLetakRelatif(int letak){
        Point layar= new Point();
        ((Activity) konteks).getWindowManager().getDefaultDisplay().getSize(layar);
        int pjgLayar= layar.x;
        int lbrLayar= layar.y;

        //measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layout(getLeft(), getTop(), getRight(), getBottom());

        switch(letak) {
            case BAR_DI_BAWAH:
                if (getY() +getHeight() +viewBar.getHeight()> lbrLayar)
                    letak = BAR_DI_ATAS;
                aturArahBar(ARAH_VERTICAL);
                break;
            case BAR_DI_KANAN:
                if (getX() +getWidth() +viewBar.getWidth()> pjgLayar)
                    letak = BAR_DI_KIRI;
                aturArahBar(ARAH_HORIZONTAL);
                break;
            case BAR_DI_ATAS:
                if(getY() -viewBar.getHeight() < 0)
                    letak= BAR_DI_BAWAH;
                aturArahBar(ARAH_VERTICAL);
                break;
            case BAR_DI_KIRI:
                if(getX() -viewBar.getWidth() < 0)
                    letak= BAR_DI_KANAN;
                aturArahBar(ARAH_HORIZONTAL);
                break;
        }
        letakBar= letak;
    }
    private void sesuaikanLetakRelatif(int letak){
        int letakAbs[]= new int[2];
        getLocationOnScreen(letakAbs);

        float x= letakAbs[0], y= letakAbs[1];
        switch(letak){
            case BAR_DI_BAWAH:
                y+= getHeight();
                break;
            case BAR_DI_KANAN:
                x+= getWidth();
                break;
            case BAR_DI_ATAS:
                y-= viewBar.getHeight();
                break;
            case BAR_DI_KIRI:
                x-= viewBar.getWidth();
                break;
        }
        aturLetakPilihan(x, y);
    }

    public void aturRataanRelatif(int rataan){
        rataanBar= rataan;
    }
    private void cekRataan(){
        Point layar= new Point();
        ((Activity) konteks).getWindowManager().getDefaultDisplay().getSize(layar);
        int pjgLayar= layar.x;
        int lbrLayar= layar.y;
    }

    public void aturUkuran(int pjg, int lbr){
        this.pjg= pjg;
        this.lbr= lbr;
        RelativeLayout.LayoutParams lp= ambilLpBar();
        lp.width= pjg;
        lp.height= lbr;
    }
    public void aturPanjang(int pjg){
        aturUkuran(pjg, lbr);
    }
    private void aturPanjangInternal(int pjg){
        this.pjg= pjg;
    }
    public int ambilPanjangInduk(){
        return viewBar.getWidth();
    }

    public void aturLebar(int lbr){
        aturUkuran(pjg, lbr);
    }
    private void aturLebarInternal(int lbr){
        this.lbr= lbr;
    }
    public int ambilLebarInduk(){
        return viewBar.getHeight();
    }

    public void aturLetakPilihan(float x, float y){
        aturLetakX(x);
        aturLetakY(y);
    }
    public void aturLetakX(float x){
        letakX= x;
        viewBar.setX(letakX);
    }
    public float ambilLetakX(){
        return letakX;
    }
    public void aturLetakY(float y){
        letakY= y;
        viewBar.setY(letakY);
    }
    public float ambilLetakY(){
        return letakY;
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
        if(Warna.cekWarnaSah(warnaLatar)) {
            this.warnaLatar = warnaLatar;
            sesuaikanWarnaLatar();
        }
    }
    public void aturWarnaLatar(String warnaLatar, boolean sesuaikan){
        if(Warna.cekWarnaSah(warnaLatar)) {
            this.warnaLatar = warnaLatar;
            sesuaikanWarnaLatar();
            if(sesuaikan) {
                aturWarnaTersedia(false);
                aturWarnaTakTersedia(false);
                sesuaikanKetersediaanItem();
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
        if(daftarItem[posisiItem] != null)
            daftarItem[posisiItem].setColorFilter(Color.parseColor(warnaKuat));
    }
    public void aturItemWarnaLemah(int posisiItem){
        if(daftarItem[posisiItem] != null)
            daftarItem[posisiItem].setColorFilter(Color.parseColor(warnaLemah));
    }

    public void aturItemTersedia(int indek[]){
        if(indek.length == daftarItemTersedia.length && indek.length > 0) {
            daftarItemTersedia = indek;
            sesuaikanKetersediaanItem();
        }
    }
    public void aturItemTersedia(View v){
        for(int i= 0; i<daftarItem.length; i++)
            if(daftarItem[i] == v){
                aturItemTersedia(i);
                break;
            }
    }
    public void aturItemTersedia(int posisiItem){
        if(daftarItem[posisiItem] != null) {
            daftarItem[posisiItem].setColorFilter(Color.parseColor(warnaTersedia));
            daftarItemTersedia[posisiItem]= ITEM_TERSEDIA;
        }
    }

    public void aturItemTakTersedia(View v){
        for(int i= 0; i<daftarItem.length; i++)
            if(daftarItem[i] == v){
                aturItemTakTersedia(i);
                break;
            }
    }
    public void aturItemTakTersedia(int posisiItem){
        if(daftarItem[posisiItem] != null) {
            daftarItem[posisiItem].setColorFilter(Color.parseColor(warnaTakTersedia));
            daftarItemTersedia[posisiItem]= ITEM_TAK_TERSEDIA;
        }
    }
    public void aturItemHanyaTersedia(int posisiItem){
        for(int i= 0; i< jmlItem; i++)
            if(daftarItem[i] != null)
                aturItemTakTersedia(i);

        if(daftarItem[posisiItem] != null)
            aturItemTersedia(posisiItem);
    }

    private void sesuaikanKetersediaanItem(){
        for(int i= 0; i<jmlItem; i++) {
            if(daftarItemTersedia[i] == ITEM_TERSEDIA)
                aturItemTersedia(i);
            else if(daftarItemTersedia[i] == ITEM_TAK_TERSEDIA)
                aturItemTakTersedia(i);
        }
    }

    public void aturItemKuat(View v){
        for(int i= 0; i<daftarItem.length; i++)
            if(daftarItem[i] == v) {
                aturItemKuat(i);
                break;
            }
    }
    public void aturItemKuat(int posisi){
        daftarItem[posisi].setColorFilter(Color.parseColor(warnaKuat));
    }

    public void aturItemLemah(View v){
        for(int i= 0; i<daftarItem.length; i++)
            if(daftarItem[i] == v) {
                aturItemLemah(i);
                break;
            }
    }
    public void aturItemLemah(int posisi){
        daftarItem[posisi].setColorFilter(Color.parseColor(warnaLemah));
    }

    private void sesuaikanWarnaLatar(){
        if(viewBar != null)
            viewBar.getBackground().setTint(Color.parseColor(warnaLatar));
        if(getBackground() != null)
            getBackground().setTint(Color.parseColor(warnaLatar));
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
        Drawable d= getBackground();
        d.setTint(Color.parseColor(warnaLatar));
        d.setAlpha(0);
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
        if(sumber != -1)
            sumberLatarInduk_Awal = sumber;
    }
    public void aturLatarInduk_Akhir(int sumber){
        if(sumber != -1){
            sumberLatarInduk_Akhir = sumber;
            setBackgroundResource(sumber);
        } else
            latarIndukAwal();
    }

    public int latarIndukAwal(){
        if(sumberLatarInduk_Awal != -1)
            setBackgroundResource(sumberLatarInduk_Awal);
        return sumberLatarInduk_Awal;
    }
    public int latarIndukAkhir(){
        if(sumberLatarInduk_Akhir != -1)
            setBackgroundResource(sumberLatarInduk_Akhir);
        return sumberLatarInduk_Akhir;
    }

/*
=========================
View Bar
=========================
*/
    public void aturElevasiBar(int elevasi){
        elevasiBar = elevasi;
    }
    private void inisiasiViewBar(){
        RelativeLayout.LayoutParams lpInduk= new RelativeLayout.LayoutParams(pjg, lbr);
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
            viewBar.getBackground().setTint(Color.parseColor(warnaLatar));
        }
    }

    private void isiViewBar(){
        for(int i= 0; i<jmlItem; i++)
            if(daftarItem[i] != null)
                viewBar.addView(daftarItem[i]);
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
    public void aturJmlItem(int jml){
        jmlItem= jml;
        inisiasiDaftarItem();
    }
    public int ambilJmlItem(){
        return jmlItem;
    }

    public void aturJarakAntarItem(int jarak){
        jarakAntarItem= jarak;
        if(daftarItem != null)
            inisiasiItem();
    }
    public int ambilJarakAntarItem(){
        return jarakAntarItem;
    }

    public void aturUkuranItem(int ukuran){
        ukuranItem= ukuran;
        if(daftarItem != null)
            inisiasiItem();
    }
    public int ambilUkuranItem(){
        return ukuranItem;
    }

    private void inisiasiDaftarItem(){
        daftarItem= new ImgViewTouch[jmlItem];
        daftarItemTersedia= new int[jmlItem];
    }
    private void inisiasiItem(){
        for(int i= 0; i< jmlItem; i++)
            inisiasiItem(i);
    }
    private void inisiasiItem(int posisiItem){
        RelativeLayout.LayoutParams lpItem= new RelativeLayout.LayoutParams(Ukuran.DpKePx(ukuranItem, resolusi), Ukuran.DpKePx(ukuranItem, resolusi));
        if(arahBar == ARAH_HORIZONTAL)
            lpItem.addRule(RelativeLayout.CENTER_VERTICAL);
        else if(arahBar == ARAH_VERTICAL)
            lpItem.addRule(RelativeLayout.CENTER_HORIZONTAL);

        if(posisiItem > 0){
            if(daftarItem[posisiItem-1] != null) {
                if(arahBar == ARAH_HORIZONTAL) {
                    lpItem.addRule(RelativeLayout.RIGHT_OF, cariIdItemSebelum(posisiItem));
                    lpItem.setMargins(jarakAntarItem, 0, 0 ,0);
                }
                else if(arahBar == ARAH_VERTICAL) {
                    lpItem.addRule(RelativeLayout.BELOW, cariIdItemSebelum(posisiItem));
                    lpItem.setMargins(0, jarakAntarItem, 0, 0);
                }
            }
        }
        if(daftarItem[posisiItem] == null) {
            daftarItem[posisiItem] = new ImgViewTouch(konteks);
            daftarItem[posisiItem].setPadding(Ukuran.DpKePx(4, resolusi), Ukuran.DpKePx(4, resolusi), Ukuran.DpKePx(4, resolusi), Ukuran.DpKePx(4, resolusi));
            daftarItem[posisiItem].setId(View.generateViewId());
        }
        daftarItem[posisiItem].setLayoutParams(lpItem);
    }

    private int cariIdItemSebelum(int posisiItem){
        for(int i= posisiItem -1; i>= 0; i--)
            if(daftarItem[i] != null)
                return daftarItem[i].getId();
        return 0;
    }
    private int cariIdItemSelanjut(int posisiItem){
        for(int i= posisiItem +1; i< jmlItem; i++)
            if(daftarItem[i] != null)
                return daftarItem[i].getId();
        return 0;
    }

    public void aturGmbItem(int idGmb[]){
        aturJmlItem(idGmb.length);
        for(int i= 0; i<jmlItem; i++)
            aturGmbItem(i, idGmb[i]);
        isiViewBar();
    }
    public void aturGmbItem(int posisiItem, int idGmb){
        if(daftarItem[posisiItem] == null)
            inisiasiItem(posisiItem);

        daftarItem[posisiItem].setImageResource(idGmb);
        daftarItem[posisiItem].setColorFilter(Color.parseColor(warnaLemah));
    }

    public void aturAksiSentuhItemDefault(){
        for (int i= 0; i<daftarItem.length; i++){
            if(daftarItemTersedia[i] == ITEM_TERSEDIA)
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
}
