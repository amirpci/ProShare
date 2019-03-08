package sidev17.siits.proshare.Modul;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Login_Register.GantiPassword;
import sidev17.siits.proshare.Login_Register.Login;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.PackBahasa;
import sidev17.siits.proshare.Utils.Terjemahan;
import sidev17.siits.proshare.Utils.Utilities;

public class SettingAct extends AppCompatActivity implements View.OnClickListener{

    private View gantiPassword, logout, hapusAkun, gantiBahasa;
    private TextView textSetting[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        textSetting = new TextView[8];
        textSetting[0] = findViewById(R.id.txt_pengaturan);
        textSetting[1] = findViewById(R.id.setting_judul_akun);
        textSetting[2] = findViewById(R.id.setting_isi_1_teks);
        textSetting[3] = findViewById(R.id.setting_isi_2_teks);
        textSetting[4] = findViewById(R.id.setting_isi_3_teks);
        textSetting[5] = findViewById(R.id.setting_judul_bahasa);
        textSetting[6] = findViewById(R.id.setting_isi_4_teks);
        textSetting[7] = findViewById(R.id.setting_isi_4_penjelasan);
        gantiPassword= findViewById(R.id.setting_isi_1);
        logout= findViewById(R.id.setting_isi_2);
        hapusAkun= findViewById(R.id.setting_isi_3);
        gantiBahasa= findViewById(R.id.setting_isi_4);
        gantiBahasa.setOnClickListener(this);
        logout.setOnClickListener(this);
        hapusAkun.setOnClickListener(this);
        gantiPassword.setOnClickListener(this);
        muatBahasa();
    }

    void muatBahasa(){
        int index = Terjemahan.indexBahasa(this);
        for(int i = 0 ; i < textSetting.length - 1; i++)
            textSetting[i].setText(PackBahasa.setting[index][i]);
        textSetting[7].setText(PackBahasa.settingPilihanBahasa[index][index]);
    }

    void gantiPassword(){
        //lakukan sesuatu
        startActivity(new Intent(this, GantiPassword.class));
    }
    void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SettingAct.this, Login.class);
        intent.putExtra(Konstanta.LOGIN_INTENT, Konstanta.LOGIN_LOGOUT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        //lakukan sesuatu
    }
    void hapusAkun(){
        //lakukan sesuatu
    }
    void gantiBahasa(){
        //lakukan sesuatu
        int index = Terjemahan.indexBahasa(this);
        String[] bahasa = PackBahasa.settingPilihanBahasa[index];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(PackBahasa.setting[index][7]);
        builder.setItems(bahasa, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (which){
                    case 0:
                        Utilities.setUserBahasa(SettingAct.this, "en");
                        break;
                    case 1:
                        Utilities.setUserBahasa(SettingAct.this, "id");
                        break;
                    case 2:
                        Utilities.setUserBahasa(SettingAct.this, "ja");
                        break;
                }
                muatBahasa();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_isi_1 : {
                gantiPassword();
                break;
            }
            case R.id.setting_isi_2 : {
                logout();
                break;
            }
            case R.id.setting_isi_3 : {
                hapusAkun();
                break;
            }
            case R.id.setting_isi_4 : {
                gantiBahasa();
                break;
            }
        }
    }
}
