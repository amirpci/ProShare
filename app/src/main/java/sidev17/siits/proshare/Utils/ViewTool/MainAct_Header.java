package sidev17.siits.proshare.Utils.ViewTool;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sidev17.siits.proshare.Modul.Worker.Tab.ChatExprt;
import sidev17.siits.proshare.Modul.Worker.Tab.ProfileActWkr;
import sidev17.siits.proshare.Modul.Worker.Tab.ShareActWkr;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ArrayBernama;
import sidev17.siits.proshare.Utils.ViewTool.Aktifitas;
import sidev17.siits.proshare.ViewPagerAdapter;

public class MainAct_Header extends Aktifitas {
    public final int TOMBOL_UTAMA_HILANG= 0;

    protected ViewPager mvPager;
    protected ViewPager.OnPageChangeListener pngGantiHalaman;

    protected Array<Fragment_Header> fragmenHalaman= new Array<>(); //= {new ProfileActWkr(), new ShareActWkr(), new ChatExprt()};
    protected Array<String> judulHalaman= new Array<>(); //= {"Profil", "Pustaka", "Chat"};
    protected ViewPagerAdapter adapter;

    private ImageView tombolUtama;
    private Array<Integer> iconTombolUtama= new Array<>(); //= {TOMBOL_UTAMA_HILANG, R.drawable.obj_pensil, R.drawable.obj_chat};

    private TextView judulHeader;
    private TextView tambahanJudulHeader;
    private ImageView opsiHeader[]= new ImageView[3];
    private String warnaOpsiHeader[]= new String[3];


    public boolean bolehInitHeader= true;


    @Override
    public void onAttachFragment(Fragment fragment) {
        fragmenHalaman.tambah((Fragment_Header)fragment);
        judulHalaman.tambah(((Fragment_Header)fragment).judulHeader());
        super.onAttachFragment(fragment);
    }


    private void initFragmenHolder(){
        fragmenHalaman.aturPenungguTraverse(new Array.PenungguTraverse<Void, Void, Void>() {
            @Override
            public Void traverse(Array array, int indek, Object isiArray) {
                adapter.addFragment((Fragment_Header) isiArray, judulHalaman.ambil(indek));
                return null;
            }
        });
    }

