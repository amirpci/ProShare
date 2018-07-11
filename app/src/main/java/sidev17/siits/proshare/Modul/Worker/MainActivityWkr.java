package sidev17.siits.proshare.Modul.Worker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import sidev17.siits.proshare.Modul.Worker.Tab.DaftarTanyaActWkr;
import sidev17.siits.proshare.Modul.Worker.Tab.ProfileActWkr;
import sidev17.siits.proshare.Modul.Worker.Tab.ShareActWkr;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.ViewPagerAdapter;

public class MainActivityWkr extends AppCompatActivity {

    private LinearLayout tmb_Profile;
    private ImageView garis_Profile;

    private LinearLayout tmb_Tanya;
    private ImageView garis_Tanya;

    private LinearLayout tmb_Jawab;
    private ImageView garis_Jawab;

    private ViewPagerAdapter adapter;
    private ViewPager mvPager;
    private boolean click_duaKali=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wkr);
        final int tmbTab[][] = {{R.id.tab_profile_ikon_wkr, R.id.tab_tanya_ikon_wkr, R.id.tab_jawab_ikon_wkr},
                {R.id.tab_profile_garis_wkr, R.id.tab_tanya_garis_wkr, R.id.tab_jawab_garis_wkr}};
        final int warnaTab[][] = {{R.color.colorAccent, R.color.colorPrimaryDark},
                {R.color.colorPrimary, R.color.colorPrimaryDark}};

        final ImageView icon_Profile = (ImageView) findViewById(R.id.tab_profile_ikon_wkr);
        garis_Profile = (ImageView) findViewById(R.id.tab_profile_garis_wkr);
        tmb_Profile = (LinearLayout) findViewById(R.id.tab_profile_wkr);
        tmb_Tanya = (LinearLayout) findViewById(R.id.tab_tanya_wkr);
        tmb_Jawab = (LinearLayout) findViewById(R.id.tab_jawab_wkr);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mvPager = (ViewPager)findViewById(R.id.layout_wadah_fragment_wkr);
        adapter.AddFragment(new ProfileActWkr(), "");
        adapter.AddFragment(new ShareActWkr(), "");
        adapter.AddFragment(new DaftarTanyaActWkr(), "");
        mvPager.setAdapter(adapter);

        mvPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                gantiWarnaTab(position, tmbTab[0], tmbTab[1],warnaTab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Buat Warna Inisiasi
       saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_profile_ikon_wkr), warnaTab[0],
                tmbTab[1], (ImageView) findViewById(R.id.tab_profile_garis_wkr), warnaTab[1]);

//        garis_Profile= (ImageView) findViewById(R.id.tab_profile_garis_wkr);
        tmb_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan(mvPager, 0);
            }
        });

//        garis_Tanya= (ImageView) findViewById(R.id.tab_tanya_garis);
        tmb_Tanya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan(mvPager, 1);
            }
        });

//        garis_Jawab= (ImageView) findViewById(R.id.tab_jawab_garis);
        tmb_Jawab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan(mvPager, 2);
            }
        });
    }
/*
    void tampilkan(Fragment frag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_wadah_fragment_wkr, frag);
        ft.commit();
    }
*/
    void tampilkan(ViewPager vPager, int posisi) {
        vPager.setCurrentItem(posisi);
    }

    void gantiWarnaTab(int posisi, int[] ikon, int[] garis, int[][] warnaTab){
        saringTerpilih(ikon, (ImageView) findViewById(ikon[posisi]), warnaTab[0],
                garis, (ImageView) findViewById(garis[posisi]), warnaTab[1]);
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
    public void onBackPressed() {
        if(click_duaKali){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            System.exit(0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
