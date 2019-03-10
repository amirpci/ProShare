package sidev17.siits.proshare.Modul.Worker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ViewTool.Aktifitas_Slider;
import sidev17.siits.proshare.Utils.ViewTool.Fragment_Header;
import sidev17.siits.proshare.Utils.ViewTool.MainAct_Header;
import sidev17.siits.proshare.Modul.Worker.Tab.ChatExprt;
import sidev17.siits.proshare.Modul.Worker.Tab.ProfileActWkr;
import sidev17.siits.proshare.Modul.Worker.Tab.ShareActWkr;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.ViewPagerAdapter;

public class MainActivityWkr extends MainAct_Header implements Aktifitas_Slider {

    private RelativeLayout tmb_Profile;
    private ImageView garis_Profile;

    private RelativeLayout tmb_Tanya;
    private ImageView garis_Tanya;

    private RelativeLayout tmb_Jawab, tmb_Chat;
    private ImageView garis_Jawab;

    private boolean click_duaKali=false;

    private int halamanFragmen= 0;
    private PenungguGantiHalaman pngGantiHalaman;

    private final int tmbTab[][] = {{R.id.tab_profile_ikon_wkr, R.id.tab_tanya_ikon_wkr/*, R.id.tab_tl_ikon_wkr*/, R.id.tab_feedback_ikon_Exprt},
            {R.id.tab_profile_garis_wkr, R.id.tab_tanya_garis_wkr, /*R.id.tab_tl_garis_wkr,*/ R.id.tab_feedback_garis_Exprt}};

    private final int warnaTab[][] = {{R.color.colorAccent, R.color.colorPrimaryDark},
            {R.color.colorPrimary, R.color.colorPrimaryDark}};

//    private ImageView tmbUtama;

//    private boolean bolehInitHeader= false; //false diperoleh hanya sekali saat pertama kali di-init

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wkr);

        bolehInitHeader= false;

        tombolUtama((ImageView)findViewById(R.id.tombol_utama));

        final ImageView icon_Profile = (ImageView) findViewById(R.id.tab_profile_ikon_wkr);
        garis_Profile = (ImageView) findViewById(R.id.tab_profile_garis_wkr);
        tmb_Profile = findViewById(R.id.tab_profile_wkr);
        tmb_Tanya = findViewById(R.id.tab_tanya_wkr);
//        tmb_Jawab = findViewById(R.id.tab_tl_wkr);
        tmb_Chat = findViewById(R.id.tab_feedback_wkr);

/*
        adapter.addFragment(new ProfileActWkr(), "");
//        adapter.addFragment(new DaftarTanyaActWkr(), "");
        adapter.addFragment(new ShareActWkr(), "");
        adapter.addFragment(new ChatExprt(), "");
*/


        //Buat Warna Inisiasi
       saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_profile_ikon_wkr), warnaTab[0],
                tmbTab[1], (ImageView) findViewById(R.id.tab_profile_garis_wkr), warnaTab[1]);

//        garis_Profile= (ImageView) findViewById(R.id.tab_profile_garis_wkr);
        tmb_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tampilkan(new ProfileActWkr(), 0);
                gantiWarnaTab(0, tmbTab[0], tmbTab[1], warnaTab);
            }
        });

//        garis_Tanya= (ImageView) findViewById(R.id.tab_tanya_garis);
        tmb_Tanya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tampilkan(new DaftarTanyaActWkr(), 1);
                gantiWarnaTab(1, tmbTab[0], tmbTab[1], warnaTab);
            }
        });

