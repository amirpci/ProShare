package sidev17.siits.procks.Modul.Expert;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import sidev17.siits.procks.Modul.Expert.Tab.FeedbackActExprt;
import sidev17.siits.procks.Modul.Expert.Tab.JawabActExprt;
import sidev17.siits.procks.Modul.Expert.Tab.ProfileActExprt;
import sidev17.siits.procks.Modul.Expert.Tab.TimelineActExprt;
import sidev17.siits.procks.Utils.PackBahasa;
import sidev17.siits.procks.Utils.Terjemahan;
import sidev17.siits.procks.Utils.ViewTool.Aktifitas_Slider;
import sidev17.siits.procks.Utils.ViewTool.MainAct_Header;
import sidev17.siits.procks.R;
import sidev17.siits.procks.ViewPagerAdapter;

public class MainActivityExprt extends MainAct_Header implements Aktifitas_Slider {

    private LinearLayout tmb_Profile, tmb_Jawab, tmb_Timeline, tmb_Feedback;
    private ImageView garis_Profile, garis_Timeline, garis_Jawab, garis_Feedback;

    protected ViewPagerAdapter adapter;
    private boolean click_duaKali=false;
    private PenungguGantiHalaman pngGantiHalaman;

    private final int tmbTab[][] = {{R.id.tab_profile_ikon_Exprt, R.id.tab_jawab_ikon_Exprt, R.id.tab_timeline_ikon_Exprt, R.id.tab_feedback_ikon_Exprt},
            {R.id.tab_profile_garis_Exprt, R.id.tab_jawab_garis_Exprt, R.id.tab_timeline_garis_Exprt, R.id.tab_feedback_garis_Exprt}};
    private final int warnaTab[][] = {{R.color.colorAccent, R.color.colorPrimaryDark},
            {R.color.colorPrimary, R.color.colorPrimaryDark}};

//    private Array<Fragment_Header> fragmenHalaman= new Array<>(); //= {new ProfileActWkr(), new ShareActWkr(), new ChatExprt()};
//    private Array<String> judulHalaman= new Array<>(); //= {"Profil", "Pustaka", "Chat"};
//    private boolean bolehInitHeader= false; //false diperoleh hanya sekali saat pertama kali di-init

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_exprt);

        bolehInitHeader= false;

        tombolUtama((ImageView)findViewById(R.id.tombol_utama));

        tmb_Profile = (LinearLayout) findViewById(R.id.tab_profile_Exprt);
        tmb_Timeline = (LinearLayout) findViewById(R.id.tab_timeline_Exprt);
        tmb_Jawab = (LinearLayout) findViewById(R.id.tab_jawab_Exprt);
        tmb_Feedback = (LinearLayout) findViewById(R.id.tab_feedback_Exprt);

        initAdapter_Int();
/*
        adapter.addFragment(new ProfileActExprt(), "");
        adapter.addFragment(new JawabActExprt(), "");
        adapter.addFragment(new TimelineActExprt(), "");
        adapter.addFragment(new FeedbackActExprt(), "");
*/


        //Buat Warna Inisiasi
        saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_profile_ikon_Exprt), warnaTab[0],
                tmbTab[1], (ImageView) findViewById(R.id.tab_profile_garis_Exprt), warnaTab[1]);

//        garis_Profile= (ImageView) findViewById(R.id.tab_profile_garis);
        tmb_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // tampilkan(new ProfileActExprt());
                gantiWarnaTab(0, tmbTab[0], tmbTab[1],warnaTab);
                //tampilkan(mvPager, 0);
            }
        });

//        garis_Tanya= (ImageView) findViewById(R.id.tab_tanya_garis);
        tmb_Timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   tampilkan(new TimelineActExprt());
                gantiWarnaTab(2, tmbTab[0], tmbTab[1],warnaTab);
                //tampilkan(mvPager, 2);
            }
        });

//        garis_Jawab= (ImageView) findViewById(R.id.tab_jawab_garis);
        tmb_Jawab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // tampilkan(new JawabActExprt());
                gantiWarnaTab(1, tmbTab[0], tmbTab[1],warnaTab);
                //tampilkan(mvPager, 1);
            }
        });

        tmb_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // tampilkan(new FeedbackActExprt());
                gantiWarnaTab(3, tmbTab[0], tmbTab[1],warnaTab);
            }
        });
        initHeader();
        initBackPress();
    }

    @Override
    public void keHalaman(int ke) {
        bolehInitHeader= true;
        mvPager.setCurrentItem(ke);
    }

    private void initAdapter_Int(){
        aturIconTombolUtama(TOMBOL_UTAMA_HILANG, TOMBOL_UTAMA_HILANG, R.drawable.obj_pensil, R.drawable.obj_chat);
        mvPager = (ViewPager)findViewById(R.id.layout_wadah_fragment_Exprt);
        aturFragmen(new ProfileActExprt(), new JawabActExprt(), new TimelineActExprt(), new FeedbackActExprt());
        aturJudul(PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][0],
                PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][1],
                PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][2],
                PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][3]);
        initAdapter();
        mvPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                gantiWarnaTab(position, tmbTab[0], tmbTab[1], warnaTab);
                gantiIconTombolUtama();
//                gantiIconTombolUtama();

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

    @Override
    protected void onResume() {
        super.onResume();
        perbaruiJudul(PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][0],
                PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][1],
                PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][2],
                PackBahasa.mainHeader[Terjemahan.indexBahasa(this)][3]);
        aturJudulHeader(judulHalaman.ambil(0));
    }

    /*
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
            fragmenHalaman.tambah(new Fragment_Header[]{new ProfileActExprt(), new JawabActExprt(), new TimelineActExprt(), new FeedbackActExprt()});
            judulHalaman.tambah(new String[]{"Profil", "Pertanyaan yang Harus Dijawab", "Pustaka", "Chat"});
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
                if(position != 1)
                    aturTambahanHeader("");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mvPager.setOffscreenPageLimit(4);
    }
*/

    public interface PenungguGantiHalaman{
        void gantiHalaman(int halSebelumnnya, int halamanSkrg);
    }
    public void aturPenungguGantiHalaman(PenungguGantiHalaman png){
        pngGantiHalaman= png;
    }

    void tampilkan(Fragment frag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_wadah_fragment_Exprt, frag);
        ft.addToBackStack(null);
        ft.commit();
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
                Toast.makeText(MainActivityExprt.this, "Press BACK again to exit!", Toast.LENGTH_LONG).show();
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