    protected void aturFragmen(Fragment_Header... fragmen){
        if(fragmenHalaman.ukuran() > 0)
            return;
        fragmenHalaman.tambah(fragmen);
    }
    protected void aturJudul(String... judul){
        if(judulHalaman.ukuran() > 0)
            return;
        judulHalaman.tambah(judul);
    }
/*
    private void initFragmen(){
        initFragmenHolder();
        if(fragmenHalaman.ukuran() == 0) {
            Toast.makeText(this, "INIT ISI!!!", Toast.LENGTH_SHORT).show();
            fragmenHalaman.tambah(new Fragment_Header[]{new ProfileActWkr(), new ShareActWkr(), new ChatExprt()});
            judulHalaman.tambah(new String[]{"Profil", "Pustaka", "Chat"});
        }
    }
*/
    protected void initAdapter(){
        if(mvPager == null)
            return;
        initFragmenHolder();
        bolehInitHeader= false;
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmenHalaman.traverse();
        mvPager.setAdapter(adapter);
        mvPager.clearOnPageChangeListeners();
/*
        mvPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                gantiWarnaTab(position, tmbTab[0], tmbTab[1], warnaTab);
                gantiIconTombolUtama();

/*
                if(fragmenHalaman.ambil(position).ambilAktifitas() != null)
                    Toast.makeText(MainActivityWkr.this, "kelasnya= " +fragmenHalaman.ambil(position).ambilAktifitas().getClass().getName(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivityWkr.this, "null= " +(fragmenHalaman.ambil(position).ambilAktifitas() == null), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivityWkr.this, "boleh= " +bolehInitHeader, Toast.LENGTH_SHORT).show();
* /
                if(bolehInitHeader)
                    fragmenHalaman.ambil(position).initHeader();
                aturJudulHeader(judulHalaman.ambil(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/
        mvPager.setOffscreenPageLimit(4);
    }
    private void lepaskanFragment(){
        for(int i= 0; i< fragmenHalaman.ukuran(); i++)
            fragmenHalaman.ambil(i).getFragmentManager().beginTransaction().remove(fragmenHalaman.ambil(i)).commit();
    }


    /*
===============================
Khusus untuk kustomasi header
===============================
*/
    //dipanggil setelah onCreate() dan setContentView()
    public void initHeader(){
        pngGantiHalaman= new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        };
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
//    public static final String PREFIX_KONTEKS_HALAMAN= "KE-";
    private ArrayBernama<Array<AksiBackPress>> aksiBackPress_Halaman= new ArrayBernama<>(false);

    @Override
    public void daftarkanAksiBackPress(AksiBackPress a) {
        throw new UnsupportedOperationException("Gunakan daftarkanAksiBackPress(AksiBackPress a, int ke) sebagai gantinya");
    }
    public void daftarkanAksiBackPress(AksiBackPress a, Fragment_Header fragmen) {
        int indek= fragmenHalaman.indekAwal(fragmen);
        if(indek == -1)
            throw new RuntimeException("daftarkanAksiBackPress(AksiBackPress a, Fragment_Header fragmen) harus diakses dari Fragment_Header bersangkutan!");
        if(aksiBackPress_Halaman.isiNull(indek))
            aksiBackPress_Halaman.tambah(indek,
                    new Array<AksiBackPress>().aturPenungguTraverse(buat_penungguBackPress()));
        aksiBackPress_Halaman.ambil(indek).tambah(a);
    }

    @Override
    public void onBackPressed() {
        boolean keAnak= false;
        if(mvPager != null){
            int indek= mvPager.getCurrentItem();
            if(!aksiBackPress_Halaman.isiNull(indek))
                keAnak |= (Boolean) aksiBackPress_Halaman.ambil(indek).traverse();
        }
        if(!keAnak)
            backPress_int();
    }


    protected final void tombolUtama(ImageView tmb){
        tombolUtama= tmb;
        tombolUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mvPager != null){
                    int indek= mvPager.getCurrentItem();
                    if(!aksiTombolUtama.isiNull(indek))
                        aksiTombolUtama.ambil(indek).tekan(tombolUtama, indek);
                }
            }
        });
    }
    public void tampilkanTombolUtama(boolean terlihat){
        int visibility= terlihat ? View.VISIBLE : View.GONE;
        tombolUtama.setVisibility(visibility);
    }

    public interface AksiTombolUtama{
        void tekan(View tombolUtama, int halaman);
    }
    public interface PenampilTombolUtama{
        boolean tampilkan(View tombolUtama);
    }
    private Array<AksiTombolUtama> aksiTombolUtama= new Array<>(true);
    private Array<PenampilTombolUtama> penampilTombolUtama= new Array<>(true);

    protected void daftarkanAksiTombolUtama(AksiTombolUtama a, Fragment_Header fragmen){
        int indek= fragmenHalaman.indekAwal(fragmen);
        if(indek == -1)
            throw new RuntimeException("daftarkanAksiTombolUtama(AksiTombolUtama a, Fragment_Header fragmen) harus diakses dari Fragment_Header bersangkutan!");
        if(!aksiTombolUtama.isiNull(indek))
            aksiTombolUtama.tambah(a, indek);
        else
            aksiTombolUtama.ganti(indek, a);
    }

    public void aturIconTombolUtama(int... icon){
        if(iconTombolUtama.ukuran() > 0)
            iconTombolUtama.hapusSemua();
        iconTombolUtama.dariArrayPrimitif(icon);
        if(iconTombolUtama.ukuran() < fragmenHalaman.ukuran())
            for(int i= iconTombolUtama.ukuran(); i< fragmenHalaman.ukuran(); i++)
                iconTombolUtama.tambah(TOMBOL_UTAMA_HILANG);
    }
    public void gantiIconTombolUtama(){
        int icon= iconTombolUtama.ambil(mvPager.getCurrentItem());
        boolean tampil= true;
        int indek= mvPager.getCurrentItem();
        if(!penampilTombolUtama.isiNull(indek))
            tampil &= penampilTombolUtama.ambil(indek).tampilkan(tombolUtama);
        tampil &= (icon != TOMBOL_UTAMA_HILANG);

        int visibility= (tampil) ? View.VISIBLE : View.GONE;
        tombolUtama.setVisibility(visibility);

        if(tampil) tombolUtama.setImageResource(icon);
    }

    protected void daftarkanPenampilTombolUtama(PenampilTombolUtama p, Fragment_Header fragmen){
        int indek= fragmenHalaman.indekAwal(fragmen);
        if(indek == -1)
            throw new RuntimeException("daftarkanPenampilTombolUtama(AksiTombolUtama a, Fragment_Header fragmen) harus diakses dari Fragment_Header bersangkutan!");
        if(!penampilTombolUtama.isiNull(indek))
            penampilTombolUtama.tambah(p, indek);
        else
            penampilTombolUtama.ganti(indek, p);
    }


    public interface AksiGantiHalaman{
        void gantiHalaman(int posisi);
    }
    private Array<AksiGantiHalaman> aksiGantiHalaman;//= new Array<>(true);

    public void daftarkanAksiGantiHalaman(AksiGantiHalaman a){
        if(aksiGantiHalaman== null){
            aksiGantiHalaman= new Array<>();
            aksiGantiHalaman.aturPenungguTraverse(new Array.PenungguTraverse<Integer, Void, Void>() {
                int posisiHalaman= 0;
                @Override
                public void awalTraverse(Integer... masukan) {
                    posisiHalaman= masukan[0];
                }

                @Override
                public Void traverse(Array array, int indek, Object isiArray) {
                    if(array.isiNull(indek))
                        return null;
                    ((AksiGantiHalaman)isiArray).gantiHalaman(posisiHalaman);
                        return null;
                }
            });
        }
        aksiGantiHalaman.tambah(a);
    }
    protected void aksiGantiHalaman(int posisi){
        if(aksiGantiHalaman != null)
            aksiGantiHalaman.traverse(posisi);
    }
}
