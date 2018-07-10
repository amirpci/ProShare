package sidev17.siits.proshare.Modul.Expert;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import sidev17.siits.proshare.Modul.Expert.Tab.FeedbackActExprt;
import sidev17.siits.proshare.Modul.Expert.Tab.JawabActExprt;
import sidev17.siits.proshare.Modul.Expert.Tab.ProfileActExprt;
import sidev17.siits.proshare.Modul.Expert.Tab.TimelineActExprt;
import sidev17.siits.proshare.R;

public class MainActivityExprt extends AppCompatActivity {

    private LinearLayout tmb_Profile, tmb_Jawab, tmb_Timeline, tmb_Feedback;
    private ImageView garis_Profile, garis_Timeline, garis_Jawab, garis_Feedback;

    private boolean click_duaKali=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_exprt);
        final int tmbTab[][] = {{R.id.tab_profile_ikon_Exprt, R.id.tab_jawab_ikon_Exprt, R.id.tab_timeline_ikon_Exprt, R.id.tab_feedback_ikon_Exprt},
                {R.id.tab_profile_garis_Exprt, R.id.tab_jawab_garis_Exprt, R.id.tab_timeline_garis_Exprt, R.id.tab_feedback_garis_Exprt}};
        final int warnaTab[][] = {{R.color.colorAccent, R.color.colorPrimaryDark},
                {R.color.colorPrimary, R.color.colorPrimaryDark}};

       // final ImageView icon_Profile = (ImageView) findViewById(R.id.tab_profile_ikon);
      //  garis_Profile = (ImageView) findViewById(R.id.tab_profile_garis);

        tmb_Profile = (LinearLayout) findViewById(R.id.tab_profile_Exprt);
        tmb_Timeline = (LinearLayout) findViewById(R.id.tab_timeline_Exprt);
        tmb_Jawab = (LinearLayout) findViewById(R.id.tab_jawab_Exprt);
        tmb_Feedback = (LinearLayout) findViewById(R.id.tab_feedback_Exprt);


        //Buat Warna Inisiasi
        saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_jawab_ikon_Exprt), warnaTab[0],
                tmbTab[1], (ImageView) findViewById(R.id.tab_jawab_garis_Exprt), warnaTab[1]);

//        garis_Profile= (ImageView) findViewById(R.id.tab_profile_garis);
        tmb_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan(new ProfileActExprt());
                saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_profile_ikon_Exprt), warnaTab[0],
                        tmbTab[1], (ImageView) findViewById(R.id.tab_profile_garis_Exprt), warnaTab[1]);
            }
        });

//        garis_Tanya= (ImageView) findViewById(R.id.tab_tanya_garis);
        tmb_Timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan(new TimelineActExprt());
                saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_timeline_ikon_Exprt), warnaTab[0],
                        tmbTab[1], (ImageView) findViewById(R.id.tab_timeline_garis_Exprt), warnaTab[1]);
            }
        });

//        garis_Jawab= (ImageView) findViewById(R.id.tab_jawab_garis);
        tmb_Jawab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan(new JawabActExprt());
                saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_jawab_ikon_Exprt), warnaTab[0],
                        tmbTab[1], (ImageView) findViewById(R.id.tab_jawab_garis_Exprt), warnaTab[1]);
            }
        });

        tmb_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan(new FeedbackActExprt());
                saringTerpilih(tmbTab[0], (ImageView) findViewById(R.id.tab_feedback_ikon_Exprt), warnaTab[0],
                        tmbTab[1], (ImageView) findViewById(R.id.tab_feedback_garis_Exprt), warnaTab[1]);
            }
        });
    }


    void tampilkan(Fragment frag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_wadah_fragment_Exprt, frag);
        ft.commit();
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
        Toast.makeText(MainActivityExprt.this, "Press BACK again to exit!", Toast.LENGTH_LONG).show();
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
