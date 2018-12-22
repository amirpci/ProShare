package sidev17.siits.proshare.Modul;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import sidev17.siits.proshare.R;

public class SettingAct extends AppCompatActivity {

    private View gantiPassword, logout, hapusAkun, gantiBahasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        gantiPassword= findViewById(R.id.setting_isi_1);
        logout= findViewById(R.id.setting_isi_2);
        hapusAkun= findViewById(R.id.setting_isi_3);
        gantiBahasa= findViewById(R.id.setting_isi_4);
    }

    void gantiPassword(){
        //lakukan sesuatu
    }
    void logout(){
        //lakukan sesuatu
    }
    void hapusAkun(){
        //lakukan sesuatu
    }
    void gantiBahasa(){
        //lakukan sesuatu
    }
}