//        garis_Jawab= (ImageView) findViewById(R.id.tab_jawab_garis);
/*        tmb_Jawab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tampilkan(new ShareActWkr(), 2);
                gantiWarnaTab(2, tmbTab[0], tmbTab[1], warnaTab);
            }
        });*/
        tmb_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tampilkan(new ChatExprt(), 3);
                gantiWarnaTab(2, tmbTab[0], tmbTab[1], warnaTab);
            }
        });
        initHeader();
        initAdapter_Int();
        initBackPress();
    }

    @Override
    public void keHalaman(int ke) {
        bolehInitHeader= true;
        mvPager.setCurrentItem(ke);
    }

    private void initAdapter_Int(){
        aturIconTombolUtama(TOMBOL_UTAMA_HILANG, R.drawable.obj_pensil, R.drawable.obj_chat);
        mvPager = (ViewPager)findViewById(R.id.layout_wadah_fragment_wkr);
        aturFragmen(new ProfileActWkr(), new ShareActWkr(), new ChatExprt());
        aturJudul("Profil", "Pustaka", "Chat");
        initAdapter();
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
*/
                if(bolehInitHeader)
                    fragmenHalaman.ambil(position).initHeader();
                aturJudulHeader(judulHalaman.ambil(position));
                aksiGantiHalaman(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
/*
    protected void bolehInitHeader(boolean boleh){
        bolehInitHeader= boleh;
    }

    @Override
    protected void onStop() {
        bolehInitHeader= false;
        super.onStop();
    }
/*
    @Override
    protected void onResumeFragments() {
        bolehInitHeader= true;
        super.onResumeFragments();
    }
*/
/*
    @Override
    public void onAttachFragment(Fragment fragment) {
        fragmenHalaman.tambah((Fragment_Header)fragment);
        judulHalaman.tambah(((Fragment_Header)fragment).judulHeader());
        super.onAttachFragment(fragment);
    }


    private void initFragmenHolder(){
        fragmenHalaman.aturPenungguTraverse(new Array.PenungguTraverse<Void, Void>() {
            @Override
            public Void traverse(int indek, Object isi) {
                adapter.addFragment((Fragment_Header) isi, judulHalaman.ambil(indek));
                return null;
            }

            @Override
            public Void akhirTraverse(Void... hasilTraverse) {
                return null;
            }
        });
    }
    private void initFragmen(){
        initFragmenHolder();
        if(fragmenHalaman.ukuran() == 0) {
            Toast.makeText(this, "INIT ISI!!!", Toast.LENGTH_SHORT).show();
            fragmenHalaman.tambah(new Fragment_Header[]{new ProfileActWkr(), new ShareActWkr(), new ChatExprt()});
            judulHalaman.tambah(new String[]{"Profil", "Pustaka", "Chat"});
        }
    }

    private void initAdapter(){
        initFragmen();
        bolehInitHeader= false;
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmenHalaman.traverse();
        mvPager.setAdapter(adapter);
        mvPager.clearOnPageChangeListeners();
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
        mvPager.setOffscreenPageLimit(4);
    }
    private void lepaskanFragment(){
        for(int i= 0; i< fragmenHalaman.ukuran(); i++)
            fragmenHalaman.ambil(i).getFragmentManager().beginTransaction().remove(fragmenHalaman.ambil(i)).commit();
    }
*/
/*
    private void gantiIconTombolUtama(){
        int icon= iconTombolUtama[mvPager.getCurrentItem()];
        if(icon != TOMBOL_UTAMA_HILANG){
            tmbUtama.setImageResource(icon);
            tmbUtama.setVisibility(View.VISIBLE);
        }
        else
            tmbUtama.setVisibility(View.GONE);
    }
*/
    @Override
    protected void onResume() {
//        bolehInitHeader= false;
        super.onResume();
    }

    public interface PenungguGantiHalaman{
        void gantiHalaman(int halSebelumnnya, int halamanSkrg);
    }
    public void aturPenungguGantiHalaman(PenungguGantiHalaman png){
        pngGantiHalaman= png;
    }

    void tampilkan(Fragment frag, int halamanKe) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_wadah_fragment_wkr, frag);
        ft.commit();

        if(pngGantiHalaman != null)
            pngGantiHalaman.gantiHalaman(halamanFragmen, halamanKe);
        halamanFragmen= halamanKe;

        Toast.makeText(this, "ganti fragment", Toast.LENGTH_SHORT).show();
    }

    void tampilkan(ViewPager vPager, int posisi) {
        vPager.setCurrentItem(posisi);
    }

    void gantiWarnaTab(int posisi, int[] ikon, int[] garis, int[][] warnaTab){
        saringTerpilih(ikon, (ImageView) findViewById(ikon[posisi]), warnaTab[0],
                garis, (ImageView) findViewById(garis[posisi]), warnaTab[1]);
        tampilkan(mvPager, posisi);
    }
    void saringTerpilih(int id[], ImageView img) {
        // warna[0]= belum; warna[1]= terpilih;
        int warna[] = {R.color.colorAccent, R.color.colorPrimaryDark};
        saringTerpilih(id, img, warna);
    }

    void saringTerpilih(int id[], ImageView img, int warna[]) {
        ImageView imgLain;
        for (int i = 0; i < id.length; i++) {
            imgLain = (ImageView) findViewById(id[i]);
            imgLain.setColorFilter(ambilWarna(warna[0]));
        }
        img.setColorFilter(ambilWarna(warna[1]));
    }

    void saringTerpilih(int id[], ImageView img, int idBg[], ImageView bg) {
        // warna[0]= belum; warna[1]= terpilih;
        int warna[] = {R.color.colorAccent, R.color.colorPrimaryDark};
        int warnaBg[] = {R.color.colorAccent, R.color.colorPrimaryDark};
        saringTerpilih(id, img, warna, idBg, bg, warnaBg);
    }

    void saringTerpilih(int id[], ImageView img, int warna[], int idBg[], ImageView bg, int warnaBg[]) {
        ImageView imgLain;
        for (int i = 0; i < id.length; i++) {
            imgLain = (ImageView) findViewById(id[i]);
            imgLain.setColorFilter(ambilWarna(warna[0]));
        }
        img.setColorFilter(ambilWarna(warna[1]));

        ImageView bgLain;
        for (int i = 0; i < id.length; i++) {
            bgLain = (ImageView) findViewById(idBg[i]);
            bgLain.setBackgroundColor(ambilWarna(warnaBg[0]));
        }
        bg.setBackgroundColor(ambilWarna(warnaBg[1]));
    }

    int ambilWarna(int id) {
        return getResources().getColor(id);
    }

    void pesan(String pesan) {
        Toast.makeText(this, pesan, Toast.LENGTH_LONG).show();
    }

    private void initBackPress() {
        aturAksiBackPress_Int(new AksiBackPress_Internal() {
            @Override
            public void backPress_Int() {
                if(click_duaKali){
                    Intent pergi = new Intent(Intent.ACTION_MAIN);
                    pergi.addCategory(Intent.CATEGORY_HOME);
                    pergi.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(pergi);
//                    finish();
//                    System.exit(0);
                }
                Toast.makeText(getApplicationContext(), "Press BACK again to exit!", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        click_duaKali=false;
                    }
                },3000);
                click_duaKali=true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
